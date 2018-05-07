package com.laf.manager.enums;

public enum MemberStatus {
    NONE(-1, "状态不限"),

    NORMAL(0, "正常"),

    LOCKED(1, "锁定"),

    BLACKLIST(2, "黑名单");

    private final int value;

    private final String theName;

    MemberStatus(int value, String theName) {
        this.value = value;
        this.theName = theName;
    }

    public int value() {
        return this.value;
    }

    public String theName() {
        return this.theName;
    }

    public static MemberStatus valueOf(int value) {
        for (MemberStatus status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
