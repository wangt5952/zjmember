package com.laf.manager.repository;

import com.laf.manager.dto.ShopPointsAssociation;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SimplePointsRuleRepositoryTest {
    @Autowired
    SimplePointsRuleRepository repository;

    @Test
    public void insertShopPointsAssociations() throws Exception {
        List<ShopPointsAssociation> list = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            ShopPointsAssociation shopPointsAssociation = new ShopPointsAssociation();
            shopPointsAssociation.setAmount(new BigDecimal(1));
            shopPointsAssociation.setShop_id(i+1);
            shopPointsAssociation.setRule_id(1);
            shopPointsAssociation.setShop_name("abc");
            shopPointsAssociation.setRule_type(0);

            list.add(shopPointsAssociation);
        }

       // repository.insertShopPointsAssociations(list);
    }

}