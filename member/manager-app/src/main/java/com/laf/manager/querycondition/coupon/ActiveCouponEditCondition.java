package com.laf.manager.querycondition.coupon;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ActiveCouponEditCondition {

//    private Date issueStart;

//    private Date issueEnd;

    private Long expiryDateStart;

    private Long expiryDateEnd;

    private String couponName;

    private Integer couponId;

    private Integer verificationType;

    private String shops;

    private Integer circulation;

    private Integer dailyCirculation;

    private Integer limited;

    private Integer dailyLimited;

    private Integer verificationOf;

    private BigDecimal price;

    private BigDecimal costPrice;

    private BigDecimal discountedPrice;

    private Integer receiveMethod;

    private Integer keepVerificationOf;

    private String intro;

    private Integer introId;

    private Long regTimeStart;

    private Long regTimeEnd;

    private Integer sortId;

    private Integer requiredPoints;

    private String picture;

    private Long activeTime;

    private int activable;

    private String activationCondition;

    private String activetionSite;

    public Integer getCouponId() {
        return couponId == null ? 0 : couponId;
    }

    public Integer getCouponType() {
        return 0;
    }

    public Integer getReceiveMethod() {
//        return requiredPoints == null || requiredPoints <= 0 ? 2 : 0;
        return 2;
    }

    public Integer getRequiredPoints() {
        return requiredPoints == null ? 0 : requiredPoints;
    }

    public BigDecimal getDiscountedPrice() {
        return discountedPrice == null ? new BigDecimal(0L) : discountedPrice;
    }
}
