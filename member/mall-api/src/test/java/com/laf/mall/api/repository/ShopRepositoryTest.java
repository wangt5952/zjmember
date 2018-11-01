package com.laf.mall.api.repository;

import com.laf.mall.api.dto.Shop;
import com.laf.mall.api.querycondition.ShopQueryCondition;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ShopRepositoryTest {

    @Autowired
    private ShopRepository shopRepository;

    /**
     * SELECT s.shop_id, s.shop_name, s.phone, s.pictures, s.points, p.map_picture, i.industry_name, a.content as intro FROM `T_SHOP` s LEFT JOIN `T_PLANE_MAP` p ON s.plane_map=p.map_id LEFT JOIN `T_INDUSTRIES` i ON s.industry=i.industry_id LEFT JOIN `T_ARTICLES` a ON s.intro=a.article_id WHERE s.shop_id=2
     */
    @Test
    public void selectShopById() {
        Shop shop = shopRepository.selectShopById(2);
        Assert.assertNotNull(shop);
        log.info(shop.getShop_name());
    }

    @Test
    public void selectShopList() {
        ShopQueryCondition sq = new ShopQueryCondition();
        //如果没有传入mall_id，会直接返回null
        sq.setMall_id(1);
        //以下三个值不是必须传入
        sq.setKeywords("肯");
//        sq.setIndustry_id(4);
//        sq.setMap_id(2);
        sq.setSort("1");
        sq.setDirection("asc");
        sq.setPage(1);
        sq.setSize(5);

        ArrayList<Shop> list = (ArrayList<Shop>) shopRepository.selectShopList(sq);
        Assert.assertTrue(list != null && list.size()!= 0);
        for (Shop s : list) {
            log.info("sort=" + s.getSort());
        }
    }

    @Test
//    @Transactional
    public void save() {
        Shop s = new Shop();
        s.setShop_name("奶茶");
        s.setPlane_map(2);
        s.setIndustry(4);
        s.setMall_id(1);
        s.setSort(100100);
       // s = shopRepository.saveOne(s);
        //Assert.assertTrue(s.getShop_id() > 0);
    }

    @Test
    public void saveList() {
        ArrayList<Shop> list = new ArrayList<>();
        int in = 1300;
        for (int i = 0; i < 20; i++, in += 100) {
            Shop shop = new Shop();
            shop.setMall_id(2);
            shop.setPlane_map((i % 4) + 1);
            shop.setIndustry((i % 4) + 1);
            shop.setShop_name("苹果" + (i + 1));
            shop.setSort(100000 + in);
            list.add(shop);
        }
        //int rows = shopRepository.saveList(list);
       // Assert.assertTrue(rows > 0);
    }
}