package com.laf.manager.service.impl;

import com.laf.manager.SettingsProperties;
import com.laf.manager.dao.PointsPayCashRuleDao;
import com.laf.manager.dto.PointsPayCashRule;
import com.laf.manager.querycondition.pointsrule.PointsPayCashRuleEditCondition;
import com.laf.manager.service.PointsPayCashRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PointsPayCashRuleServiceImpl implements PointsPayCashRuleService {

    @Autowired
    PointsPayCashRuleDao ruleDao;

    @Autowired
    SettingsProperties settingsProperties;

    @Override
    public List<PointsPayCashRule> getPointsPayCashRuleList() {
        return ruleDao.getPointsPayCashRuleList(settingsProperties.getMallId());
    }

    @Override
    public PointsPayCashRule getPointsPayCashRuleById(Integer ppcrId) {
        return ruleDao.getPointsPayCashRuleById(ppcrId);
    }

    @Override
    public int editPointsPayCashRule(PointsPayCashRuleEditCondition condition) {
        PointsPayCashRule rule = null;
        if (condition.getPpcrId() != null && condition.getPpcrId() > 0) {
            rule = ruleDao.getPointsPayCashRuleById(condition.getPpcrId());
        }

        int result;
        if (rule == null) {
            rule = new PointsPayCashRule();
            rule.setMall_id(settingsProperties.getMallId());
            rule.setPoints(condition.getPoints());
//            rule.setType(PointsPayCashType.values()[condition.getTypeKey()]);
            rule.setType_name(condition.getTypeName());
            result = ruleDao.savePointsPayCashRule(rule);
        } else {
            rule.setPoints(condition.getPoints());
//            rule.setType(PointsPayCashType.values()[condition.getTypeKey()]);
            rule.setType_name(condition.getTypeName());
            result = ruleDao.updatePointsPayCashRule(rule);
        }

        return result;
    }

    @Override
    public int deletePointsPayCashRule(Integer ppcrId) {
        return ruleDao.deletePointsPayCashRule(ppcrId);
    }
}
