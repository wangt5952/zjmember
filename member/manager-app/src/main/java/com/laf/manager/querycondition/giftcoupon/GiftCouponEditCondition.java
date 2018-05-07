package com.laf.manager.querycondition.giftcoupon;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GiftCouponEditCondition {

    private Long issueStart;

    private Long issueEnd;

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

    public Integer getCouponId() {
        return couponId == null ? 0 : couponId;
    }

    public long getIssueStart() {
        return issueStart == null ? 315504000000L : issueStart;
    }

    public long getIssueEnd() {
        return issueEnd == null ? 1893427200000L : issueEnd;
    }

    public long getExpiryDateStart() {
        return expiryDateStart == null ? 0L : expiryDateStart;
    }

    public long getExpiryDateEnd() {
        return expiryDateEnd == null ? 0L : expiryDateEnd;
    }

    public long getRegTimeStart() {
        return regTimeStart == null ? 0L : regTimeStart;
    }

    public long getRegTimeEnd() {
        return regTimeEnd == null ? 0L : regTimeEnd;
    }

    public Integer getRequiredPoints() {
        return requiredPoints == null ? 0 : requiredPoints;
    }

    public BigDecimal getDiscountedPrice() {
        return discountedPrice == null ? new BigDecimal(0L) : discountedPrice;
    }

    public BigDecimal getPrice() {
        return price == null ? new BigDecimal(0L) : price;
    }

    public BigDecimal getCostPrice() {
        return costPrice == null ? new BigDecimal(0L) : costPrice;
    }

    public Integer getVerificationType() {
        return verificationType == null ? 0 : verificationType;
    }

    public Integer getCirculation() {
        return circulation == null ? 0 : circulation;
    }

    public Integer getDailyCirculation() {
        return dailyCirculation == null ? 0 : dailyCirculation;
    }

    public Integer getLimited() {
        return limited == null ? 0 : limited;
    }

    public Integer getDailyLimited() {
        return dailyLimited == null ? 0 : dailyLimited;
    }

    public Integer getVerificationOf() {
        return verificationOf == null ? 0 : verificationOf;
    }

    public Integer getKeepVerificationOf() {
        return keepVerificationOf == null ? 0 : keepVerificationOf;
    }

    public Integer getSortId() {
        return sortId == null ? 0 : sortId;
    }

    public Integer getReceiveMethod() {
        return requiredPoints == null || requiredPoints <= 0 ? 2 : 0;
    }
}
