package com.laf.mall.api.dto;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Activate {

    private int active_id;

    private int coupon_id;

    private int member_id;

    private int vc_id;

    private Date active_time;

    private int mall_id;

    private Date receive_date;

    private int cri_id;

    public long getActive_time() {
        return active_time == null ? 0L : active_time.getTime();
    }

    public long getReceive_date() {
        return receive_date == null ? 0L : receive_date.getTime();
    }
}
