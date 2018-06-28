package com.laf.mall.api.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ParkingPayInfo {

    private int pay_id;

    /**
     * 会员ID
     */
    private int member_id;

    /**
     * 车牌号
     */
    private String car_number;

    /**
     * 入场时间
     */
    private Date in_date;

    /**
     * 停车位置
     */
    private String parking_position;

    /**
     * 出场时间
     */
    private Date out_date;

    /**
     * 付款时间
     */
    private Date pay_date;

    /**
     * 付款金额（现金）
     */
    private BigDecimal price;

    /**
     * 停车券ID
     */
    private int crl_id;


}
