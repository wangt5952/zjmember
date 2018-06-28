package com.laf.manager.service;

import com.laf.manager.dto.*;
import com.laf.manager.enums.CouponType;
import com.laf.manager.querycondition.coupon.*;
import com.laf.manager.querycondition.member.MemberFilterCondition;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;

public interface CouponService {


    int editCoupon(final ReceiveCouponEditCondition condition);

    int editParkingCoupon(ReceiveCouponEditCondition condition);

    ReceiveCouponInfo getReceiveCoupon(final Integer couponId);

    ReceiveCouponInfo getParkingCoupon(final Integer couponId);

    int delReceiveCoupon(final Integer couponId);

    int delCoupon(final Integer couponId);

    int getCouponsCount(final CouponQueryCondition condition);

    int getParkingCouponsCount(final CouponQueryCondition condition);

    /**
     * 获取领取记录
     * @param condition
     * @return
     */
    List<Coupon> getReceivedLogList(CouponLogFilterCondition condition);

    int getReceivedLogCount(CouponLogFilterCondition condition);

    List<Coupon> getVerificationLogList(CouponLogFilterCondition condition);

    int getVerificationLogCount(CouponLogFilterCondition condition);

    List<Coupon> getCouponLogsList(CouponLogFilterCondition condition);

    List<ParkingCouponInfo> getParkingCouponLogsList(CouponLogFilterCondition condition);

    int getParkingCouponPayLogsCount(CouponLogFilterCondition condition);

    List<ParkingCouponInfo> getParkingCouponPayLogsList(CouponLogFilterCondition condition);

    int getCouponLogsCount(CouponLogFilterCondition condition);

    int getParkingCouponLogsCount(CouponLogFilterCondition condition);

    BigDecimal getCouponLogsSum(CouponLogFilterCondition condition);

    BigDecimal getParkingCouponLogsSum(CouponLogFilterCondition condition);

    int editPushCoupon(final PushCouponEditCondition condition);

    List<ReceiveCouponInfo> getCouponInfoList(CouponQueryCondition condition);

    List<ReceiveCouponInfo> getParkingCouponInfoList(CouponQueryCondition condition);

    List<ReceiveCouponInfo> getCouponInfoListFromType(CouponType couponType);

    int pushCoupons(MemberFilterCondition condition, final String ids);

    List<ReceiveCouponInfo> getCouponInfoListByType(CouponQueryCondition condition);

    List<Integer> getCouponCountMonthly(String year);

    List<Integer> getVerifCountMonthly(String year);

    String associationCoupons2Json(List<ReceiveCouponInfo> list);

    void print2Excel(List<Coupon> members, OutputStream out);

    void print2ExcelParking(List<ParkingCouponInfo> couponLogs, OutputStream out);

    int saveParkingInfo(String intro);

    ParkingInfo getParkingInfo();
}
