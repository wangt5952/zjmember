package com.laf.mall.api.service;

import com.laf.mall.api.dto.Coupon;
import com.laf.mall.api.dto.ReceiveCouponInfo;
import com.laf.mall.api.querycondition.coupon.CouponForShopQueryCondition;
import com.laf.mall.api.querycondition.coupon.CouponQueryCondition;
import com.laf.mall.api.querycondition.coupon.CouponReceiveCondition;
import com.laf.mall.api.querycondition.coupon.MyCouponListQueryCondition;

import java.util.List;

public interface CouponService {

    /**
     * 获取领取型优惠券列表
     *
     * @param condition
     * @return
     */
    List<ReceiveCouponInfo> getCouponInfoList(CouponQueryCondition condition);

    /**
     * 获取优惠券信息详情
     *
     * @param couponId
     * @param memberId
     * @return
     */
    ReceiveCouponInfo getCouponInfoById(final Integer couponId, final Integer memberId);

    /**
     * 获取我的券列表
     *
     * @param condition
     * @return
     */
    List<Coupon> getCouponListByMember(final MyCouponListQueryCondition condition);

    /**
     * 获取我的券详情
     * @param crlId
     * @return
     */
    Coupon getCouponById(final Integer crlId);

    /**
     * 券核销
     * @param memberId 核销人员的memberId
     * @param crlId
     * @return
     */
    int toVerify(final Integer memberId, final Integer crlId);

    /**
     * 获得注册送优惠券
     * @return
     */
    int getPointByRegister();

    /**
     * 获得注册送优惠券
     * @return
     */
    int getCouponByRegister();

    /**
     * 激活
     * @param memberId
     * @param crlId
     * @param mallId
     * @return
     */
    int toActivate(final Integer memberId, final Integer crlId, final Integer mallId);

    /**
     * 校验能否激活(在现场校验，核查工作人员)
     * @param memberId
     * @param crlId
     * @param mallId
     * @return
     */
    int verifyActiveIncludeClerk(final Integer memberId, final Integer crlId, final Integer mallId);

    /**
     * 校验激活(不包括核查工作人员)
     * @param crlId
     * @return
     */
    int verifyActiveExcludeClerk(Integer crlId);

    /**
     * 录入优惠券(领取型，激活型，关联型)
     * @param condition
     * @return
     */
    int saveCouponForMember(CouponReceiveCondition condition);

    /**
     * 获取关联性优惠券列表
     * @param mallId
     * @return
     */
    List<ReceiveCouponInfo> getRelateCouponInfoList(final int mallId);

    /**
     * 获取商户相关的优惠券列表
     * @param condition
     * @return
     */
    List<ReceiveCouponInfo> getCouponInfoListByShopId(CouponForShopQueryCondition condition);
}
