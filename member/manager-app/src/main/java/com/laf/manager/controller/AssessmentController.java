package com.laf.manager.controller;

import com.laf.manager.SettingsProperties;
import com.laf.manager.dto.Assessment;
import com.laf.manager.querycondition.assessment.AssessmentEditCondition;
import com.laf.manager.querycondition.assessment.AssessmentQueryCondition;
import com.laf.manager.service.AssessmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

@Controller
@Slf4j
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    @Autowired
    private SettingsProperties settingsProperties;

    @GetMapping("/assessments")
    public ModelAndView getAssessments(HttpSession session) {
        ModelAndView model = new ModelAndView();
        AssessmentQueryCondition condition = (AssessmentQueryCondition) session.getAttribute("assessment_querycondition");

        if (condition == null) condition = new AssessmentQueryCondition();

        condition.setMallId(settingsProperties.getMallId());
        List<Assessment> list = assessmentService.getAssessmentList(condition);

        int total = assessmentService.getAssessmentCount(condition);
        HashMap<String, Object> pageMap = new HashMap<>();
        pageMap.put("index", condition.getPage());
        pageMap.put("size", condition.getSize());
        pageMap.put("total", total);

        model.getModel().put("assessments", list);
        model.getModel().put("pageMap", pageMap);
        model.setViewName("assessments");

        return model;
    }

    @PostMapping("/assessments/filter")
    public String assessmentsFilter(AssessmentQueryCondition condition, String filterJson, BindingResult errors, HttpSession session) {
        session.setAttribute("assessment_querycondition", condition);
        session.setAttribute("assessment_filter", filterJson);

        return "redirect:/assessments";
    }

    @PostMapping("/assessments/reset")
    public String resetAssessments(HttpSession session) {
        Enumeration<String> names = session.getAttributeNames();

        while (names.hasMoreElements()) {
            String name = names.nextElement();

            if (name.startsWith("assessment_")) {
                session.removeAttribute(name);
            }
        }

        return "redirect:/assessments";
    }

    @GetMapping("/assessment")
    public ModelAndView getAssessment(@RequestParam Integer assessmentId) {

        if (assessmentId == null || assessmentId <= 0) return new ModelAndView("404");

        Assessment assessment = assessmentService.getAssessmentById(assessmentId);

        if (assessment == null) return new ModelAndView("404");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModel().put("assessment", assessment);
        modelAndView.setViewName("assessment");

        return modelAndView;
    }

    @PostMapping("/saveAssessment")
    public String saveAssessment(AssessmentEditCondition condition, BindingResult errors) {

        if (errors.hasErrors()) {
            return "redirect:error";
        }

        int result = assessmentService.editAssessment(condition);

        if (result <= 0) return "redirect:404";

        return "redirect:/assessments";
    }

    @GetMapping("/delAssessment")
    public String delAssessment(@RequestParam Integer assessmentId) {

        if (assessmentId == null || assessmentId <= 0) return "redirect:404";

        int result = assessmentService.delAssessment(assessmentId);

        if (result <= 0) return "redirect:404";

        return "redirect:/assessments";
    }
}
