package com.laf.manager.enums;

public enum Occupation {
    NONE(-1, "无"),

    TEACHER(0, "教师"),

    ACCOUNTANT(1, "会计"),

    IT(2, "IT"),

    FINANCE(3, "金融"),

    SALESMAN(4, "销售"),

    SALESPERSON(5, "营业员"),

    OFFICEHOLDER(6, "公务员"),

    FREELANCER(7, "自由职业");

    private final int value;

    private final String occuName;

    Occupation(int value, String occuName) {
        this.value = value;
        this.occuName = occuName;
    }

    public int value() {
        return this.value;
    }

    public String occuName() {
        return this.occuName;
    }

    public static Occupation valueOf(int value) {
        for (Occupation occu : values()) {
            if (occu.value == value) {
                return occu;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
