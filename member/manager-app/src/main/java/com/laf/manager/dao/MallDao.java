package com.laf.manager.dao;

import com.laf.manager.dto.ActionPointsRule;
import com.laf.manager.dto.Mall;
import com.laf.manager.repository.ActionPointsRuleRepository;
import com.laf.manager.repository.MallRepository;
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

    public int saveMall(final Mall mall) {
        return repository.insertMall(mall);
    }

    public int editMall(final Mall mall) {
        return repository.updateMall(mall);
    }
}
