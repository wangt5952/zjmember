package com.laf.mall.api.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ActionPointsRule {

    private int apr_id;

    private String rule_name;

    private int points;

    private int coupon_id;

    private boolean on_off;

    private int options;

    private int mall_id;
}
