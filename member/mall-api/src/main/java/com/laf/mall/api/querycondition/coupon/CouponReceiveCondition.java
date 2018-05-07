package com.laf.mall.api.querycondition.coupon;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class CouponReceiveCondition {

    private int couponId;

    private int memberId;

    private int mallId;

    private int couponStatus;

    private int sources = -1;

    private int sourceId = 0;

    public long getCurrTime() {
        return new Date().getTime();
    }
}
