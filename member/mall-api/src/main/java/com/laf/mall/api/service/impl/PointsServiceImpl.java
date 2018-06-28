package com.laf.mall.api.service.impl;

import com.jayway.jsonpath.JsonPath;
import com.laf.mall.api.dao.*;
import com.laf.mall.api.dto.*;
import com.laf.mall.api.querycondition.points.PointsActionCondition;
import com.laf.mall.api.querycondition.points.PointsQueryCondition;
import com.laf.mall.api.querycondition.points.PromotionQueryCondition;
import com.laf.mall.api.service.PointsService;
import com.laf.manager.core.support.tuple.Tuple2;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PointsServiceImpl implements PointsService {

    @Autowired
    private PointsDao pointsDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private SimplePointsRuleDao simplePointsRuleDao;

    @Autowired
    private PromotionPointsRuleDao promotionPointsRuleDao;

    @Autowired
    private PointsPayCashRuleDao pointsPayCashRuleDao;

    @Autowired
    private MallDao mallDao;

    @Autowired
    ShopDao shopDao;

    @Autowired
    LevelDao levelDao;

    public List<Points> getPointsList(PointsQueryCondition condition, Integer memberId) {
        return pointsDao.getPointsList(condition, memberId);
    }

    @Override
    public Points getPointsDetail(String mplogId) {
        return pointsDao.getPointsDetail(mplogId);
    }

    @Override
    public Points getPointsDetailByTickNo(String ticket_no) {
        return pointsDao.getPointsDetailByTicketNo(ticket_no);
    }

    @Override
    public int savePoints(Points points) {
        return pointsDao.savePoints(points);
    }

    public int calculatePoints(Integer mallId) {
        List<SimplePointsRule> ruleList = simplePointsRuleDao.getAllSimpleRule(mallId);
        List<Shop> allShopList = shopDao.getAllShopList(mallId);

        int result = 0;
        SimplePointsQuery defaultSimplePoints = new SimplePointsQuery();


        for (SimplePointsRule rule : ruleList) {
            for (Shop shop : allShopList) {
                defaultSimplePoints.setAmount(rule.getAmount());
                defaultSimplePoints.setShop_id(shop.getShop_id());
                defaultSimplePoints.setLevel_id(rule.getLevel_id());
                defaultSimplePoints.setMall_id(rule.getMall_id());

                result = simplePointsRuleDao.savePointsQuery(defaultSimplePoints);
            }
        }

        List<SimplePointsQuery> industryList = simplePointsRuleDao.getSimpleAmountListByIndustry(1);

        for (SimplePointsQuery simplePointsQuery : industryList) {
            simplePointsRuleDao.editPointsQuery(simplePointsQuery);
        }

        List<SimplePointsQuery> shopPointsList = simplePointsRuleDao.getSimpleAmountListByShopInMall(1);

        for (SimplePointsQuery simplePointsQuery : shopPointsList) {
            simplePointsRuleDao.editPointsQuery(simplePointsQuery);
        }

        return 1;
    }

    @Override
    public int calculatePromotionPoints(Integer ruleId) {

        PromotionPointsRule rule = promotionPointsRuleDao.getRuleContentById(ruleId);

        if (rule != null) {

            /************************************插入day points*********************************************************/
            LocalDate start = Instant.ofEpochMilli(rule.getStart_date()).atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate end = Instant.ofEpochMilli(rule.getEnd_date()).atZone(ZoneId.systemDefault()).toLocalDate();

            int startDays = start.getDayOfYear();
            int endDays = end.getDayOfYear();

            if (end.getYear() > start.getYear()) {
                endDays = end.getDayOfYear() + (start.getYear() % 4 == 0 ? 366 : 365);
            }

            for (int i = startDays, d = 0; i <= endDays; i++, d++) {
                LocalDate localDate = start.plusDays(d);
                LocalDateTime time =
                        LocalDateTime.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), 0, 0, 0, 0);

                ZonedDateTime zdt = time.atZone(ZoneId.systemDefault());
                promotionPointsRuleDao.saveDayPointsRule(ruleId, zdt.toInstant().toEpochMilli());
            }

            /***********************************jsonpath 解析*************************************************************/

            String json = rule.getRule_content();
            int size = ((net.minidev.json.JSONArray) JsonPath.parse(json).read("$..industries")).size();
            List<Map<String, Object>> list = ListUtils.EMPTY_LIST;
            List<Shop> shops = new ArrayList<>();

            if (size > 0) {
                list = (List<Map<String, Object>>) JsonPath.parse(json).read("$.industries", List.class);

                for (Map<String, Object> map : list) {
                    Integer industryId = (Integer) map.get("industry_id");
                    List<Shop> _list = shopDao.getShopListByIndustry(industryId, rule.getMall_id());
                    shops.addAll(_list);
                }

            } else {
                size = ((net.minidev.json.JSONArray) JsonPath.parse(json).read("$..shops")).size();

                if (size > 0) {
                    list = (List<Map<String, Object>>) JsonPath.parse(json).read("$.shops", List.class);

                    for (Map<String, Object> map : list) {
                        Shop shop = new Shop();
                        shop.setMall_id(rule.getMall_id());
                        shop.setShop_name(map.get("shop_name").toString());
                        shop.setShop_id((Integer) map.get("shop_id"));
                        shops.add(shop);
                    }
                } else {
                    shops = shopDao.getAllShopList(rule.getMall_id());
                }
            }

            List<Level> levels = new ArrayList<>();
            size = ((net.minidev.json.JSONArray) JsonPath.parse(json).read("$..levels")).size();

            if (size > 0) {
                list = (List<Map<String, Object>>) JsonPath.parse(json).read("$.levels", List.class);

                for (Map<String, Object> map : list) {
                    Level level = new Level();
                    level.setMall_id(rule.getMall_id());
                    level.setLevel_id((Integer) map.get("level_id"));
                    level.setLevel_name(map.get("level_name").toString());

                    levels.add(level);
                }
            } else {
                levels = levelDao.getAllLevel(rule.getMall_id());
            }


            for (Level level : levels) {
                for (Shop shop : shops) {

                    PromotionPointsQuery promotionPointsQuery = new PromotionPointsQuery();
                    promotionPointsQuery.setAmount(rule.getAmount());
                    promotionPointsQuery.setRule_id(rule.getPpr_id());
                    promotionPointsQuery.setBirthday_start(rule.getBirthday_start());
                    promotionPointsQuery.setBirthday_end(rule.getBirthday_end());
                    promotionPointsQuery.setLevel_id(level.getLevel_id());
                    promotionPointsQuery.setShop_id(shop.getShop_id());

                    promotionPointsRuleDao.savePointsQuery(promotionPointsQuery);
                }
            }

            return 1;
        }

        return 0;
    }

    @Override
    public BigDecimal getSimpleAmount(Integer levelId, Integer shopId) {
        return simplePointsRuleDao.getSimpleAmount(levelId, shopId);
    }

    @Override
    public BigDecimal getPromotionAmount(PromotionQueryCondition condition) {
        BigDecimal amount = promotionPointsRuleDao.getRule(condition);

        return amount;
    }

    @Override
    public Tuple2<Integer, Integer> getActionPointsByType2(Integer mallId) {
        ActionPointsRule rule = mallDao.getActionPointsRuleByOption(2, mallId);
        if (rule == null) return null;

        Tuple2<Integer, Integer> t2 = null;
        switch (rule.getOptions()) {
            case 0: //积分
                t2 = new Tuple2<>(rule.getPoints(), 0);
                break;

            case 1: //券
                t2 = new Tuple2<>(0, rule.getCoupon_id());
                break;

            case 2: //积分&券
                t2 = new Tuple2<>(rule.getPoints(), rule.getCoupon_id());
                break;
        }

        return t2;
    }

    @Override
    public int deleteSimpleRule(Integer ruleId) {
        SimplePointsRule rule = simplePointsRuleDao.getSimpleRuleById(ruleId);

        if (rule == null) return 0;

        simplePointsRuleDao.deleteSimpleIndustryPoints(ruleId);
        simplePointsRuleDao.deleteSimpleShopPoints(ruleId);
        simplePointsRuleDao.deleteSimpleQuery(rule.getLevel_id());
        simplePointsRuleDao.deleteSimpleRule(ruleId);

        return 1;
    }

    @Override
    public int deletePromotionRule(Integer ruleId) {
        return promotionPointsRuleDao.deletePromotionRule(ruleId);
    }

    @Override
    public int addPointsExcludeConsume(PointsActionCondition condition) {
        return pointsDao.savePointsExcludeConsume(condition);
    }

    @Override
    public int getPointsPayCashRuleByType(int type, int mallId) {
        return pointsPayCashRuleDao.getPointsPayCashRuleByType(type, mallId);
    }
}
