package com.laf.manager.dto;

import com.laf.manager.enums.PointsPayCashType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PointsPayCashRule {

    /**
     * 主键
     */
    private int ppcr_id;

    /**
     * 抵现规则类型
     */
    private PointsPayCashType type;

    /**
     * 抵现规则类型名称
     */
    private String type_name;

    /**
     * 抵现积分
     */
    private int points;

    /**
     * 相关商城
     */
    private int mall_id;
}

