package com.laf.manager.controller;

import com.laf.manager.SettingsProperties;
import com.laf.manager.core.exception.MallDBException;
import com.laf.manager.dao.PromotionPointsRuleDao;
import com.laf.manager.dto.*;
import com.laf.manager.querycondition.pointsrule.PromotionEditCondition;
import com.laf.manager.querycondition.pointsrule.SimpleEditCondition;
import com.laf.manager.querycondition.shop.ShopQueryCondition;
import com.laf.manager.service.LevelService;
import com.laf.manager.service.MallService;
import com.laf.manager.service.PointsRuleService;
import com.laf.manager.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Controller
@Slf4j
public class PointsRuleController {

    @Autowired
    LevelService levelService;

    @Autowired
    MallService mallService;

    @Autowired
    ShopService shopService;

    @Autowired
    PointsRuleService pointsRuleService;

    @Autowired
    SettingsProperties settingsProperties;

    @Autowired
    PromotionPointsRuleDao promotionPointsRuleDao;

    @GetMapping("/simpleRule")
    public ModelAndView getSimplePointsRule(@RequestParam(required = false) Integer ruleId) throws Exception {
        final int id = ruleId == null ? 0 : ruleId.intValue();
        ModelAndView model = new ModelAndView();

        List<Level> levels = null;

        if (id != 0) {
            SimplePointsRule rule = pointsRuleService.getSimpleRuleById(id);
            if (rule == null) {
                return new ModelAndView("404");
            }

            String indusJson = pointsRuleService.getSimpleIndusRuleJsonById(id);
            String shopsJson = pointsRuleService.getSimpleShopsRuleJsonById(id);
            model.getModel().put("rule", rule);
            model.getModel().put("indusJson", indusJson);
            model.getModel().put("shopsJson", shopsJson);

            levels = levelService.getAllLevelByMall();
        } else {
            levels = levelService.getAllUsableLevelByMall();
        }

        List<Shop> shops = shopService.getShopAllList();

        List<Industry> industries = mallService.getIndustries();

        model.getModel().put("levels", levels);
        model.getModel().put("shops", shops);
        model.getModel().put("industries", industries);

        model.setViewName("simplerule");

        return model;
    }

    @PostMapping("/simpleRuleSave")
    public String editSimpleRule(SimpleEditCondition condition, BindingResult errors) {

        if (errors.hasErrors()) {
            return "redirect:error";
        }

        int result = 0;

        try {
            result = pointsRuleService.editSimpleRule(condition);
        } catch (MallDBException e) {
            result = e.getResult();
        }

        if (result > 0) {
            return "redirect:simpleRules";
        }

        return "redirect:error";
    }

    @GetMapping("/promotionRule")
    public ModelAndView getPromotionPointsRule(@RequestParam(required = false) Integer ruleId) throws Exception {
        final int id = ruleId == null ? 0 : ruleId.intValue();
        ModelAndView model = new ModelAndView();

        if (id != 0) {
            PromotionPointsRule rule = pointsRuleService.getPromotionRuleById(id);
            if (rule == null) {
                return new ModelAndView("404");
            }

            model.getModel().put("rule", rule);
        }

        List<Level> levels = levelService.getAllLevelByMall();
        ShopQueryCondition condition = new ShopQueryCondition();
        condition.setMall_id(settingsProperties.getMallId());
        condition.setSize(200);
        List<Shop> shops = shopService.getShopList(condition);

        List<Industry> industries = mallService.getIndustries();

        model.getModel().put("levels", levels);
        model.getModel().put("shops", shops);
        model.getModel().put("industries", industries);

        model.setViewName("promotionrule");

        return model;
    }

    @PostMapping("/promotionRuleSave")
    public String editPromotionRule(PromotionEditCondition condition, BindingResult errors) {

        if (errors.hasErrors()) {
            return "redirect:error";
        }

        int result = 0;

        try {
            result = pointsRuleService.editPromotionRule(condition);
        } catch (MallDBException e) {
            result = e.getResult();
        }

        if (result > 0) {
            return "redirect:promotionRules";
        }

        return "redirect:error";
    }

    @GetMapping("/promotionRules")
    public ModelAndView getPromotionRules() {
        List<PromotionPointsRule> list = pointsRuleService.getPromotionList();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModel().put("ppRules", list);
        modelAndView.setViewName("promotionrules");

        int levelUsable = levelService.getAllLevelByMall().size();
        modelAndView.getModel().put("levelUsable", levelUsable);

        return modelAndView;
    }

    @GetMapping("/simpleRules")
    public ModelAndView getSimpleRules() {
        List<SimplePointsRule> list = pointsRuleService.getSimpleList();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModel().put("simpleRules", list);
        modelAndView.setViewName("simplerules");

        int levelUsable = levelService.getAllUsableLevelByMall().size();
        modelAndView.getModel().put("levelUsable", levelUsable);

        return modelAndView;
    }

    @GetMapping("/delSimpleRule")
    public String delSimpleRule(@RequestParam Integer ruleId, @RequestParam Integer levelId) {

//        if (ruleId <= 0 || levelId <= 0) return "redirect:error";
        int result = 0;

        try {
            result = pointsRuleService.deleteSimplePointsRule(ruleId, levelId);
        } catch (MallDBException e) {
            result = e.getResult();
        }

        if (result <= 0) {
            return "redirect:error";
        }

        return "redirect:simpleRules";
    }

    @GetMapping("/delPromotionRule")
    public String delPromotionRule(@RequestParam Integer ruleId) {
        int result = promotionPointsRuleDao.deletePromotionRule(ruleId);

        if (result <= 0) {
            return "redirect:error";
        }

        return "redirect:promotionRules";
    }
}
