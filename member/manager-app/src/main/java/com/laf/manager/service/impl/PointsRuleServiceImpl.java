package com.laf.manager.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.laf.manager.SettingsProperties;
import com.laf.manager.core.exception.MallDBException;
import com.laf.manager.dao.LevelDao;
import com.laf.manager.dao.PromotionPointsRuleDao;
import com.laf.manager.dao.ShopDao;
import com.laf.manager.dao.SimplePointsRuleDao;
import com.laf.manager.dto.*;
import com.laf.manager.querycondition.pointsrule.PromotionEditCondition;
import com.laf.manager.querycondition.pointsrule.SimpleEditCondition;
import com.laf.manager.service.LevelService;
import com.laf.manager.service.PointsRuleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;

@Service
@Slf4j
public class PointsRuleServiceImpl implements PointsRuleService {

    @Autowired
    SimplePointsRuleDao simplePointsRuleDao;

    @Autowired
    PromotionPointsRuleDao promotionPointsRuleDao;

    @Autowired
    SettingsProperties settingsProperties;

    @Autowired
    ShopDao shopDao;

    @Autowired
    LevelDao levelDao;

    @Autowired
    LevelService levelService;

    @Override
    @Transactional
    public int editSimpleRule(SimpleEditCondition condition) throws MallDBException {
        SimplePointsRule rule = simplePointsRuleDao.getSimpleRuleById(condition.getRuleId());

        if (rule == null) {
            rule = new SimplePointsRule();
        }

        rule.setMall_id(settingsProperties.getMallId());
        rule.setLevel_id(condition.getLevelId());
        rule.setRule_name(condition.getRuleName());
        rule.setAmount(condition.getAmount());

        int result = 0;
        if (condition.getRuleId() == 0) { // insert

            int key = simplePointsRuleDao.saveRule(rule);

            if (key <= 0) return key;
            else result = key;

            List<Map<String, Object>> list = (List<Map<String, Object>>) JsonPath.parse(condition.getShopsRule()).read("$", List.class);
            List<ShopPointsAssociation> shopPointsAssociations = new ArrayList<>();

            for (Map<String, Object> map : list) {
                ShopPointsAssociation shopPointsAssociation = new ShopPointsAssociation();
                shopPointsAssociation.setRule_id(key);
                shopPointsAssociation.setAmount(new BigDecimal(map.get("amount").toString()));
                shopPointsAssociation.setShop_id((Integer.valueOf(map.get("id").toString())));
                shopPointsAssociation.setShop_name(map.get("name").toString());

                shopPointsAssociations.add(shopPointsAssociation);
            }

            if (shopPointsAssociations.size() > 0) {

                if (shopPointsAssociations.size() == 1) {
                    result = simplePointsRuleDao.saveShopsRule(shopPointsAssociations.get(0));
                } else {
                    result = simplePointsRuleDao.saveShopPointsAssociations(shopPointsAssociations);
                }

                if (result <= 0) throw new MallDBException();
            }

            list = (List<Map<String, Object>>) JsonPath.parse(condition.getIndusRule()).read("$", List.class);
            for (Map<String, Object> map : list) {
                IndustryPointsAssociation industryPointsAssociation = new IndustryPointsAssociation();
                industryPointsAssociation.setRule_id(key);
                industryPointsAssociation.setAmount(new BigDecimal(map.get("amount").toString()));
                industryPointsAssociation.setIndustry_id(Integer.valueOf(map.get("id").toString()));
                industryPointsAssociation.setIndustry_name(map.get("name").toString());

                result = simplePointsRuleDao.saveIndusRule(industryPointsAssociation);

                if (result <= 0) throw new MallDBException();
            }

            result = levelService.updateLevelUsableFalse(condition.getLevelId());
            if (result <= 0) throw new MallDBException();

        } else {
            rule.setSpr_id(condition.getRuleId());
            result = simplePointsRuleDao.editRule(rule);

            if (result <= 0) return result;

            if (simplePointsRuleDao.getShopPointsCount(rule.getSpr_id()) > 0) {
                result = simplePointsRuleDao.deleteSimpleShopPoints(rule.getSpr_id());
                if (result <= 0) throw new MallDBException();
            }

            if (simplePointsRuleDao.getIndustryPointsCount(rule.getSpr_id()) > 0) {
                result = simplePointsRuleDao.deleteSimpleIndustryPoints(rule.getSpr_id());
                if (result <= 0) throw new MallDBException();
            }

            List<Map<String, Object>> list = (List<Map<String, Object>>) JsonPath.parse(condition.getShopsRule()).read("$", List.class);
            List<ShopPointsAssociation> shopPointsAssociations = new ArrayList<>();

            if (list.size() > 0) {

                for (Map<String, Object> map : list) {
                    ShopPointsAssociation shopPointsAssociation = new ShopPointsAssociation();
                    shopPointsAssociation.setAmount(new BigDecimal(map.get("amount").toString()));
                    shopPointsAssociation.setShop_id(Integer.valueOf(map.get("id").toString()));
                    shopPointsAssociation.setShop_name(map.get("name").toString());
                    shopPointsAssociation.setRule_id(rule.getSpr_id());

                    shopPointsAssociations.add(shopPointsAssociation);
                }
            }

            if (shopPointsAssociations.size() > 0) {

                if (shopPointsAssociations.size() == 1) {
                    result = simplePointsRuleDao.saveShopsRule(shopPointsAssociations.get(0));
                } else {
                    result = simplePointsRuleDao.saveShopPointsAssociations(shopPointsAssociations);
                }

                if (result <= 0) throw new MallDBException();
            }

            list = (List<Map<String, Object>>) JsonPath.parse(condition.getIndusRule()).read("$", List.class);

            if (list.size() > 0) {

                for (Map<String, Object> map : list) {
                    IndustryPointsAssociation industryPointsAssociation = new IndustryPointsAssociation();
                    industryPointsAssociation.setAmount(new BigDecimal(map.get("amount").toString()));
                    industryPointsAssociation.setIndustry_id(Integer.valueOf(map.get("id").toString()));
                    industryPointsAssociation.setIndustry_name(map.get("name").toString());
                    industryPointsAssociation.setRule_id(rule.getSpr_id());

                    result = simplePointsRuleDao.saveIndusRule(industryPointsAssociation);
                    if (result <= 0) throw new MallDBException();
                }
            }

            if (simplePointsRuleDao.getSimpleQueryCount(condition.getLevelId()) > 0) {
                result = simplePointsRuleDao.deleteSimpleQuery(condition.getLevelId());
                if (result <= 0) throw new MallDBException();
            }
        }

        try {
            result = calculateSimple(condition.getLevelId());
        } catch (MallDBException e) {
            result = e.getResult();
        }

        return result;
    }

    @Override
    public List<SimplePointsRule> getSimpleList() {
        return simplePointsRuleDao.getAllSimpleRule(settingsProperties.getMallId());
    }

    @Override
    public SimplePointsRule getSimpleRuleById(Integer ruleId) {
        return simplePointsRuleDao.getSimpleRuleById(ruleId);
    }

    @Override
    public List<IndustryPointsAssociation> getSimpleIndusRuleById(Integer ruleId) {
        return simplePointsRuleDao.getSimpleIndusRuleListById(ruleId);
    }

    @Override
    public List<ShopPointsAssociation> getSimpleShopsRuleById(Integer ruleId) {
        return simplePointsRuleDao.getSimpleShopsRuleListById(ruleId);
    }

    @Override
    public String getSimpleIndusRuleJsonById(Integer ruleId) throws JsonProcessingException {
        List<IndustryPointsAssociation> list = this.getSimpleIndusRuleById(ruleId);
        List<Map<String, Object>> indus = new ArrayList<>();

        for (IndustryPointsAssociation industryPointsAssociation : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", industryPointsAssociation.getIndustry_id());
            map.put("name", industryPointsAssociation.getIndustry_name());
            map.put("amount", industryPointsAssociation.getAmount().toString());

            indus.add(map);
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(indus);
        return jsonInString;
    }

    @Override
    public String getSimpleShopsRuleJsonById(Integer ruleId) throws JsonProcessingException {
        List<ShopPointsAssociation> list = this.getSimpleShopsRuleById(ruleId);
        List<Map<String, Object>> indus = new ArrayList<>();

        for (ShopPointsAssociation shopPointsAssociation : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", shopPointsAssociation.getShop_id());
            map.put("name", shopPointsAssociation.getShop_name());
            map.put("amount", shopPointsAssociation.getAmount().toString());

            indus.add(map);
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(indus);
        return jsonInString;
    }

    @Override
    @Transactional
    public int editPromotionRule(PromotionEditCondition condition) throws MallDBException {

        PromotionPointsRule rule = promotionPointsRuleDao.getRuleContentById(condition.getRuleId());

        if (rule == null) {
            rule = new PromotionPointsRule();
        }

        rule.setMall_id(settingsProperties.getMallId());
        rule.setAmount(condition.getAmount());
        rule.setStart_date(new Date(condition.getStartTime()));
        rule.setEnd_date(new Date(condition.getEndTime()));
        rule.setBirthday_start(condition.getBirthdayStart());
        rule.setBirthday_end(condition.getBirthdayEnd());
        rule.setRule_name(condition.getRuleName());
        rule.setRule_content(condition.getRuleContent());

        int result = 0;

        if (condition.getRuleId() == 0) { // insert
            if (condition.getBirthdayEnd() == null)
                result = promotionPointsRuleDao.savePointsRuleWithoutBirthday(rule);
            else
                result = promotionPointsRuleDao.savePointsRule(rule);

            try {
                result = calculatePromotion(result);
            } catch (MallDBException e) {
                result = e.getResult();
            }

        } else {

            if (condition.getBirthdayEnd() == null)
                result = promotionPointsRuleDao.editPointsRuleWithoutBirthday(rule);
            else
                result = promotionPointsRuleDao.editPointsRule(rule);

            result = promotionPointsRuleDao.removePromotionQuery(condition.getRuleId());
            if (result <= 0) throw new MallDBException();

            try {
                result = calculatePromotion(condition.getRuleId());
            } catch (MallDBException e) {
                result = e.getResult();
            }
        }

        return result;
    }

    @Override
    public PromotionPointsRule getPromotionRuleById(Integer ruleId) {
        return promotionPointsRuleDao.getRuleContentById(ruleId);
    }

    @Override
    @Transactional
    public int calculateSimple(Integer levelId) throws MallDBException {
        List<Shop> shops = shopDao.getAllShopList(settingsProperties.getMallId());

        List<SimplePointsRule> rules = simplePointsRuleDao.getAllSimpleRule(settingsProperties.getMallId());

        int result = 0;

        for (SimplePointsRule rule : rules) {
            if (rule.getLevel_id() == levelId) {

                List<SimplePointsQuery> queries = new ArrayList<>();

                for (Shop shop : shops) {
                    SimplePointsQuery query = new SimplePointsQuery();
                    query.setAmount(rule.getAmount());
                    query.setLevel_id(levelId);
                    query.setMall_id(settingsProperties.getMallId());
                    query.setShop_id(shop.getShop_id());

                    queries.add(query);
//                    result = simplePointsRuleDao.savePointsQuery(query);
//                    if (result <= 0) throw new MallDBException();
                }

                if (queries.size() > 0) {
                    result = simplePointsRuleDao.savePointsQueries(queries);
                    if (result <= 0) throw new MallDBException();
                }

                List<IndustryPointsAssociation> indus = simplePointsRuleDao.getSimpleIndusRuleListById(rule.getSpr_id());

                for (IndustryPointsAssociation industry : indus) {
                    shops = shopDao.getShopListByIndustry(industry.getIndustry_id(), settingsProperties.getMallId());

                    for (Shop shop : shops) {
                        SimplePointsQuery query = new SimplePointsQuery();
                        query.setShop_id(shop.getShop_id());
                        query.setMall_id(settingsProperties.getMallId());
                        query.setAmount(industry.getAmount());
                        query.setLevel_id(levelId);
                        result = simplePointsRuleDao.editPointsQuery(query);

                        if (result <= 0) throw new MallDBException();
                    }
                }

                List<ShopPointsAssociation> shopPointsAssociations = simplePointsRuleDao.getSimpleShopsRuleListById(rule.getSpr_id());

                for (ShopPointsAssociation shopPointsAssociation : shopPointsAssociations) {
                    SimplePointsQuery query = new SimplePointsQuery();
                    query.setShop_id(shopPointsAssociation.getShop_id());
                    query.setMall_id(settingsProperties.getMallId());
                    query.setAmount(shopPointsAssociation.getAmount());
                    query.setLevel_id(levelId);
                    result = simplePointsRuleDao.editPointsQuery(query);

                    if (result <= 0) throw new MallDBException();
                }

                break;
            }
        }

        return result;
    }

    @Override
    @Transactional
    public int calculatePromotion(Integer ruleId) throws MallDBException {
        PromotionPointsRule rule = promotionPointsRuleDao.getRuleContentById(ruleId);

        int result = 0;

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

                result = promotionPointsRuleDao.saveDayPointsRule(ruleId, zdt.toInstant().toEpochMilli());
                if (result <= 0) throw new MallDBException();
            }

            /***********************************jsonpath 解析*************************************************************/

            String json = rule.getRule_content();

            int size = 0;
            List<Shop> shops = new ArrayList<>();
            List<Level> levels = new ArrayList<>();
            List<Map<String, Object>> list = ListUtils.EMPTY_LIST;

            if (!StringUtils.isEmpty(json)) {

                size = ((net.minidev.json.JSONArray) JsonPath.parse(json).read("$..industries")).size();

                if (size > 0) {
                    list = (List<Map<String, Object>>) JsonPath.parse(json).read("$.industries", List.class);

                    for (Map<String, Object> map : list) {
                        Integer industryId = Integer.valueOf(map.get("industry_id").toString());
                        List<Shop> _list = shopDao.getShopListByIndustry(industryId, rule.getMall_id());
                        shops.addAll(_list);
                    }
                }

                size = ((net.minidev.json.JSONArray) JsonPath.parse(json).read("$..shops")).size();
                if (size > 0) {
                    list = (List<Map<String, Object>>) JsonPath.parse(json).read("$.shops", List.class);

                    for (Map<String, Object> map : list) {
                        Shop shop = new Shop();
                        shop.setMall_id(rule.getMall_id());
                        shop.setShop_name(map.get("shop_name").toString());
                        log.info("========={}==========", map.get("shop_id").toString());
                        shop.setShop_id(Integer.valueOf(map.get("shop_id").toString()));
                        shops.add(shop);
                    }
                }

                size = ((net.minidev.json.JSONArray) JsonPath.parse(json).read("$..levels")).size();

                if (size > 0) {
                    list = (List<Map<String, Object>>) JsonPath.parse(json).read("$.levels", List.class);

                    for (Map<String, Object> map : list) {
                        Level level = new Level();
                        level.setMall_id(rule.getMall_id());
                        level.setLevel_id(Integer.valueOf(map.get("level_id").toString()));
                        level.setLevel_name(map.get("level_name").toString());

                        levels.add(level);
                    }
                }
            }

            if (shops.size() == 0) {
                shops = shopDao.getAllShopList(rule.getMall_id());
            }

            if (levels.size() == 0) {
                levels = levelDao.getAllLevel(rule.getMall_id());
            }

            for (Level level : levels) {
                for (Shop shop : shops) {

                    if (rule.getBirthday_end() == 0) {
                        LocalDate date = LocalDate.now();
                        LocalDateTime time = LocalDateTime.of(date.getYear()+1, 12, 31, 0, 0, 0, 0);
                        ZonedDateTime zdt = time.atZone(ZoneId.systemDefault());

                        long milli = zdt.toInstant().toEpochMilli();

                        rule.setBirthday_end(new Date(milli));
                    }

                    PromotionPointsQuery promotionPointsQuery = new PromotionPointsQuery();
                    promotionPointsQuery.setAmount(rule.getAmount());
                    promotionPointsQuery.setRule_id(rule.getPpr_id());
                    promotionPointsQuery.setBirthday_start(rule.getBirthday_start());
                    promotionPointsQuery.setBirthday_end(rule.getBirthday_end());
                    promotionPointsQuery.setLevel_id(level.getLevel_id());
                    promotionPointsQuery.setShop_id(shop.getShop_id());

                    result = promotionPointsRuleDao.savePointsQuery(promotionPointsQuery);
                    if (result <= 0) throw new MallDBException();
                }
            }
        }

        return result;
    }

    @Override
    @Transactional
    public int deleteSimplePointsRule(Integer ruleId, Integer levelId) throws MallDBException {
        int result =  simplePointsRuleDao.deleteSimpleRule(ruleId);

        if (result <= 0)
        {
            log.error("delete table `T_SIMPLE_RULE` fail");
            return 0;
        }

        if (simplePointsRuleDao.getShopPointsCount(ruleId) > 0) {
            result = simplePointsRuleDao.deleteSimpleShopPoints(ruleId);
            if (result <= 0) throw new MallDBException();
        }

        if (simplePointsRuleDao.getIndustryPointsCount(ruleId) > 0) {
            result = simplePointsRuleDao.deleteSimpleIndustryPoints(ruleId);
            if (result <= 0) throw new MallDBException();
        }


        if (simplePointsRuleDao.getSimpleQueryCount(levelId) > 0) {
            result = simplePointsRuleDao.deleteSimpleQuery(levelId);
            if (result <= 0) throw new MallDBException();
        }

        result = levelService.updateLevelUsableTrue(levelId);
        if (result <= 0) throw new MallDBException();

        return result;
    }

    @Override
    public List<PromotionPointsRule> getPromotionList() {
        return promotionPointsRuleDao.getPromotionPointsRuleList(settingsProperties.getMallId());
    }
}
