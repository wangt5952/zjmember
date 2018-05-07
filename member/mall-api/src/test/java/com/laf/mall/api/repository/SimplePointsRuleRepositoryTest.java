package com.laf.mall.api.repository;

import com.laf.mall.api.dto.*;
import com.laf.manager.core.support.tuple.Tuple2;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SimplePointsRuleRepositoryTest {

    @Autowired
    SimplePointsRuleRepository repository;

    @Autowired
    ShopRepository shopRepository;

    @Autowired
    IndustryRepository industryRepository;

    @Test
    public void insertPointsRule() throws Exception {
        SimplePointsRule simplePointsRule = new SimplePointsRule();
        simplePointsRule.setLevel_id(2);
        simplePointsRule.setMall_id(1);
        simplePointsRule.setAmount(new BigDecimal(80));

        int result = repository.insertPointsRule(simplePointsRule);
        assertTrue(result > 0);
    }

    @Test
    public void insertShopPointsAssociation() throws Exception {
        ShopPointsAssociation shopPointsAssociation = new ShopPointsAssociation();
        shopPointsAssociation.setRule_id(1);
        shopPointsAssociation.setRule_type(0);
        shopPointsAssociation.setShop_id(20);
        shopPointsAssociation.setAmount(new BigDecimal(75));

        int result = repository.insertShopPointsAssociation(shopPointsAssociation);

        assertTrue(result > 0);
    }

    @Test
    public void insertIndustryPointsAssociation() throws Exception {
        IndustryPointsAssociation industryPointsAssociation = new IndustryPointsAssociation();
        industryPointsAssociation.setRule_id(1);
        industryPointsAssociation.setRule_type(0);
        industryPointsAssociation.setIndustry_id(3);
        industryPointsAssociation.setAmount(new BigDecimal(88));

        int result = repository.insertIndustryPointsAssociation(industryPointsAssociation);

        assertTrue(result > 0);
    }

    @Test
    public void insertPointsQuery() throws Exception {
        List<SimplePointsRule> ruleList = repository.selectAllSimpleRule(1);
        List<Shop> shopList = shopRepository.selectAllShopsByMall(1);

        int result = 0;
        int count = 0;

        for (SimplePointsRule rule : ruleList) {
            for (Shop shop : shopList) {
                result = repository.insertPointsQuery(rule, shop.getShop_id());
                ++count;
            }
        }

//        assertTrue(count == 42);

        List<SimplePointsQuery> industryList = repository.selectSimpleAmountListByIndustry(1);

        for (SimplePointsQuery simplePointsQuery : industryList) {
            repository.updatePointsQuery(simplePointsQuery);
        }

        List<SimplePointsQuery> shopPointsList = repository.selectSimpleAmountListByShopInMall(1);

        for (SimplePointsQuery simplePointsQuery : shopPointsList) {
            repository.updatePointsQuery(simplePointsQuery);
        }
    }


    @Test
    public void insertPointsQueryByIndustries() throws Exception {

    }

    @Test
    public void selectAllSimpleRule() throws Exception {
        List<SimplePointsRule> list = repository.selectAllSimpleRule(1);

        assertTrue(!list.isEmpty());
    }

    @Test
    public void selectSimpleAmountByIndustry() throws Exception {
        List<SimplePointsQuery> industryList = repository.selectSimpleAmountListByIndustry(1);

        assertTrue(!industryList.isEmpty());
    }

    @Test
    public void selectSimpleAmountListByShopInMall() throws Exception {
        List<SimplePointsQuery> list = repository.selectSimpleAmountListByShopInMall(1);

        assertTrue(!list.isEmpty());
    }

}