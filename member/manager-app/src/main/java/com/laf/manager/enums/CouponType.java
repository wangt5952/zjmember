package com.laf.manager.enums;

public enum CouponType {
    NONE(-1, "类型不限"),

    RECEIVE(0, "领取型"),

    ACTIVITY(1, "激活型"),

    PUSH(2, "推送型"),

    ASSOCIATION(3, "关联型");

    private final int value;

    private final String theName;

    CouponType(int value, String theName) {
        this.value = value;
        this.theName = theName;
    }

    public int value() {
        return this.value;
    }

    public String theName() {
        return this.theName;
    }

    public static CouponType valueOf(int value) {
        for (CouponType sources : values()) {
            if (sources.value == value) {
                return sources;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
