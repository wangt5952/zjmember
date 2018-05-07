/******************************************************************************
 *
 * 商铺与积分规则的关联实体
 *
 ******************************************************************************/

package com.laf.mall.api.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ShopPointsAssociation {

    private int sp_id;

    private int shop_id;

    private BigDecimal amount;

    private int rule_id;

    private int rule_type;
}
