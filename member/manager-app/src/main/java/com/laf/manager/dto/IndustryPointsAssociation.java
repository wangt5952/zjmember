
/******************************************************************************
 *
 * 业态与积分规则的关联实体
 *
 ******************************************************************************/

package com.laf.manager.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class IndustryPointsAssociation {

    private int ip_id;

    private int industry_id;

    private String industry_name;

    private BigDecimal amount;

    private int rule_id;

    private int rule_type;
}
