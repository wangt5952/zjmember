package com.laf.manager.querycondition.coupon;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ReceiveCouponEditCondition {

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

    private Long activeTime;

    private Integer activable;

    private String activationCondition;

    private String activetionSite;

    private Integer couponType;

    private String qrCodeParam;

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

    public long getActiveTime() {
        return activeTime == null ? 0L : activeTime;
    }

    public Integer getReceiveMethod() {
        if (couponType == 0 || couponType == 1) {
            return requiredPoints == null || requiredPoints <= 0 ? 2 : 0;
        }
        return -1;
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

    public Integer getActivable() {
        return activable == null ? 0 : activable;
    }
}
