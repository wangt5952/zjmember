package com.laf.manager.controller;

import com.laf.manager.SettingsProperties;
import com.laf.manager.core.exception.MallDBException;
import com.laf.manager.dto.PlaneMap;
import com.laf.manager.dto.Points;
import com.laf.manager.querycondition.points.PointsLogFilterCondition;
import com.laf.manager.querycondition.points.PointsManualCondition;
import com.laf.manager.service.PointsService;
import com.laf.manager.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class PointsController {

    @Autowired
    PointsService pointsService;

    @Autowired
    ShopService shopService;

    @Autowired
    SettingsProperties settingsProperties;

    @GetMapping("/pointsDetail")
    public ModelAndView getPointsDetail(HttpSession session) {

        ModelAndView model = new ModelAndView();
        PointsLogFilterCondition condition = null;

        if (session.getAttribute("points_queryCondition") != null) {
            condition = (PointsLogFilterCondition) session.getAttribute("points_queryCondition");
        } else {
            condition = new PointsLogFilterCondition();
        }

//        int pointsSum = pointsService.getPointsSum(condition);
//        model.getModel().put("pointsSum", pointsSum);

        int total = pointsService.getLogsCount(condition);

        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("index", condition.getPage());
        pageMap.put("size", condition.getSize());
        pageMap.put("total", total);
        model.getModel().put("pageMap", pageMap);

        List<Points> list = pointsService.getPointsList(condition);
        model.getModel().put("points", list);

        List<PlaneMap> maps = shopService.getShopsGroupByMap();
        model.getModel().put("maps", maps);

        model.setViewName("points");

        return model;
    }

    @PostMapping("/points/filter")
    public String filterPoints(PointsLogFilterCondition condition, BindingResult errors, String filterJson, HttpSession session) {

        session.setAttribute("points_queryCondition", condition);
        session.setAttribute("points_filterJson", filterJson);

        return "redirect:/pointsDetail";
    }

    @PostMapping("/points/reset")
    public String resetPoints(HttpSession session) {

        Enumeration<String> names = session.getAttributeNames();

        while (names.hasMoreElements()) {
            String name = names.nextElement();

            if (name.startsWith("points_")) {
                session.removeAttribute(name);
            }
        }

        return "redirect:/pointsDetail";
    }

    @GetMapping("/manualPoints")
    public ModelAndView getHandlePoints() {
        ModelAndView model = new ModelAndView();

        model.setViewName("manualpoints");
        return model;
    }

    @PostMapping("/saveManualPoints")
    public String manualPoints(PointsManualCondition condition, BindingResult errors) {

        if (errors.hasErrors()) {
            return "redirect:error";
        }

        int result = 0;

        try {
            result = pointsService.manual(condition);
        } catch (MallDBException e) {
            result = e.getResult();
        }

        if (result > 0) {
            return "redirect:/pointsDetail";
        } else {
            return "redirect:error";
        }
    }

    // saveManualPoints


    @GetMapping("/export_points")
    public void exportMembers(HttpServletResponse response, HttpSession session) throws IOException {
        String mimeType = "application/octet-stream";
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", settingsProperties.getExcel());
        response.setHeader(headerKey, headerValue);
        response.setContentType(mimeType);

        PointsLogFilterCondition condition = null;

        if (session.getAttribute("members_queryCondition") != null) {
            condition = (PointsLogFilterCondition) session.getAttribute("members_queryCondition");

        } else {
            condition = new PointsLogFilterCondition();
        }

        condition.setSize(3000000); // excel 最大支持300万行
        List<Points> list = pointsService.getPointsList(condition);
        pointsService.print2Excel(list, response.getOutputStream());
    }
}
