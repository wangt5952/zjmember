package com.laf.mall.api.service.impl;

import com.jayway.jsonpath.JsonPath;
import com.laf.mall.api.dao.CouponDao;
import com.laf.mall.api.dao.MemberDao;
import com.laf.mall.api.dto.*;
import com.laf.mall.api.querycondition.coupon.CouponForShopQueryCondition;
import com.laf.mall.api.querycondition.coupon.CouponQueryCondition;
import com.laf.mall.api.querycondition.coupon.CouponReceiveCondition;
import com.laf.mall.api.querycondition.coupon.MyCouponListQueryCondition;
import com.laf.mall.api.service.CouponService;
import com.laf.mall.api.utils.datetime.DateTimeUtils;
import com.laf.manager.core.exception.MallDBException;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private DateTimeUtils dateTimeUtils;

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

        if (coupon != null && coupon.getCoupon_type() == 1) {
            coupon.setLimitPromptCode(verifyActiveExcludeClerk(crlId));
        }
        return coupon;
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

    @Override
    public int getCouponByRegister(Integer mallId) {
        String json = couponDao.getCouponRule(mallId);
        int result = 0;
        Map<String, String> map = null;

        if (!StringUtils.isEmpty(json)) {
            map = (Map<String, String>) JsonPath.parse(json).read("$", Map.class);

            if (map != null && map.size() > 0) {
                long nowTime = dateTimeUtils.getMilliWithoutTime(new Date().getTime());
                long activityTime = dateTimeUtils.getMilliWithoutTime(Long.valueOf(map.get("activity_date")).longValue());

                result = nowTime == activityTime ?
                        Integer.valueOf(map.get("activity_coupon_id")).intValue() :
                        Integer.valueOf(map.get("coupon_id")).intValue();
            }
        }

        /**
         * {"activity_date": 342423321312,
         * "activity_coupon_id": 1,
         * "activity_coupon_name": "50元",
         * "coupon_id": 2,
         * "coupon_name": "破朔料袋"}
         */

        return result;
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
     * 校验激活(在现场校验，核查工作人员)
     * @param memberId
     * @param crlId
     * @param mallId
     * @return 0 非相关工作人员
     *         1 重复激活
     *         2 不在有效期内
     *         3 未到有效激活时间
     *         4 已超出有效激活数量
     *         5 可激活
     */
    @Override
    public int verifyActiveIncludeClerk(Integer memberId, Integer crlId, Integer mallId) {

        VerificationClerk vc = memberDao.getClerk(memberId, 0, mallId);
        if (vc == null) return 0;

        return verifyActiveExcludeClerk(crlId);
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

    @Override
    public int saveCouponForMember(CouponReceiveCondition condition) {
        return couponDao.saveReceivedCouponByMember(condition);
    }

    @Override
    public List<ReceiveCouponInfo> getRelateCouponInfoList(final int mallId) {
        return couponDao.getRelateCouponInfoList(mallId);
    }

    @Override
    public List<ReceiveCouponInfo> getCouponInfoListByShopId(CouponForShopQueryCondition condition) {
        return couponDao.getCouponInfoListByShopId(condition);
    }
}
