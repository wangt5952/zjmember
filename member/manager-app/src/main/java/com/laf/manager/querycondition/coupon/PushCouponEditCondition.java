package com.laf.manager.querycondition.coupon;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PushCouponEditCondition {

    private long expiryDateStart;

    private long expiryDateEnd;

    private String couponName;

    private Integer couponId;

    private Integer verificationType;

    private String shops;

    private BigDecimal price;

    private BigDecimal costPrice;

    private String intro;

    private Integer introId;

    private String picture;

    public Integer getCouponId() {
        return couponId == null ? 0 : couponId;
    }

    public Integer getCouponType() {
        return 2;
    }
}
