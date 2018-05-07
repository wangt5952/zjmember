package com.laf.manager.enums;

public enum GiftCouponStatus {
    NONE(-1, "状态不限"),

    ACTIVATED(1, "未核销"),

    VERIFICATION(2, "已核销");

    private final int value;

    private final String theName;

    GiftCouponStatus(int value, String theName) {
        this.value = value;
        this.theName = theName;
    }

    public int value() {
        return this.value;
    }

    public String theName() {
        return this.theName;
    }

    public static GiftCouponStatus valueOf(int value) {
        for (GiftCouponStatus sources : values()) {
            if (sources.value == value) {
                return sources;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
