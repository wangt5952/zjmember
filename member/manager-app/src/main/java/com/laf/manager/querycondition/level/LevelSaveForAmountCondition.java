package com.laf.manager.querycondition.level;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LevelSaveForAmountCondition {

    private Integer levelId;

    private String levelName;

    private Integer mallId;

    private BigDecimal amountLow;

    private BigDecimal amountHigh;

    private Integer algorithm;
}
