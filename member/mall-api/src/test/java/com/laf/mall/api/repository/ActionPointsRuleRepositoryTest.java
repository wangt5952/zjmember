package com.laf.mall.api.repository;

import com.laf.mall.api.dto.ActionPointsRule;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ActionPointsRuleRepositoryTest {

    @Autowired
    ActionPointsRuleRepository repository;

    @Test
    public void selectActionPointsByType() throws Exception {
        int i = repository.selectActionPointsByType(3, 10);
        Assert.assertNotEquals(0, i);
    }

    @Test
    public void selectActionPointsRuleByOption() throws Exception {
        ActionPointsRule rule = repository.selectActionPointsRuleByOption(2, 10);
        Assert.assertNotEquals(null, rule);
        log.info("points:{}", rule.getPoints());
        log.info("cunpon_id:{}", rule.getCoupon_id());
        log.info("options:{}", rule.getOptions());
    }

}