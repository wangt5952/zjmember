package com.laf.manager.service;

import com.laf.manager.dto.Coupon;
import com.laf.manager.dto.ReceiveCouponInfo;
import com.laf.manager.querycondition.coupon.CouponLogFilterCondition;
import com.laf.manager.querycondition.giftcoupon.GiftCouponEditCondition;
import com.laf.manager.querycondition.giftcoupon.GiftCouponQueryCondition;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;

public interface GiftCouponService {

    int editGiftCouponInfo(final GiftCouponEditCondition condition);

    List<ReceiveCouponInfo> getGiftCouponInfoList(GiftCouponQueryCondition condition);

    ReceiveCouponInfo getGiftCouponInfoById(final Integer couponId);

    int getGiftCouponsCount(final GiftCouponQueryCondition condition);

    List<Coupon> getCouponLogsList(CouponLogFilterCondition condition);

    int getCouponLogsCount(CouponLogFilterCondition condition);

    BigDecimal getCouponLogsSum(CouponLogFilterCondition condition);

    void print2Excel(List<Coupon> members, OutputStream out);

    List<ReceiveCouponInfo> getGiftCouponInfoListForRaffle(GiftCouponQueryCondition condition);

    String associationCoupons2Json(List<ReceiveCouponInfo> list);
}
