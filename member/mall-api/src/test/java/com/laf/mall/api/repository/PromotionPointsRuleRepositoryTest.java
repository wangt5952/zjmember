package com.laf.mall.api.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.laf.mall.api.dto.PromotionPointsRule;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PromotionPointsRuleRepositoryTest {

    @Autowired
    PromotionPointsRuleRepository repository;

    @Test
    public void insertPromotionPointsRule() throws Exception {

        List<Map<String, Object>> industries = new ArrayList<>();
        Map<String, Object> industry1 = new HashMap<>();
        industry1.put("industry_id", 1);
        industry1.put("industry_name", "餐饮");
        Map<String, Object> industry2 = new HashMap<>();
        industry2.put("industry_id", 2);
        industry2.put("industry_name", "服饰");
        industries.add(industry1);
        industries.add(industry2);

        List<Map<String, Object>> shoppes = new ArrayList<>();
        Map<String, Object> shop1 = new HashMap<>();
        shop1.put("shop_id", "1");
        shop1.put("shop_name", "kfc");
        Map<String, Object> shop2 = new HashMap<>();
        shop2.put("shop_id", "2");
        shop2.put("shop_name", "m");
        shoppes.add(shop1);
        shoppes.add(shop2);

        Map<Object, Object> objects = new HashMap<>();
        objects.put("industries", industries);
        objects.put("shoppes", shoppes);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(objects);
        log.info(jsonInString);

        PromotionPointsRule rule = new PromotionPointsRule();
        rule.setRule_content(jsonInString);
        rule.setRule_name("rule1");
        rule.setMall_id(1);
        rule.setStart_date(new Date());
        rule.setEnd_date(new Date());
        rule.setAmount(new BigDecimal(99));

        repository.insertPromotionPointsRuleWithoutBirthday(rule);
    }

    @Test
    public void insertPromotionPointsRuleWithoutBirthday() throws Exception {

    }

    @Test
    public void selectRuleContentById() throws Exception {
    }

}