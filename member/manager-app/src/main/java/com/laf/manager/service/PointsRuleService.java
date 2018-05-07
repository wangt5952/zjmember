package com.laf.manager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.laf.manager.dto.IndustryPointsAssociation;
import com.laf.manager.dto.PromotionPointsRule;
import com.laf.manager.dto.ShopPointsAssociation;
import com.laf.manager.dto.SimplePointsRule;
import com.laf.manager.querycondition.pointsrule.PromotionEditCondition;
import com.laf.manager.querycondition.pointsrule.SimpleEditCondition;

import java.util.List;

public interface PointsRuleService {

    int editSimpleRule(SimpleEditCondition condition);

    List<SimplePointsRule> getSimpleList();

    SimplePointsRule getSimpleRuleById(final Integer ruleId);

    List<IndustryPointsAssociation> getSimpleIndusRuleById(final Integer ruleId);

    List<ShopPointsAssociation> getSimpleShopsRuleById(final Integer ruleId);

    String getSimpleIndusRuleJsonById(final Integer ruleId) throws JsonProcessingException;

    String getSimpleShopsRuleJsonById(final Integer ruleId) throws JsonProcessingException;

    int editPromotionRule(final PromotionEditCondition condition);

    PromotionPointsRule getPromotionRuleById(final Integer ruleId);

    int calculateSimple(final Integer levelId);

    int calculatePromotion(final Integer ruleId);

    int deleteSimplePointsRule(Integer ruleId, Integer levelId);

    List<PromotionPointsRule> getPromotionList();
}
