package com.laf.mall.api.service.impl;

import com.laf.mall.api.dao.GiftCouponDao;
import com.laf.mall.api.dao.MemberDao;
import com.laf.mall.api.dto.*;
import com.laf.mall.api.querycondition.coupon.CouponForShopQueryCondition;
import com.laf.mall.api.querycondition.coupon.CouponReceiveCondition;
import com.laf.mall.api.querycondition.coupon.MyCouponListQueryCondition;
import com.laf.mall.api.querycondition.giftcoupon.GiftCouponQueryCondition;
import com.laf.mall.api.querycondition.points.PointsActionCondition;
import com.laf.mall.api.service.GiftCouponService;
import com.laf.mall.api.service.PointsService;
import com.laf.mall.api.utils.datetime.DateTimeUtils;
import com.laf.manager.core.exception.MallDBException;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class GiftCouponServiceImpl implements GiftCouponService {

    @Autowired
    private GiftCouponDao giftCouponDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private PointsService pointsService;

    @Autowired
    private DateTimeUtils dateTimeUtils;

    @Override
    public List<GiftCouponInfo> getGiftCouponInfoList(GiftCouponQueryCondition condition) {

        long registTime = 0;
        if (condition.getMemberId() != null && condition.getMemberId() > 0) {
            registTime = memberDao.getRegistTime4Member(condition.getMemberId(), condition.getMallId());
        }

        return giftCouponDao.getGiftCouponInfoList(condition, registTime);
    }

    @Override
    public GiftCouponInfo getGiftCouponInfoById(Integer couponId, Integer memberId) {
        long today = dateTimeUtils.getMilliWithoutTime(new Date());
        GiftCouponInfo giftCouponInfo = giftCouponDao.getGiftCouponInfoById(couponId, today);

        if (giftCouponInfo == null) return null;

        if (today < giftCouponInfo.getIssue_start().getTime() || today > giftCouponInfo.getIssue_end().getTime()) { //发行期入库时应为年月日,这里不作处理
            giftCouponInfo.setLimitPromptCode(0);
            return giftCouponInfo; //此券已经终止发行提示
        }

        //实时库存，限制条件。总数不变，剩余数在不断变化，限制条件的成立建立在剩余数的前提上
        //日发行量-当天领取张数=当天剩余张数
        int circulation = giftCouponInfo.getCirculation(); //总发行量
        int circulationDaily = giftCouponInfo.getDaily_circulation(); //日发行量
        int receivedTotal = giftCouponInfo.getReceivedTotal(); //总领取张数
        int receivedDaily = giftCouponInfo.getReceivedDaily(); //当天领取张数

        //总发行量-总领取张数=库存
        int inventory = circulation - receivedTotal;
        if (inventory <= 0) {
            giftCouponInfo.setLimitPromptCode(1);
            return giftCouponInfo; //库存不足
        }


        if (circulationDaily > 0) {
            //circulationDaily = circulationDaily > inventory ? inventory : circulationDaily; //当日限制总数
            if (circulationDaily - receivedDaily <= 0) { //日发行量-当天领取张数=当天剩余张数
                giftCouponInfo.setLimitPromptCode(2);
                return giftCouponInfo; //当天已不能领取
            }
        }

        if (giftCouponInfo.getReceive_method() == 0) {
            int requiredPoints = giftCouponInfo.getRequired_points();
            int usablePoints = memberDao.getUsablePoints4Member(memberId);

            if (usablePoints < requiredPoints) {
                giftCouponInfo.setLimitPromptCode(4);
                return giftCouponInfo; //积分不足
            }
        }

        int limited = giftCouponInfo.getLimited(); //限制张数
        int dailyLimited = giftCouponInfo.getDaily_limited(); //日限制张数
        int keepVerificationOf = giftCouponInfo.getKeep_verification_of(); //保留核销数
        int memberNonVerification = giftCouponDao.getNonVerificationCouponByMember(memberId, couponId); //有几张未核销
        int memberTotalReceived = giftCouponDao.getReceivedByMember(memberId, couponId); //已领取张数
        int memberDailyReceived = giftCouponDao.getDailyReceivedByMember(memberId, couponId, today); //每日领取张数
        int verification = giftCouponInfo.getVerification_of(); //总核销量

        //limited受库存限制
        if (limited > 0) {
            //limited = limited > inventory ? inventory : limited;
            if (limited <= memberTotalReceived) {
                log.info("CouponServiceImpl:===========[已领取{}达到总限领张数{}]", memberTotalReceived, limited);
                giftCouponInfo.setLimitPromptCode(5);
                return giftCouponInfo; //已领取
            }
        }

        //1.有限制张数，限制张数-已领取张数
        //2.没有限制张数，
        if (dailyLimited > 0) {
            //dailyLimited = dailyLimited > (limited - memberTotalReceived) ? (limited - memberTotalReceived) : dailyLimited;
            if (dailyLimited <= memberDailyReceived) {
                log.info("CouponServiceImpl:===========[当天领取{}已达到限领张数{}]", memberDailyReceived, dailyLimited);
                giftCouponInfo.setLimitPromptCode(5);
                return giftCouponInfo; //已领取
            }
        }

        if (keepVerificationOf > 0 && memberNonVerification > keepVerificationOf) {
            log.info("CouponServiceImpl:===========[未核销数{}超过保留张数{}张]", memberNonVerification, keepVerificationOf);
            giftCouponInfo.setLimitPromptCode(5);
            return giftCouponInfo; //已领取
        }

        giftCouponInfo.setLimitPromptCode(6);
        return giftCouponInfo;
    }

    public List<GiftCoupon> getCouponListByMember(final MyCouponListQueryCondition condition) {
        List<GiftCoupon> list = giftCouponDao.getCouponListByMember(condition);

        return list;
    }

    @Override
    public GiftCoupon getCouponById(final Integer crlId) {

        GiftCoupon coupon = giftCouponDao.getCouponById(crlId);

        return coupon;
    }

    @Override
    @Transactional
    public int toVerify(final Integer memberId, final Integer crlId) throws MallDBException {
        //检查memberId是否为核销人员
        VerificationClerk vc = memberDao.getVerificationClerk(memberId);
        if (vc == null || vc.getStatus() != 0) return 0;

        GiftCoupon coupon = giftCouponDao.getCouponById(crlId);
        if (coupon == null || coupon.getCoupon_status() == 2) return 0;

        //检查是否在这张券的核销范围内
        int count = giftCouponDao.getVerificationWideByShop(vc, coupon.getCoupon_id());
        if (count <= 0) return 0;

        //检查券的有效期
        long today = dateTimeUtils.getMilliByToDay();
        if (today < coupon.getExpiry_date_start() || today > coupon.getExpiry_date_end()) return 0;

        //检查核销数量
        int totalVerify = coupon.getVerification_of();
        int total = giftCouponDao.getCouponCountByStatus(coupon.getCoupon_id(), 2);
        if(total >= totalVerify) return 0;

        int result = giftCouponDao.updateCouponStatus(2, crlId);

        if (result > 0) {
            result = giftCouponDao.saveVerification(vc, coupon);

            if (result <= 0) throw new MallDBException();
        }

        return result;
    }

    public String toJson(GiftCouponInfo giftCouponInfo) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(giftCouponInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public int saveCouponForMember(CouponReceiveCondition condition) {
        return giftCouponDao.saveGiftCouponByMember(condition);
    }

    @Override
    public List<GiftCouponInfo> getGiftCouponInfoListByShopId(CouponForShopQueryCondition condition) {
        return giftCouponDao.getCouponInfoListByShopId(condition);
    }

    /**
     * @param memberId
     * @param couponId
     * @return 0 已经终止发行
     * 1 库存不足
     * 2 当天已不能领取
     * 3 已领取券组中的其他券
     * 4 积分不足
     * 5 已领取
     * 6 条件满足可以领取
     * 7 领取失败(数据库操作失败，非逻辑错误)
     * 8 领取成功
     * @throws MallDBException
     */
    @Override
    @Transactional
    public int receiveCouponByMember(final int memberId, final int couponId) throws MallDBException {

        Member member = memberDao.getMemberById(memberId);
        if (member == null) {
            return 7;
        }

        GiftCouponInfo info = getGiftCouponInfoById(couponId, memberId);

        if (info.getLimitPromptCode() != 6) return info.getLimitPromptCode(); //领取之前判断能否领取

        CouponReceiveCondition condition = new CouponReceiveCondition();
        condition.setCouponStatus(1);
        condition.setCouponId(couponId);
        condition.setMemberId(memberId);
        condition.setMallId(info.getMall_id());
        int result = saveCouponForMember(condition);

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

}
