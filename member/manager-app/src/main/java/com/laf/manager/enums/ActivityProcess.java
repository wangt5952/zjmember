package com.laf.manager.enums;

public enum ActivityProcess {

    NONE(-1, "不限"),

    NOTSTARTED(0, "未开始"),

    UNDERWAY(1, "进行中"),

    OVER(2, "已结束");

    private int value;

    private String theName;

    ActivityProcess(int value, String name) {
        this.value = value;
        this.theName = name;
    }

    public int theValue() {
        return value;
    }

    public String theName() {
        return theName;
    }

}
