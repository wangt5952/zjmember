package com.laf.manager.querycondition.coupon;

import lombok.Data;

@Data
public class ReceivedQueryCondition {
//    private String keywords;

    private int mallId;

    private Integer couponStatus;

    private Integer page = 1;

    private Integer size = 10;

    public String getSort() {
        return "receive_date";
    }

    public String getDirection() {
        return "desc";
    }

    public int getOffset() {
        return (page - 1) * size;
    }
}
