package com.laf.manager.querycondition.coupon;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CouponReceiveCondition {

    @NotNull(message = "优惠券Id必填")
    private Integer couponId;

    @NotNull(message = "会员Id必填")
    private Integer memberId;

    @NotNull(message = "商场Id")
    private Integer mallId;
}
