package com.laf.manager.controller;

import com.laf.manager.dto.PointsPayCashRule;
import com.laf.manager.querycondition.pointsrule.PointsPayCashRuleEditCondition;
import com.laf.manager.service.PointsPayCashRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class PointsPayCashRuleController {

    @Autowired
    PointsPayCashRuleService pointsPayCashRuleService;

    @GetMapping("/pointsPayCashRules")
    public ModelAndView getPointsPayCashRules() {
        List<PointsPayCashRule> list = pointsPayCashRuleService.getPointsPayCashRuleList();

        ModelAndView model = new ModelAndView();
        model.getModel().put("rules", list);

        model.setViewName("pointspaycashrules");

        return model;
    }

    @GetMapping("/pointsPayCashRule")
    public ModelAndView getPointsPayCashRule(@RequestParam(required = false) Integer ppcrId) throws Exception {
        final int id = ppcrId == null ? 0 : ppcrId.intValue();

        ModelAndView model = new ModelAndView();
        if (id != 0) {
            PointsPayCashRule rule = pointsPayCashRuleService.getPointsPayCashRuleById(ppcrId);
            if (rule == null) return new ModelAndView("404");

            model.getModel().put("rule", rule);
        }

        model.setViewName("pointspaycashrule");

        return model;
    }

    @PostMapping("/pointsPayCashRuleSave")
    public String editPointsPayCashRule(PointsPayCashRuleEditCondition condition, BindingResult errors) {

        if (errors.hasErrors()) {
            return "redirect:error";
        }

        int result = pointsPayCashRuleService.editPointsPayCashRule(condition);
        if (result > 0) {
            return "redirect:pointsPayCashRules";
        }else {
            return "redirect:error";
        }
    }

    @GetMapping("/delPointsPayCashRule")
    public String delPointsPayCashRule(@RequestParam Integer ppcrId) {
        int result = pointsPayCashRuleService.deletePointsPayCashRule(ppcrId);

        if (result > 0) {
            return "redirect:pointsPayCashRules";
        } else {
            return "redirect:errors";
        }
    }

}
