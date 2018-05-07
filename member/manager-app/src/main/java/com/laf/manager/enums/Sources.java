package com.laf.manager.enums;

public enum Sources {
    NONE(-1, "不限"),

    TICKET(0, "上传小票"),

    SCAN_CODE(1, "购阿购扫码"),

    CASH(2, "积分抵现"),

    FORWARD(3, "转发文章"),

    PERFECT(4, "完善资料"),

    ASSESSMENT(5, "评价建议"),

    RECEIVE_COUPON(6, "兑换优惠券"),

    SIGN_ON_ACTIVITIES(7, "参加活动"),

    REGISTER(8, "注册"),

    UPGRADE(9, "升级"),

    ENTRY(10, "手动录入"),

    MANUAL(11, "手动调整"),

    RAFFLE(12, "抽奖"),

    REWARD(13, "奖品");

    private final int value;

    private final String theName;

    Sources(int value, String theName) {
        this.value = value;
        this.theName = theName;
    }

    public int value() {
        return this.value;
    }

    public String theName() {
        return this.theName;
    }

    public static Sources valueOf(int value) {
        for (Sources sources : values()) {
            if (sources.value == value) {
                return sources;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
