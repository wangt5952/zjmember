package com.laf.mall.api.dao;

import com.laf.mall.api.dto.PromotionPointsQuery;
import com.laf.mall.api.dto.PromotionPointsRule;
import com.laf.mall.api.querycondition.points.PromotionQueryCondition;
import com.laf.mall.api.repository.PromotionPointsRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PromotionPointsRuleDao {

    @Autowired
    PromotionPointsRuleRepository repository;

    public int savePointsRule(PromotionPointsRule rule) {
        return repository.insertPromotionPointsRule(rule);
    }

    public int savePointsRuleWithoutBirthday(PromotionPointsRule rule) {
        return repository.insertPromotionPointsRuleWithoutBirthday(rule);
    }

    public PromotionPointsRule getRuleContentById(final Integer ruleId) {
        return repository.selectRuleContentById(ruleId);
    }

    public int saveDayPointsRule(final Integer ruleId, final Long day) {
        return repository.insertDayPointsRule(ruleId, day);
    }

    public int savePointsQuery(final PromotionPointsQuery promotionPointsQuery) {
        return repository.insertPointsQuery(promotionPointsQuery);
    }

    public BigDecimal getRule(PromotionQueryCondition condition) {
        return repository.selectPromotionPointsRule(condition);
    }

    public int deletePromotionRule(Integer ruleId) {
        int result = 0;

        result = repository.deletePromotionRule(ruleId);

        result = repository.deleteDayPointsRule(ruleId);

        result = repository.deletePromotionQuery(ruleId);

        return result;
    }
}
