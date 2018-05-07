package com.laf.manager.service;

import com.laf.manager.dto.PointsPayCashRule;
import com.laf.manager.querycondition.pointsrule.PointsPayCashRuleEditCondition;

import java.util.List;

public interface PointsPayCashRuleService {

    List<PointsPayCashRule> getPointsPayCashRuleList();

    PointsPayCashRule getPointsPayCashRuleById(final Integer ppcrId);

    int editPointsPayCashRule(final PointsPayCashRuleEditCondition condition);

    int deletePointsPayCashRule(final Integer ppcrId);
}
