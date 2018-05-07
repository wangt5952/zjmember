package com.laf.mall.api.dao;

import com.laf.mall.api.dto.SimplePointsQuery;
import com.laf.mall.api.dto.SimplePointsRule;
import com.laf.mall.api.repository.SimplePointsRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class SimplePointsRuleDao {

    @Autowired
    SimplePointsRuleRepository repository;

    public List<SimplePointsRule> getAllSimpleRule(final Integer mallId) {
        return repository.selectAllSimpleRule(mallId);
    }

    public int savePointsQuery(final SimplePointsQuery simplePointsQuery) {
        return repository.insertPointsQuery(simplePointsQuery);
    }

    public int editPointsQuery(final SimplePointsQuery simplePointsQuery) {
        return repository.updatePointsQuery(simplePointsQuery);
    }

    public List<SimplePointsQuery> getSimpleAmountListByIndustry(final Integer mallId) {
        return repository.selectSimpleAmountListByIndustry(mallId);
    }

    public List<SimplePointsQuery> getSimpleAmountListByShopInMall(final Integer mallId) {
        return repository.selectSimpleAmountListByShopInMall(mallId);
    }

    public BigDecimal getSimpleAmount(final Integer levelId, final Integer shopId) {
        return repository.selectSimpleAmount(levelId, shopId);
    }

    public int deleteSimpleRule(Integer ruleId) {
        return repository.deleteSimpleRule(ruleId);
    }

    public int deleteSimpleQuery(Integer levelId) {
        return repository.deleteSimpleQuery(levelId);
    }

    public int deleteSimpleShopPoints(Integer ruleId) {
        return repository.deleteShopPoints(ruleId);
    }

    public int deleteSimpleIndustryPoints(Integer ruleId) {
        return repository.deleteIndustryPoints(ruleId);
    }

    public SimplePointsRule getSimpleRuleById(final Integer ruleId) {
        return repository.selectSimpleRule(ruleId);
    }
}
