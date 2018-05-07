package com.laf.manager.service.impl;

import com.laf.manager.service.PointsRuleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PointsRuleServiceImplTest {
    @Autowired
    PointsRuleService pointsRuleService;

    @Test
    public void getSimpleIndusRuleJsonById() throws Exception {
        String json = pointsRuleService.getSimpleIndusRuleJsonById(6);
        log.info(json);
    }

    @Test
    public void getSimpleShopsRuleJsonById() throws Exception {
        String json = pointsRuleService.getSimpleShopsRuleJsonById(1);
        log.info(json);
    }
}