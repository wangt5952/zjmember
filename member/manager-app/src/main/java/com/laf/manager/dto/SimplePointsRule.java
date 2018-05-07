package com.laf.manager.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SimplePointsRule {

    private int spr_id;

    private int level_id;

    private String level_name;

    private BigDecimal amount;

    private int mall_id;

    private String rule_name;

//    private List<ShopPointsAssociation> shopPointsAssociationList = ListUtils.EMPTY_LIST;
//
//    private List<IndustryPointsAssociation> industryPointsAssociationList = ListUtils.EMPTY_LIST;
}


