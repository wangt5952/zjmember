package com.laf.mall.api.menu;

public enum CouponStatus {

    ENABLED(0, "满足领取条件"),

    ISSUEEND(1, "优惠券已终止发行"),

    UNDERSTOCK(2, "库存不足"),

    COUPONGROUP(3, "已领取券组中的其他券"),

    POINTSLACK(4, "积分不足"),

    RECEIVED(5, "已领取");





    private final int value;

    private final String content;

    CouponStatus(int value, String content) {
        this.value = value;
        this.content = content;
    }
}