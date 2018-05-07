package com.laf.manager.service.impl;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ShopServiceImplTest {

    @Autowired
    ShopServiceImpl shopService;

    @Test
    public void getShopList() throws Exception {
    }

    @Test
    public void getShopById() throws Exception {
    }

    @Test
    public void editShop() throws Exception {
//        for (int i = 0; i < 20; i++) {
//            Shop shop = new Shop();
//            if (i <= 4) {
//                shop.setShop_name("餐饮" + (i + 1));
//                shop.setIndustry_name("餐饮");
//                shop.setIndustry(1);
//            } else if (i >= 4 && i <= 12) {
//                shop.setShop_name("娱乐" + (i + 1));
//                shop.setIndustry_name("娱乐");
//                shop.setIndustry(2);
//            } else {
//                shop.setShop_name("服装" + (i + 1));
//                shop.setIndustry_name("服装");
//                shop.setIndustry(3);
//            }
//            shop.setPhone("025-12345678");
//            shop.setMobile("1333333333");
//            shop.setLinkman("店主" + (i + 1));
//            shop.setPictures("http://localhost/xxx");
//            shop.setLogo("http://localhost/xxx");
//            shop.setBerth_number("F1-v-2-" + (i + 1));
//            shop.setMap_name("2F");
//            shop.setPlane_map(3);
//
//            shop.setBrand("随便写");
//            shop.setBusiness_hours("早上10:00-晚上10:00");
//            shop.setStatus(1);
//            shop.setSort(i + 1);
//            int result = shopService.editShop(shop);
//            Assert.assertTrue(result >= 0);
//        }
    }

    @Test
    public void deleteShop() throws Exception {
        String json = "[{\"id\":1,\"name\":\"abc\",\"amount\":99},{\"id\":2,\"name\":\"def\",\"amount\":89}]";
        List<Map<String, Object>> list = (List<Map<String, Object>>) JsonPath.parse(json).read("$", List.class);

        for (Map<String, Object> map : list) {
            log.info(map.get("id").toString());
            log.info(map.get("name").toString());
        }
    }

}
