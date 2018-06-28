package com.laf.mall.api.service;

import com.laf.mall.api.dto.Coupon;
import com.laf.mall.api.dto.Parking;
import com.laf.mall.api.dto.ParkingInfo;
import com.laf.mall.api.dto.ReceiveCouponInfo;
import com.laf.mall.api.querycondition.coupon.CouponQueryCondition;
import com.laf.mall.api.querycondition.coupon.MyCouponListQueryCondition;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ParkingCouponService {

    List<Parking> getParkingList(int member_id);

    int saveOrUpdateParking(Parking parking);

    /**
     * 获取停车券列表
     *
     * @param condition
     * @return
     */
    List<ReceiveCouponInfo> getCouponInfoList(CouponQueryCondition condition);

    /**
     * 获取停车券信息详情
     *
     * @param couponId
     * @param memberId
     * @return
     */
    ReceiveCouponInfo getCouponInfoById(final Integer couponId, final Integer memberId);

    /**
     * 获取我的停车券列表
     *
     * @param condition
     * @return
     */
    List<Coupon> getCouponListByMember(final MyCouponListQueryCondition condition);

    /**
     * 获取我的停车券详情
     * @param crlId
     * @return
     */
    Coupon getCouponById(final Integer crlId);

    /**
     *
     * @param memberId
     * @param couponId
     * @return
     */
    int receiveCouponByMember(final int memberId, final int couponId);

    /**
     * 券核销
     * @param memberId 核销人员的memberId
     * @param crlId
     * @return
     */
    int toVerify(final Integer memberId, final Integer crlId);


    /**
     * 激活
     * @param memberId
     * @param crlId
     * @param mallId
     * @return
     */
    int toActivate(final Integer memberId, final Integer crlId, final Integer mallId);

    ParkingInfo getParkingInfo();

    int updateCarInfo( Integer memberId, String carNumber);

    int parkingPay(Integer memberId, String carNumber, String ticketNo, Coupon coupon, BigDecimal price, Date in_date);
}
