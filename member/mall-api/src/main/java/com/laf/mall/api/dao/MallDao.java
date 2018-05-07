package com.laf.mall.api.dao;

import com.laf.mall.api.dto.ActionPointsRule;
import com.laf.mall.api.dto.Mall;
import com.laf.mall.api.repository.ActionPointsRuleRepository;
import com.laf.mall.api.repository.MallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MallDao {

    @Autowired
    MallRepository repository;

    @Autowired
    ActionPointsRuleRepository actionPointsRuleRepository;

    public Mall getMallById(Integer mall_id) {
        return repository.selectMallById(mall_id);
    }

    public Mall getMallByAppId(String appId) {
        return repository.selectMallByAppId(appId);
    }

    public int getActionPointsByType(final Integer ruleType, final Integer mallId) {
        return actionPointsRuleRepository.selectActionPointsByType(ruleType, mallId);
    }

    public ActionPointsRule getActionPointsRuleByOption(final Integer ruleType, final Integer mallId) {
        return actionPointsRuleRepository.selectActionPointsRuleByOption(ruleType, mallId);
    }
}
