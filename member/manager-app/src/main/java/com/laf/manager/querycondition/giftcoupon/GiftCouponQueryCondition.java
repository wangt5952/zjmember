package com.laf.manager.querycondition.giftcoupon;

import com.laf.manager.utils.datetime.DateTimeUtils;
import lombok.Data;

@Data
public class GiftCouponQueryCondition {

    private int mallId;

    private Integer page = 1;

    private Integer size = 10;

    public String getSort() {
        return "sort_id";
    }

    public String getDirection() {
        return "asc";
    }

    public int getOffset() {
        return (page - 1) * size;
    }

    public long getCurrDateStart() {
        DateTimeUtils dateTimeUtils = new DateTimeUtils();
        return dateTimeUtils.getMilliByToDay();
    }

    public long getCurrDateEnd() {
        DateTimeUtils dateTimeUtils = new DateTimeUtils();
        return dateTimeUtils.getMilliByTodayEnd();
    }

    public long getCurr() {
        DateTimeUtils dateTimeUtils = new DateTimeUtils();
        return dateTimeUtils.getMilliByToDay();
    }
}
