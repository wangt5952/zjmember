package com.laf.mall.api.dao;

import com.laf.mall.api.repository.PointsPayCashRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PointsPayCashRuleDao {

    @Autowired
    PointsPayCashRepository repository;

    public int getPointsPayCashRuleByType(final int type, final int mallId) {
        return repository.selectPointsPayCashRuleByType(type, mallId);
    }
}
