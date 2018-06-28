package com.laf.mall.api.service.impl;

import com.laf.mall.api.dao.MemberDao;
import com.laf.mall.api.dao.ParkingCouponDao;
import com.laf.mall.api.dto.*;
import com.laf.mall.api.querycondition.coupon.CouponQueryCondition;
import com.laf.mall.api.querycondition.coupon.CouponReceiveCondition;
import com.laf.mall.api.querycondition.coupon.MyCouponListQueryCondition;
import com.laf.mall.api.querycondition.points.PointsActionCondition;
import com.laf.mall.api.service.ParkingCouponService;
import com.laf.mall.api.service.PointsService;
import com.laf.mall.api.utils.datetime.DateTimeUtils;
import com.laf.manager.core.exception.MallDBException;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ParkingCouponServiceImpl implements ParkingCouponService {

    @Autowired
    private ParkingCouponDao couponDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private DateTimeUtils dateTimeUtils;

    @Autowired
    PointsService pointsService;

    @Override
    public List<Parking> getParkingList(int member_id) {
        return couponDao.getParkingList(member_id);
    }

    @Override
    public int saveOrUpdateParking(Parking parking) {

        return 0;
    }

    @Override
    public List<ReceiveCouponInfo> getCouponInfoList(final CouponQueryCondition condition) {

        long registTime = 0;
        if (condition.getMemberId() != null && condition.getMemberId() > 0) { //会员注册时间在sql语句里与注册时间的区间进行比较，在入库时应为年月日，这里不作处理
            registTime = memberDao.getRegistTime4Member(condition.getMemberId(), condition.getMallId());
        }

        return couponDao.getCouponInfoList(condition, registTime);
    }

    @Override
    public ReceiveCouponInfo getCouponInfoById(final Integer couponId, final Integer memberId) {
        long today = dateTimeUtils.getMilliWithoutTime(new Date());
        ReceiveCouponInfo receiveCouponInfo = couponDao.getCouponInfoById(couponId, today);

        if (receiveCouponInfo == null) return null;

        if (today < receiveCouponInfo.getIssue_start().getTime() || today > receiveCouponInfo.getIssue_end().getTime()) { //发行期入库时应为年月日,这里不作处理
            receiveCouponInfo.setLimitPromptCode(0);
            return receiveCouponInfo; //此券已经终止发行提示
        }

        //实时库存，限制条件。总数不变，剩余数在不断变化，限制条件的成立建立在剩余数的前提上
        //日发行量-当天领取张数=当天剩余张数
        int circulation = receiveCouponInfo.getCirculation(); //总发行量
        int circulationDaily = receiveCouponInfo.getDaily_circulation(); //日发行量
        int receivedTotal = receiveCouponInfo.getReceivedTotal(); //总领取张数
        int receivedDaily = receiveCouponInfo.getReceivedDaily(); //当天领取张数

        //总发行量-总领取张数=库存
        int inventory = circulation - receivedTotal;
        if (inventory <= 0) {
            receiveCouponInfo.setLimitPromptCode(1);
            return receiveCouponInfo; //库存不足
        }


        if (circulationDaily > 0) {
            //circulationDaily = circulationDaily > inventory ? inventory : circulationDaily; //当日限制总数
            if (circulationDaily - receivedDaily <= 0) { //日发行量-当天领取张数=当天剩余张数
                receiveCouponInfo.setLimitPromptCode(2);
                return receiveCouponInfo; //当天已不能领取
            }
        }

        if (receiveCouponInfo.getGroup_id() != -1) { //优惠券组只有在状态为发布时,才应更新相关券的group_id。其他任何情况下，group_id=-1

            //判断是否已领取券组里其他任意一种券
            //1.找到该组除自己以外所有券(u)
            //2.u中是否有任一种券已领取
            int count = couponDao.getReceivedCountInCouponGroup(couponId, memberId, receiveCouponInfo.getGroup_id());
            if (count > 0) {
                receiveCouponInfo.setLimitPromptCode(3);
                return receiveCouponInfo; //已领取券组中的其他券
            }
        }

        if (receiveCouponInfo.getReceive_method() == 0) {
            int requiredPoints = receiveCouponInfo.getRequired_points();
            int usablePoints = memberDao.getUsablePoints4Member(memberId);

            if (usablePoints < requiredPoints) {
                receiveCouponInfo.setLimitPromptCode(4);
                return receiveCouponInfo; //积分不足
            }
        }

        int limited = receiveCouponInfo.getLimited(); //限制张数
        int dailyLimited = receiveCouponInfo.getDaily_limited(); //日限制张数
        int keepVerificationOf = receiveCouponInfo.getKeep_verification_of(); //保留核销数
        int memberNonVerification = couponDao.getNonVerificationCouponByMember(memberId, couponId); //有几张未核销
        int memberTotalReceived = couponDao.getReceivedByMember(memberId, couponId); //已领取张数
        int memberDailyReceived = couponDao.getDailyReceivedByMember(memberId, couponId, today); //每日领取张数
        int verification = receiveCouponInfo.getVerification_of(); //总核销量

        //limited受库存限制
        if (limited > 0) {
            //limited = limited > inventory ? inventory : limited;
            if (limited <= memberTotalReceived) {
                log.info("CouponServiceImpl:===========[已领取{}达到总限领张数{}]", memberTotalReceived, limited);
                receiveCouponInfo.setLimitPromptCode(5);
                return receiveCouponInfo; //已领取
            }
        }

        //1.有限制张数，限制张数-已领取张数
        //2.没有限制张数，
        if (dailyLimited > 0) {
            //dailyLimited = dailyLimited > (limited - memberTotalReceived) ? (limited - memberTotalReceived) : dailyLimited;
            if (dailyLimited <= memberDailyReceived) {
                log.info("CouponServiceImpl:===========[当天领取{}已达到限领张数{}]", memberDailyReceived, dailyLimited);
                receiveCouponInfo.setLimitPromptCode(5);
                return receiveCouponInfo; //已领取
            }
        }

        if (keepVerificationOf > 0 && memberNonVerification > keepVerificationOf) {
            log.info("CouponServiceImpl:===========[未核销数{}超过保留张数{}张]", memberNonVerification, keepVerificationOf);
            receiveCouponInfo.setLimitPromptCode(5);
            return receiveCouponInfo; //已领取
        }

        receiveCouponInfo.setLimitPromptCode(6);
        return receiveCouponInfo;
    }

    public List<Coupon> getCouponListByMember(final MyCouponListQueryCondition condition) {
        List<Coupon> list = couponDao.getCouponListByMember(condition);

        return list;
    }

    @Override
    public Coupon getCouponById(final Integer crlId) {

        Coupon coupon = couponDao.getCouponById(crlId);

        return coupon;
    }

    @Override
    public int receiveCouponByMember(int memberId, int couponId) {
        Member member = memberDao.getMemberById(memberId);
        if (member == null) {
            return 7;
        }

        ReceiveCouponInfo info = this.getCouponInfoById(couponId, memberId);

        if (info.getLimitPromptCode() != 6) return info.getLimitPromptCode(); //领取之前判断能否领取

        CouponReceiveCondition condition = new CouponReceiveCondition();
        if (info.getCoupon_type() == 0) {
            condition.setCouponStatus(1);
        } else if (info.getCoupon_type() == 1) {
            condition.setCouponStatus(0);
        }
        condition.setCouponId(couponId);
        condition.setMemberId(memberId);
        condition.setMallId(info.getMall_id());
        int result = this.saveCouponForMember(condition);

        if (result <= 0) return 7;

        if (info.getReceive_method() == 0) {
            PointsActionCondition actionCondition = new PointsActionCondition();
            actionCondition.setMemberId(member.getMember_id());
            actionCondition.setMemberName(member.getName());
            actionCondition.setMemberMobile(member.getMobile());
            actionCondition.setMallId(member.getMall_id());
            actionCondition.setSources(6);
            actionCondition.setPoints(info.getRequired_points() * -1);
            actionCondition.setHandleDate(new Date(condition.getCurrTime())); //检查处理时间是否为年月日
            result = pointsService.addPointsExcludeConsume(actionCondition);

            if (result <= 0) throw new MallDBException(7);

            member.setUsable_points(member.getUsable_points() - info.getRequired_points());
            result = memberDao.updateMember(member);

            if (result <= 0) throw new MallDBException(7);
        }

        return 8;
    }

    @Override
    @Transactional
    public int toVerify(final Integer memberId, final Integer crlId) throws MallDBException {
        //检查memberId是否为核销人员
        VerificationClerk vc = memberDao.getVerificationClerk(memberId);
        if (vc == null || vc.getStatus() != 0) return 0;

        Coupon coupon = couponDao.getCouponById(crlId);
        if (coupon == null || coupon.getCoupon_status() == 2) return 0;

        //检查是否在这张券的核销范围内
        int count = couponDao.getVerificationWideByShop(vc, coupon.getCoupon_id());
        if (count <= 0) return 0;

        //检查券的有效期
        long today = dateTimeUtils.getMilliByToDay();
        if (today < coupon.getExpiry_date_start() || today > coupon.getExpiry_date_end()) return 0;

        //检查核销数量
        int totalVerify = coupon.getVerification_of();
        int total = couponDao.getCouponCountByStatus(coupon.getCoupon_id(), 2);
        if(total >= totalVerify) return 0;

        int result = couponDao.updateCouponStatus(2, crlId);

        if (result > 0) {
            result = couponDao.saveVerification(vc, coupon);

            if (result <= 0) throw new MallDBException();
        }

        return result;
    }

    public String toJson(ReceiveCouponInfo receiveCouponInfo) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(receiveCouponInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }



    /**
     * 激活
     * @param memberId
     * @param crlId
     * @param mallId
     * @return 6 激活失败
     *         7 已激活
     * @throws MallDBException
     */
    @Override
    @Transactional
    public int toActivate(Integer memberId, Integer crlId, final Integer mallId) throws MallDBException {

//        VerificationClerk vc = memberDao.getClerk(memberId, 0, mallId);
        VerificationClerk vc = memberDao.getVerificationClerk(memberId);
        if (vc == null) return 0;

        Coupon coupon = couponDao.getCouponById(crlId);
        if (coupon == null || coupon.getCoupon_status() > 0) return 1;

        long today = dateTimeUtils.getMilliByToDay();
        if (today < coupon.getExpiry_date_start() || today > coupon.getExpiry_date_end()) return 2;

        if (today < coupon.getActive_time()) return 3;

        int activate = couponDao.getCouponCountByStatus(coupon.getMember_id(), coupon.getCoupon_id(), 1);
        if (activate >= coupon.getActivable()) return 4;

        int result = 0;

        Activate act = new Activate();
        act.setVc_id(vc.getVc_id());
        act.setActive_time(new Date());
        act.setCoupon_id(coupon.getCoupon_id());
        act.setCri_id(crlId);
        act.setMall_id(mallId);
        act.setMember_id(coupon.getMember_id());
        act.setReceive_date(new Date(coupon.getReceive_date()));
        result = couponDao.saveCouponActiveLog(act);

        if (result <= 0) throw new MallDBException(6);

        result = couponDao.updateCouponStatus(1, crlId);

        if (result <= 0) throw new MallDBException(6);

        return 7;
    }



    /**
     * 校验激活(不包括核查工作人员)
     * @param crlId
     * @return
     */
    public int verifyActiveExcludeClerk(Integer crlId) {
        Coupon coupon = couponDao.getCouponById(crlId);
        if (coupon == null || coupon.getCoupon_status() > 0) return 1;

        //检查券的有效期
        long today = dateTimeUtils.getMilliByToDay();
        if (today < coupon.getExpiry_date_start() || today > coupon.getExpiry_date_end()) return 2;

        if (today < coupon.getActive_time()) return 3;

        //
        int activate = couponDao.getCouponCountByStatus(coupon.getMember_id(), coupon.getCoupon_id(), 1);
        if (activate >= coupon.getActivable()) return 4;

        return 5;
    }

    public int saveCouponForMember(CouponReceiveCondition condition) {
        return couponDao.saveReceivedCouponByMember(condition);
    }

    @Override
    public int updateCarInfo(Integer memberId, String carNumber) {
        List<Parking> list = couponDao.getParkingList(memberId, carNumber);
        int result = 0;
        if(list == null && list.size() == 0){
            couponDao.updateParking(memberId);
            Parking park = new Parking();
            park.setIsdefault(1);//默认
            park.setCar_number(carNumber);
            park.setMember_id(memberId);
            result = couponDao.saveParking(park);
        }else{
            Parking park = list.get(0);
            if(park.getIsdefault() == 0){
                result = couponDao.updateParking(memberId,carNumber);
            }
        }
        return result;
    }

    @Override
    public ParkingInfo getParkingInfo() {
        return couponDao.getParkingInfo();
    }

    @Override
    public int parkingPay(Integer memberId, String carNumber, String ticketNo, Coupon coupon, BigDecimal price, Date in_date) {
        int result;
        Member member = memberDao.getMemberById(memberId);
        if(member == null) return 0;
        this.updateCarInfo(memberId,carNumber);
        if(coupon != null){
            if(coupon.getMember_id() != memberId || coupon.getCoupon_status() != 1)  return 0;
            couponDao.updateCouponStatus(2, coupon.getCrl_id());//更新状态已使用
            result = couponDao.saveParkingLog(coupon.getCrl_id(),memberId,carNumber,price,in_date,ticketNo);
        }else{
            result = couponDao.saveParkingLog(0,memberId,carNumber,price,in_date,ticketNo);
        }
        if(result > 0) return 1;
        return 0;

    }
}
