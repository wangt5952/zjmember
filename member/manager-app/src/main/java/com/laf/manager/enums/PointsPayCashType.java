package com.laf.manager.enums;

public enum PointsPayCashType {

    Shop(0, "商户"),

    Mall(1, "商场"),

    ParkingLot(2, "停车场");

    private int key;

    private String value;

    PointsPayCashType(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

}
