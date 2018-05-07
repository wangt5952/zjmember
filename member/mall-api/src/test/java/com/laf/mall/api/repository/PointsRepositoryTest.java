package com.laf.mall.api.repository;

import com.laf.mall.api.dto.Points;
import com.laf.mall.api.querycondition.points.PointsQueryCondition;
import com.laf.mall.api.querycondition.points.PointsBaseCondition;
import com.laf.mall.api.querycondition.points.PointsSaveCondition;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PointsRepositoryTest {

    @Autowired
    private PointsRepository pointsRepository;

    @Test
    public void selectPontsList() throws Exception {
//        PointsQueryCondition condition = new PointsQueryCondition();
//        condition.setMallId(10);
//        condition.setMemberId(1);
//        condition.setPage(1);
//        condition.setSize(4);
//        List<Points> list = pointsRepository.selectPointsList(condition);
//        Assert.assertNotNull(list);
//        Assert.assertTrue(!list.isEmpty());
//        for (Points p : list) {
////            log.info("shopping_date={}", new Date(p.getShopping_date()));
//            log.info("shopping_date={}", p.getShopping_date());
//        }
    }

    @Test
    public void selectPointsDetail() throws Exception {
        Points p = pointsRepository.selectPointsDetail("118772455610388480");
        Assert.assertNotNull(p);
//        log.info("{}", p);
        log.info("shopping_date={}", p.getShopping_date());
    }

    @Test
    public void insertPoints() throws Exception {

        for (int i = 0; i < 10; i++) {
            Points p = new Points();
            p.setHandle_date(new Date());
            p.setSources(i % 4);
            p.setShop_name("kfc" + (i + 1));
            p.setShopping_date(new Date());
            if (4 / (i + 1) == 0) {
                p.setAmount(new BigDecimal(-(i + 1) * 100));
                p.setPoints(+(i + 1));
                p.setMember_id(1);
            } else {
                p.setAmount(new BigDecimal(+ (i + 1)));
                p.setPoints(- (i + 1) * 100);
                p.setMember_id(2);
            }
            p.setMall_id(10);
            p.setTicket_no("348y4fw3r");
            int result = pointsRepository.insertPoints(p);
            Assert.assertNotEquals(0, result);
        }
    }

    @Test
    public void insertPointsFromSaveConditon() {

//        PointsSaveCondition condition = new PointsSaveCondition();
//        condition.setMallId(10);
//        condition.setMemberId(1);
//        condition.setMemberMobile("13713831721");
//        condition.setPoints(200);
//        condition.setSources(4);
//        condition.setHandleDate(new Date());
//        int result = pointsRepository.insertPoints(condition);
//        Assert.assertNotEquals(0, result);
    }
}