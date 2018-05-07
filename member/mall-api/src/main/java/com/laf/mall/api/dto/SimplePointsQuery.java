package com.laf.mall.api.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SimplePointsQuery {

    private int spq_id;

    private int level_id;

    private int shop_id;

    private BigDecimal amount;

    private int mall_id;
}
