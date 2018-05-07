package com.laf.manager.enums;

public enum RewardCategory {

    REGISTER(8, "注册"),

    COMPLETE(9, "完善资料"),

    SIGN_IN(10, "活动签到");

    private final int value;

    private final String theName;

    RewardCategory(int value, String theName) {
        this.value = value;
        this.theName = theName;
    }

    public int value() {
        return this.value;
    }

    public String theName() {
        return this.theName;
    }

    public static RewardCategory valueOf(int value) {
        for (RewardCategory sources : values()) {
            if (sources.value == value) {
                return sources;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
