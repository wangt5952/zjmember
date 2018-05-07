package com.laf.manager.querycondition.coupon;

import lombok.Data;

@Data
public class LogQueryCondition {

    private int mallId;

//    private String keyWords;

    private Integer page = 1;

    private Integer size = 10;

    public String getSort() {
        return "verification_date";
    }

    public String getDirection() {
        return "desc";
    }

    public int getOffset() {
        return (page - 1) * size;
    }
}
