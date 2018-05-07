package com.laf.mall.api.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PromotionPointsQuery {

    private int ppq_id;

    private int rule_id;

    private int level_id;

    private int shop_id;

    private long birthday_start;

    private long birthday_end;

    private BigDecimal amount;

}
