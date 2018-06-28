package com.laf.manager.dao;

import com.laf.manager.dto.PromotionPointsQuery;
import com.laf.manager.dto.PromotionPointsRule;
import com.laf.manager.querycondition.points.PromotionQueryCondition;
import com.laf.manager.repository.PromotionPointsRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public PromotionPointsRule getRule(PromotionQueryCondition condition) {
        return repository.selectPromotionPointsRule(condition);
    }

    public int deletePromotionRule(Integer ruleId) {
        int result = 0;

        result = repository.deletePromotionRule(ruleId);

        if (result > 0) result = repository.deleteDayPointsRule(ruleId);

        if (result > 0) repository.deletePromotionQuery(ruleId);

        return result;
    }

    public int removeDayPointsRule(Integer ruleId) {
        return repository.deleteDayPointsRule(ruleId);
    }

    public int removePromotionQuery(Integer ruleId) {
        return repository.deletePromotionQuery(ruleId);
    }

    public int editPointsRule(PromotionPointsRule rule) {
        return repository.updatePromotionPointsRule(rule);
    }

    public int editPointsRuleWithoutBirthday(PromotionPointsRule rule) {
        return repository.updatePromotionPointsRuleWithoutBirthday(rule);
    }

    public List<PromotionPointsRule> getPromotionPointsRuleList(final int mallId) {
        return repository.selectPromotionPointsRuleList(mallId);
    }
}
