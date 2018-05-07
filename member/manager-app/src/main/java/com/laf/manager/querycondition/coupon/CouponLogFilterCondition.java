package com.laf.manager.querycondition.coupon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponLogFilterCondition {
    private String couponName = "";

    private String memberName;

    private String mobile;

    private long verificationDateStart;

    private long verificationDateEnd;

    private int couponType = -1;

    private long receiveDateStart;

    private long receiveDateEnd;

    private int couponStatus = -1;

    private String vcName;

    private int shopIdByVc;

    private Integer mallId = 0;

    private int size = 10;

    private int page = 1;

    private String order;

    public long getVerificationDateEnd() {
        return verificationDateEnd + getTime();
    }

    public long getReceiveDateEnd() {
        return receiveDateEnd + getTime();
    }

    public int getOffset() {
        return (page - 1) * size;
    }

    public String getOrder() {
        return StringUtils.isEmpty(this.order) ? "receive_date" : this.order;
    }

    private long getTime() {
        long time = (((23 * 60 + 59) * 60) + 59) * 1000;

        return time;
    }
}
