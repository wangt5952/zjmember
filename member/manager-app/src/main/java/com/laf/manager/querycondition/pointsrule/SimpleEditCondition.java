package com.laf.manager.querycondition.pointsrule;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SimpleEditCondition {
    private Integer ruleId;

    private String ruleName;

    private Integer levelId;

    private BigDecimal amount;

    private String indusRule;

    private String shopsRule;

    public Integer getRuleId() {
        return ruleId == null ? 0 : ruleId;
    }
}
