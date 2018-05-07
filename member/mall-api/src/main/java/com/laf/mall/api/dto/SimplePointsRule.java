package com.laf.mall.api.dto;

import lombok.*;
import org.apache.commons.collections.ListUtils;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SimplePointsRule {

    private int spr_id;

    private int level_id;

    private BigDecimal amount;

    private int mall_id;

//    private List<ShopPointsAssociation> shopPointsAssociationList = ListUtils.EMPTY_LIST;
//
//    private List<IndustryPointsAssociation> industryPointsAssociationList = ListUtils.EMPTY_LIST;
}


