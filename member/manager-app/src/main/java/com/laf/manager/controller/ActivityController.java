package com.laf.manager.controller;


import com.laf.manager.SettingsProperties;
import com.laf.manager.dto.Activity;
import com.laf.manager.dto.ActivitySignUpLog;
import com.laf.manager.querycondition.activity.ActivityEditCondition;
import com.laf.manager.querycondition.activity.ActivityLogQueryCondition;
import com.laf.manager.querycondition.activity.ActivityQueryCondition;
import com.laf.manager.service.ActivityService;
import com.laf.manager.utils.file.FileProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@Slf4j
public class ActivityController {

    @Autowired
    ActivityService activityService;

    @Autowired
    FileProperties fileProperties;

    @Autowired
    SettingsProperties settingsProperties;

    @GetMapping("/activity")
    public ModelAndView getActivity(@RequestParam(required = false) Integer activityId) {
        ModelAndView model = new ModelAndView();

        int id = activityId == null ? 0 : activityId.intValue();

        Activity activity = activityService.getActivityById(id);

        if (activity != null) {
            model.getModel().put("activity", activity);
            model.getModel().put("isSignUp", activity.isIs_sign_up());
            model.getModel().put("isSignIn", activity.isIs_sign_in());
            model.getModel().put("isComment", activity.is_comment());
        }

        model.setViewName("activity");

        return model;
    }

    @PostMapping("/editActivity")
    public String editActivity(MultipartFile file, ActivityEditCondition condition, BindingResult errors) throws IOException {

        if (errors.hasErrors()) {
            return "redirect:error";
        }

        String picture = "";

        if (file != null && file.getName().equals("file")) {
            String uploadFileName = file.getOriginalFilename();

            if (!StringUtils.isEmpty(uploadFileName)) {

                String suffixName = uploadFileName.substring(uploadFileName.lastIndexOf("."));
                String folder = fileProperties.getPath();
                String fileName = "activity_" + new Date().getTime() + suffixName;

                File localFile = new File(folder, fileName);
                file.transferTo(localFile);
                String url = fileProperties.getDomain() + fileProperties.getBase() + fileName;
                picture = url;
            }
        }

        condition.setPicture(picture);

        try {
            activityService.editActivity(condition);
        } catch (Exception e) {

        }

        return "redirect:activities";
    }

    @GetMapping("/activities")
    public ModelAndView getActivities(HttpSession session) {
        ModelAndView model = new ModelAndView();

        ActivityQueryCondition condition = null;

        if (session.getAttribute("activities_queryCondition") != null) {
            condition = (ActivityQueryCondition) session.getAttribute("activities_queryCondition");
        } else {
            condition = new ActivityQueryCondition();
        }

        List<Activity> list = activityService.getActivityList(condition);

        int total = activityService.getActivityCount(condition);
        Map<String, Object> pageMap = new HashMap<String, Object>();
        pageMap.put("index", condition.getPage());
        pageMap.put("size", condition.getSize());
        pageMap.put("total", total);

        model.getModel().put("pageMap", pageMap);
        model.getModel().put("activities", list);
        model.setViewName("activities");

        return model;
    }

    @GetMapping("/delactivity")
    public String delActivity(@RequestParam Integer activityId) {
        int result = 0;
        String redirect = "";

        try {
            result = activityService.delActivity(activityId);
        } catch (Exception e) {
            redirect = "redirect:activities";
        }

        if (result <= 0) redirect = "redirect:error";

        return "redirect:activities";
    }


    @PostMapping("/activities/filter")
    public String filterActivities(ActivityQueryCondition condition, BindingResult errors, String filterJson, HttpSession session) {
        session.setAttribute("activities_queryCondition", condition);
        session.setAttribute("activities_filterJson", filterJson);

        return "redirect:/activities";
    }

    @PostMapping("/activities/reset")
    public String resetActivities(HttpSession session) {

        Enumeration<String> names = session.getAttributeNames();

        while (names.hasMoreElements()) {
            String name = names.nextElement();

            if (name.startsWith("activities_")) {
                session.removeAttribute(name);
            }
        }

        return "redirect:/activities";
    }

    @PostMapping("/activityLogs/filter")
    public String filterActivityLogs(String filterJson, ActivityLogQueryCondition condition, BindingResult errors, HttpSession session) {

        if (errors.hasErrors()) {
            return "redirect:error";
        }

        session.setAttribute("activityLogs_filter", filterJson);
        session.setAttribute("activityLogs_condition", condition);

        return "redirect:/activityLogs";
    }

    @GetMapping("/activityLogs")
    public ModelAndView getActivityLogs(HttpSession session) {
        ActivityLogQueryCondition condition = (ActivityLogQueryCondition) session.getAttribute("activityLogs_condition");

        if (condition == null) {
            condition = new ActivityLogQueryCondition();
        }
        condition.setMallId(settingsProperties.getMallId());

        List<ActivitySignUpLog> list = activityService.getActivityLogList(condition);

        int total = activityService.getActivityLogCount(condition);
        HashMap<String, Object> pageMap = new HashMap<>();
        pageMap.put("index", condition.getPage());
        pageMap.put("size", condition.getSize());
        pageMap.put("total", total);

        ModelAndView model = new ModelAndView();
        model.getModel().put("logs", list);
        model.getModel().put("pageMap", pageMap);
        model.setViewName("activitylogs");

        return model;
    }
}
