package com.laf.manager.dao;

import com.laf.manager.dto.PointsPayCashRule;
import com.laf.manager.repository.PointsPayCashRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PointsPayCashRuleDao {

    @Autowired
    PointsPayCashRuleRepository repository;

    public List<PointsPayCashRule> getPointsPayCashRuleList(final Integer mallId) {
        return repository.selectPointsPayCashRuleList(mallId);
    }

    public PointsPayCashRule getPointsPayCashRuleById(final Integer ppcrId) {
        return repository.selectPointsPayCashRuleById(ppcrId);
    }

    public int savePointsPayCashRule(PointsPayCashRule rule) {
        return repository.insertPointsPayCashRule(rule);
    }

    public int updatePointsPayCashRule(PointsPayCashRule rule) {
        return repository.updatePointsPayCashRule(rule);
    }

    public int deletePointsPayCashRule(final Integer ppcrId) {
        return repository.deletePointsPayCashRule(ppcrId);
    }
}
