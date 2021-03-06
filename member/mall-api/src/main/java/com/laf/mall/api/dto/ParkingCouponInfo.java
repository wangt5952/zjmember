package com.laf.mall.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ParkingCouponInfo extends Coupon{
    private String car_number;
    private Date use_date;
    private BigDecimal price;
    private Date in_date;
    private Date out_date;
    private String ticket_no;

    public long getUse_date() {
        return use_date == null ? 0L : use_date.getTime();
    }

    public long getIn_date() {
        return in_date == null ? 0L : in_date.getTime();
    }

    public long getOut_date() {
        return out_date == null ? 0L : out_date.getTime();
    }
}
