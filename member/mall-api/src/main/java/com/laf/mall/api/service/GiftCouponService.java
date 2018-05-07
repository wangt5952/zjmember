package com.laf.mall.api.service;

import com.laf.mall.api.dto.GiftCoupon;
import com.laf.mall.api.dto.GiftCouponInfo;
import com.laf.mall.api.querycondition.coupon.CouponForShopQueryCondition;
import com.laf.mall.api.querycondition.coupon.CouponReceiveCondition;
import com.laf.mall.api.querycondition.coupon.MyCouponListQueryCondition;
import com.laf.mall.api.querycondition.giftcoupon.GiftCouponQueryCondition;

import java.util.List;

public interface GiftCouponService {

    /**
     * 获取礼品券列表
     *
     * @param condition
     * @return
     */
    List<GiftCouponInfo> getGiftCouponInfoList(final GiftCouponQueryCondition condition);

    /**
     * 获取礼品券信息详情
     *
     * @param couponId
     * @param memberId
     * @return
     */
    GiftCouponInfo getGiftCouponInfoById(final Integer couponId,  final Integer memberId);

    /**
     * 获取我的券列表
     *
     * @param condition
     * @return
     */
    List<GiftCoupon> getCouponListByMember(final MyCouponListQueryCondition condition);

    /**
     * 获取我的券详情
     * @param crlId
     * @return
     */
    GiftCoupon getCouponById(final Integer crlId);

    /**
     * 券核销
     * @param memberId 核销人员的memberId
     * @param crlId
     * @return
     */
    int toVerify(final Integer memberId, final Integer crlId);

    /**
     * 录入礼品券
     * @param condition
     * @return
     */
    int saveCouponForMember(CouponReceiveCondition condition);

    /**
     * 获取商户相关的礼品券列表
     * @param condition
     * @return
     */
    List<GiftCouponInfo> getGiftCouponInfoListByShopId(CouponForShopQueryCondition condition);

    /**
     * 会员领取礼品券功能(积分兑换，免费领取)
     *
     * @param memberId
     * @param couponId
     *
     * @return
     */
    int receiveCouponByMember(final int memberId, final int couponId);

}
