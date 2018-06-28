package com.laf.mall.api.dao;

import com.laf.mall.api.dto.*;
import com.laf.mall.api.querycondition.coupon.CouponForShopQueryCondition;
import com.laf.mall.api.querycondition.coupon.CouponReceiveCondition;
import com.laf.mall.api.querycondition.coupon.MyCouponListQueryCondition;
import com.laf.mall.api.querycondition.giftcoupon.GiftCouponQueryCondition;
import com.laf.mall.api.repository.GiftCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GiftCouponDao {

    @Autowired
    private GiftCouponRepository repository;

    public List<GiftCouponInfo> getGiftCouponInfoList(GiftCouponQueryCondition condition, long registTime) {
        return repository.selectGiftCouponInfoList(condition, registTime);
    }

    public GiftCouponInfo getGiftCouponInfoById(final Integer couponId, final long today) {
        return repository.selectGiftCouponInfoById(couponId, today);
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

    public int saveGiftCouponByMember(final CouponReceiveCondition condition) {
        return repository.insertGiftCouponByMember(condition);
    }

    public List<GiftCoupon> getCouponListByMember(final MyCouponListQueryCondition condition) {
        return repository.selectCouponListByMember(condition);
    }

    public GiftCoupon getCouponById(final Integer crlId) {
        return repository.selectCouponById(crlId);
    }

    public int getCouponCountByStatus( final int couponId, final int couponStatus) {
        return repository.selectCouponCountByStatus(couponId, couponStatus);
    }

    public int getVerificationWideByShop(VerificationClerk vc, final int coupon_id) {
        return repository.selectVerificationWideByShop(vc, coupon_id);
    }

    public int updateCouponStatus(final int status, final int crlId) {
        return repository.updateCouponStatus(status, crlId);
    }

    public int saveVerification(VerificationClerk vc, GiftCoupon coupon) {
        return repository.insertVerification(vc, coupon);
    }

    public List<GiftCouponInfo> getCouponInfoListByShopId(CouponForShopQueryCondition condition) {
        return repository.selectCouponInfoListByShopId(condition);
    }
}
