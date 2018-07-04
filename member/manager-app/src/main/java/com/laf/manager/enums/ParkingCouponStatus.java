package com.laf.manager.enums;

public enum ParkingCouponStatus {
    NONE(-1, "状态不限"),

    NOT_ACTIVATED(0, "未激活"),

    ACTIVATED(1, "未使用"),

    VERIFICATION(2, "已使用");

    private final int value;

    private final String theName;

    ParkingCouponStatus(int value, String theName) {
        this.value = value;
        this.theName = theName;
    }

    public int value() {
        return this.value;
    }

    public String theName() {
        return this.theName;
    }

    public static ParkingCouponStatus valueOf(int value) {
        for (ParkingCouponStatus sources : values()) {
            if (sources.value == value) {
                return sources;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
