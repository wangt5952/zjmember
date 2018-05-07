package com.laf.manager.enums;

public enum AssessmentStatus {

    PROCESSED(0, "已处理"),
    UNPROCESSED(1, "未处理");

    private int value;

    private String theName;

    AssessmentStatus(int value, String name) {
        this.value = value;
        this.theName = name;
    }

    public int value() {
        return value;
    }

    public String theName() {
        return theName;
    }

    public static AssessmentStatus valueOf(int value) {
        AssessmentStatus[] statuses = values();

        for (AssessmentStatus status : statuses) {
            if (value == status.value()) {
                return status;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
