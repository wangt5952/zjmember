package com.laf.manager.dao;

import com.laf.manager.dto.IndustryPointsAssociation;
import com.laf.manager.dto.ShopPointsAssociation;
import com.laf.manager.dto.SimplePointsQuery;
import com.laf.manager.dto.SimplePointsRule;
import com.laf.manager.repository.SimplePointsRuleRepository;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class SimplePointsRuleDao {

    @Autowired
    SimplePointsRuleRepository repository;

    public int saveRule(SimplePointsRule rule) {
        return repository.insertPointsRule(rule);
    }

    public int saveShopsRule(ShopPointsAssociation shopPointsAssociation) {
        return repository.insertShopPointsAssociation(shopPointsAssociation);
    }

    public int editRule(SimplePointsRule rule) {
        return repository.updatePointsRule(rule);
    }

    public int saveIndusRule(IndustryPointsAssociation industryPointsAssociation) {
        return repository.insertIndustryPointsAssociation(industryPointsAssociation);
    }

    public List<SimplePointsRule> getAllSimpleRule(final Integer mallId) {
        return repository.selectAllSimpleRule(mallId);
    }

    public int savePointsQuery(final SimplePointsQuery simplePointsQuery) {
        return repository.insertPointsQuery(simplePointsQuery);
    }

    public int savePointsQueries(final List<SimplePointsQuery> simplePointsQueries) {
        return repository.insertPointsQueries(simplePointsQueries);
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

    public List<ShopPointsAssociation> getSimpleShopsRuleListById(final Integer ruleId) {
        return repository.selectSimpleShopsRuleListById(ruleId);
    }

    public List<IndustryPointsAssociation> getSimpleIndusRuleListById(final Integer ruleId) {
        return repository.selectSimpleIndusRuleListById(ruleId);
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

    public int getSimpleRuleCount(final Integer ruleId) {
        return repository.selectSimpleRuleCount(ruleId);
    }

    public int getShopPointsCount(final Integer ruleId) {
        return repository.selectShopPointsCount(ruleId);
    }

    public int getIndustryPointsCount(final Integer ruleId) {
        return repository.selectIndustryPointsCount(ruleId);
    }

    public int getSimpleQueryCount(final Integer levelId) {
        return repository.selectSimpleQueryCount(levelId);
    }

    public SimplePointsRule getSimpleRuleById(final Integer ruleId) {
        return repository.selectSimpleRule(ruleId);
    }

    public int saveShopPointsAssociations(final List<ShopPointsAssociation> shopPointsAssociations) {
        return repository.insertShopPointsAssociations(shopPointsAssociations);
    }
}
