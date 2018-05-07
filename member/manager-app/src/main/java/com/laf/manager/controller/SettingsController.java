package com.laf.manager.controller;

import com.laf.manager.dto.ReceiveCouponInfo;
import com.laf.manager.dto.Reward;
import com.laf.manager.enums.CouponType;
import com.laf.manager.enums.RewardCategory;
import com.laf.manager.querycondition.settings.RewardEditCondition;
import com.laf.manager.service.CouponService;
import com.laf.manager.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SettingsController {

    @Autowired
    SettingsService settingsService;

    @Autowired
    CouponService couponService;

    @GetMapping("/ruleSettings")
    public ModelAndView getSettings() {
        ModelAndView model = new ModelAndView();
        model.setViewName("settings");

        List<Map<String, Object>> pointsRules = settingsService.getPointsRule();
        Map<String, String> couponRuleMap = new HashMap<>();
        float coefficient = settingsService.getPointsCoefficient();

        model.getModel().put("pointsRules", pointsRules);
        model.getModel().put("couponRuleMap", couponRuleMap);
        model.getModel().put("coefficient", coefficient);

        Reward registerReward = settingsService.getRewardFromCategory(RewardCategory.REGISTER);
        Reward completeReward = settingsService.getRewardFromCategory(RewardCategory.COMPLETE);
        Reward signInReward = settingsService.getRewardFromCategory(RewardCategory.SIGN_IN);

        model.getModel().put("registerReward", registerReward);
        model.getModel().put("completeReward", completeReward);
        model.getModel().put("signInReward", signInReward);

        List<ReceiveCouponInfo> coupons = couponService.getCouponInfoListFromType(CouponType.ASSOCIATION);
        model.getModel().put("coupons", coupons);

        return model;
    }

    @PostMapping("/pointsSettings")
    public String setPointsRule(@RequestParam String pointsJson) {
        int result = settingsService.editPointsRule(pointsJson);

        if (result <= 0) {
            return "redirect:error";
        }

        return "redirect:ruleSettings";
    }

    @PostMapping("/couponSettings")
    public String setCouponRule(@RequestParam String ruleJson) {
        int result = settingsService.editCouponRule(ruleJson);

        if (result <= 0) {
            return "redirect:error";
        }

        return "redirect:ruleSettings";
    }

    @PostMapping("/coefficientSettings")
    public String setPointsRule(@RequestParam Float pointsCoefficient) {
        int result = settingsService.editPointsCoefficient(pointsCoefficient);

        if (result <= 0) {
            return "redirect:error";
        }

        return "redirect:ruleSettings";
    }

    @PostMapping("/rewardSettings")
    public String setReward(RewardEditCondition condition, BindingResult errors) {
        Reward reward = new Reward();
        reward.setReward_id(condition.getRewardCategory());
        reward.setReward_type(condition.getRewardType());
        reward.setReward_value(condition.getRewardValue());

        int result = settingsService.editReward(reward);

        if (result <= 0) {
            return "redirect:error";
        }

        return "redirect:ruleSettings";}
}
