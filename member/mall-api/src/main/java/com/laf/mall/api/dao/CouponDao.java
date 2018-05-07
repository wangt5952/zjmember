package com.laf.mall.api.dao;

import com.laf.mall.api.dto.Activate;
import com.laf.mall.api.dto.Coupon;
import com.laf.mall.api.dto.ReceiveCouponInfo;
import com.laf.mall.api.dto.VerificationClerk;
import com.laf.mall.api.querycondition.coupon.CouponForShopQueryCondition;
import com.laf.mall.api.querycondition.coupon.CouponQueryCondition;
import com.laf.mall.api.querycondition.coupon.CouponReceiveCondition;
import com.laf.mall.api.querycondition.coupon.MyCouponListQueryCondition;
import com.laf.mall.api.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CouponDao {

    @Autowired
    private CouponRepository repository;

    public List<ReceiveCouponInfo> getCouponInfoList(CouponQueryCondition condition, long registTime) {
        return repository.selectCouponInfoList(condition, registTime);
    }

    public ReceiveCouponInfo getCouponInfoById(final Integer couponId, final long today) {
        return repository.selectCouponInfoById(couponId, today);
    }

    public int getReceivedCountInCouponGroup(final Integer couponId, final Integer memberId, final Integer groupId) {
        return repository.selectReceivedCountInCouponGroup(couponId, memberId, groupId);
    }

    public int getReceivedByMember(final Integer memberId, final Integer couponId) {
        return repository.selectReceivedByMember(memberId, couponId);
    }

    public int getDailyReceivedByMember(final Integer memberId, final Integer couponId, final long currTime) {
        return repository.selectDailyReceivedByMember(memberId, couponId, currTime);
    }

    public int getNonVerificationCouponByMember(final Integer memberId, final Integer couponId) {
        return repository.selectNonVerificationCouponByMember(memberId, couponId);
    }

    public int saveReceivedCouponByMember(final CouponReceiveCondition condition) {
        return repository.insertReceivedCouponByMember(condition);
    }

    public List<Coupon> getCouponListByMember(final MyCouponListQueryCondition condition) {
        return repository.selectCouponListByMember(condition);
    }

    public Coupon getCouponById(final Integer crlId) {
        return repository.selectCouponById(crlId);
    }

    public int getVerificationWideByShop(VerificationClerk vc, final int coupon_id) {
        return repository.selectVerificationWideByShop(vc, coupon_id);
    }

    public int updateCouponStatus(final int status, final int crlId) {
        return repository.updateCouponStatus(status, crlId);
    }

    public int saveVerification(VerificationClerk vc, Coupon coupon) {
        return repository.insertVerification(vc, coupon);
    }

    public String getCouponRule(int mallId) {
        return repository.selectCouponRule(mallId);
    }

    public int getCouponCountByStatus(final int memberId, final int couponId, final int couponStatus) {
        return repository.selectCouponCountByStatus(memberId, couponId, couponStatus);
    }

    public int saveCouponActiveLog(Activate activate) {
        return repository.insertCouponActiveLog(activate);
    }

    public List<ReceiveCouponInfo> getRelateCouponInfoList(final int mallId) {
        return repository.selectRelateCouponInfoList(mallId);
    }

    public List<ReceiveCouponInfo> getCouponInfoListByShopId(CouponForShopQueryCondition condition) {
        return repository.selectCouponInfoListByShopId(condition);
    }
}
