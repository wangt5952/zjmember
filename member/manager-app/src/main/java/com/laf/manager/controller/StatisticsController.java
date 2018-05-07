package com.laf.manager.controller;

import com.laf.manager.service.ActivityService;
import com.laf.manager.service.AssessmentService;
import com.laf.manager.service.CouponService;
import com.laf.manager.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@Slf4j
public class StatisticsController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private AssessmentService assessmentService;

//    @GetMapping("/charts")
    @GetMapping("/")
    public ModelAndView getChartData() {
        List<Integer> membersData = memberService.getMemberCountMonthly("2018");
        List<Integer> activityData = activityService.getActivityCountMonthly("2018");
        List<Integer> couponData = couponService.getCouponCountMonthly("2018");
        List<Integer> verfiData = couponService.getVerifCountMonthly("2018");
        List<Integer> assessmentData = assessmentService.getAssessmentCountMonthly("2018");

        String membersDataJson = "";
        for (int i = 0; i < membersData.size(); i++) {
            Integer data = membersData.get(i);
            membersDataJson += data + ((i+1) < membersData.size() ? "," : "");
        }

        String activityDataJson = "";
        for (int i = 0; i < activityData.size(); i++) {
            Integer data = activityData.get(i);
            activityDataJson += data + ((i+1) < activityData.size() ? "," : "");
        }

        String couponDataJson = "";
        for (int i = 0; i < couponData.size(); i++) {
            Integer data = couponData.get(i);
            couponDataJson += data + ((i+1) < couponData.size() ? "," : "");
        }

        String verfiDataJson = "";
        for (int i = 0; i < verfiData.size(); i++) {
            Integer data = verfiData.get(i);
            verfiDataJson += data + ((i+1) < verfiData.size() ? "," : "");
        }

        String assessmentDataJson = "";
        for (int i = 0; i < assessmentData.size(); i++) {
            Integer data = assessmentData.get(i);
            assessmentDataJson += data + ((i+1) < assessmentData.size() ? "," : "");
        }

        ModelAndView model = new ModelAndView();

        model.setViewName("datachart");

        model.getModel().put("membersData", membersDataJson);
        model.getModel().put("activityData", activityDataJson);
        model.getModel().put("couponData", couponDataJson);
        model.getModel().put("verfiData", verfiDataJson);
        model.getModel().put("assessmentData", assessmentDataJson);

        return model;
    }
}
