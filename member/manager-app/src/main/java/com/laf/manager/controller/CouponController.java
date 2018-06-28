package com.laf.manager.controller;

import com.laf.manager.SettingsProperties;
import com.laf.manager.dao.CouponDao;
import com.laf.manager.dto.*;
import com.laf.manager.querycondition.coupon.CouponLogFilterCondition;
import com.laf.manager.querycondition.coupon.CouponQueryCondition;
import com.laf.manager.querycondition.coupon.ReceiveCouponEditCondition;
import com.laf.manager.service.CouponService;
import com.laf.manager.service.ShopService;
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

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Controller
@Slf4j
public class CouponController {

    @Autowired
    CouponService couponService;

    @Autowired
    CouponDao couponDao;

    @Autowired
    SettingsProperties settingsProperties;

    @Autowired
    ShopService shopService;

    @Autowired
    FileProperties fileProperties;

    @GetMapping("/coupon")
    public ModelAndView getCoupon(@RequestParam(required = false) Integer couponId, @RequestParam(required = false) Integer couponType) {
        ModelAndView model = new ModelAndView();
        ReceiveCouponInfo coupon;

        if (couponId != null) {
            coupon = couponService.getReceiveCoupon(couponId);
            if(coupon == null) return new ModelAndView("404");

            model.getModel().put("coupon", coupon);

        } else {
            coupon = new ReceiveCouponInfo();
            coupon.setCoupon_type(couponType == null ? 0 : couponType);
        }

        List<Shop> shops = shopService.getShopAllList();

        model.getModel().put("shops", shops);
        model.getModel().put("verificationTypes", coupon.getVerificationTypes());

        switch(coupon.getCoupon_type()) {
            case 0:
                model.setViewName("couponreceive");
                break;

            case 1:
                model.setViewName("couponactive");
                break;

            case 2:
                model.setViewName("couponpush");
                break;

            case 3:
                model.setViewName("couponrelate");
                break;
        }
        return model;
    }

    @GetMapping("/parkingCoupon")
    public ModelAndView getParkingCoupon(@RequestParam(required = false) Integer couponId) {
        ModelAndView model = new ModelAndView();
        ReceiveCouponInfo coupon = null;

        if (couponId != null) {
            coupon = couponService.getParkingCoupon(couponId);
            if (coupon == null) return new ModelAndView("404");

            model.getModel().put("coupon", coupon);

        } else {
            coupon = new ReceiveCouponInfo();
        }
        model.setViewName("parkingcoupon");
        return model;
    }

    @GetMapping("/parking")
    public ModelAndView getParking(@RequestParam(required = false) Integer couponId) {
        ModelAndView model = new ModelAndView();
        ReceiveCouponInfo coupon = null;

        if (couponId != null) {
            coupon = couponService.getParkingCoupon(couponId);
            if (coupon == null) return new ModelAndView("404");

            model.getModel().put("coupon", coupon);

        } else {
            coupon = new ReceiveCouponInfo();
        }
        model.setViewName("parking");
        return model;
    }

//    @PostMapping("/receiveCoupons")
//    public ModelAndView getReceiveCouponsForPost(@RequestParam(required = false) String pageJson) {
//        ModelAndView model = new ModelAndView();
//
//        CouponQueryCondition condition = new CouponQueryCondition();
//        condition.setMallId(settingsProperties.getMallId());
//        condition.setCouponType(0);
//        //分页
//
////        int total = couponService.getCouponsCount();
//        int total = 0;
//
//        Map<String, Object> pageMap = null;
//
//        if (!StringUtils.isEmpty(pageJson)) {
//            /**
//             * {"index":1, "size":10,"total":100}
//             */
//            pageMap = (Map<String, Object>) JsonPath.parse(pageJson).read("$", Map.class);
//            int page = Integer.valueOf(pageMap.get("index").toString());
//            int size = Integer.valueOf(pageMap.get("size").toString());
//            condition.setPage(page);
//            condition.setSize(size);
//
//            pageMap.put("total", total);
//        } else { // default page info
//            pageMap = new HashMap<String, Object>();
//            pageMap.put("index", 1);
//            pageMap.put("size", 10);
//            pageMap.put("total", total);
//        }
//
//        List<ReceiveCouponInfo> list = couponService.getCouponInfoList(condition);
//        model.getModel().put("pageMap", pageMap);
//        model.getModel().put("receiveCoupons", list);
//        model.setViewName("receivecoupons");
//        return model;
//    }

    @GetMapping("/coupons")
    public ModelAndView getCoupons(HttpSession session) {
        ModelAndView model = new ModelAndView();
        CouponQueryCondition condition;

        if (session.getAttribute("coupons_querycondition") != null) {
            condition = (CouponQueryCondition) session.getAttribute("coupons_querycondition");
        } else {
            condition = new CouponQueryCondition();
        }

        condition.setMallId(settingsProperties.getMallId());
        List<ReceiveCouponInfo> list = couponService.getCouponInfoList(condition);

        int total = couponService.getCouponsCount(condition);

        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("index", condition.getPage());
        pageMap.put("size", condition.getSize());
        pageMap.put("total", total);

        model.getModel().put("coupons", list);
        model.getModel().put("pageMap", pageMap);
        model.setViewName("coupons");

        return model;
    }

    @GetMapping("/parkingCoupons")
    public ModelAndView getParkingCoupons(HttpSession session) {
        ModelAndView model = new ModelAndView();
        CouponQueryCondition condition;

        if (session.getAttribute("coupons_querycondition") != null) {
            condition = (CouponQueryCondition) session.getAttribute("coupons_querycondition");
        } else {
            condition = new CouponQueryCondition();
        }

        condition.setMallId(settingsProperties.getMallId());
        List<ReceiveCouponInfo> list = couponService.getParkingCouponInfoList(condition);

        int total = couponService.getParkingCouponsCount(condition);

        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("index", condition.getPage());
        pageMap.put("size", condition.getSize());
        pageMap.put("total", total);

        model.getModel().put("coupons", list);
        model.getModel().put("pageMap", pageMap);
        model.setViewName("parkingcoupons");

        return model;
    }

    @PostMapping("/coupons/filter")
    public String couponsFilter(CouponQueryCondition condition, String filterJson, BindingResult errors, HttpSession session) {

        session.setAttribute("coupons_querycondition", condition);
        session.setAttribute("coupons_filter", filterJson);

        return "redirect:/coupons";
    }

    @PostMapping("/coupons/reset")
    public String resetCoupons(HttpSession session) {
        Enumeration<String> names = session.getAttributeNames();

        while (names.hasMoreElements()) {
            String name = names.nextElement();

            if (name.startsWith("coupons_")) {
                session.removeAttribute(name);
            }
        }

        return "redirect:/coupons";
    }

    @PostMapping("/saveCoupon")
    public String editCoupon(MultipartFile file, ReceiveCouponEditCondition condition, BindingResult errors) throws IOException {

        if (errors.hasErrors()) {
            return "redirect:error";
        }

        String picture = "";

        if (file != null && file.getName().equals("file")) {
            String uploadFileName = file.getOriginalFilename();

            if (!StringUtils.isEmpty(uploadFileName)) {

                String suffixName = uploadFileName.substring(uploadFileName.lastIndexOf("."));
                String folder = fileProperties.getPath();
                String fileName = "coupon_" + new Date().getTime() + suffixName;

                File localFile = new File(folder, fileName);
                file.transferTo(localFile);
                String url = fileProperties.getDomain() + fileProperties.getBase() + fileName;
                picture = url;
            }
        }

        condition.setPicture(picture);
        couponService.editCoupon(condition);

        return "redirect:coupons";
    }

    @PostMapping("/saveParkingCoupon")
    public String editParkingCoupon(MultipartFile file, ReceiveCouponEditCondition condition, BindingResult errors) throws IOException {

        if (errors.hasErrors()) {
            return "redirect:error";
        }

        String picture = "";

        if (file != null && file.getName().equals("file")) {
            String uploadFileName = file.getOriginalFilename();

            if (!StringUtils.isEmpty(uploadFileName)) {

                String suffixName = uploadFileName.substring(uploadFileName.lastIndexOf("."));
                String folder = fileProperties.getPath();
                String fileName = "parkingcoupon_" + new Date().getTime() + suffixName;

                File localFile = new File(folder, fileName);
                file.transferTo(localFile);
                String url = fileProperties.getDomain() + fileProperties.getBase() + fileName;
                picture = url;
            }
        }

        condition.setPicture(picture);
        couponService.editParkingCoupon(condition);

        return "redirect:/parkingCoupons";
    }

    @GetMapping("/getParkingInfo")
    public ModelAndView getParkingInfo()  {
        ModelAndView model = new ModelAndView();
        ParkingInfo info = couponService.getParkingInfo();
        model.getModel().put("parkingInfo", info);
        model.setViewName("parking");
        return model;
    }

    @PostMapping("/saveParkingInfo")
    public String saveParkingInfo(ParkingInfo intro, BindingResult errors)  {

        if (errors.hasErrors()) {
            return "redirect:error";
        }
        couponService.saveParkingInfo(intro.getIntro());
        return "redirect:/parkingCoupons";
    }

    @GetMapping("/delReceiveCoupon")
    public String delReceiveCoupon(@RequestParam Integer couponId) {
        if (couponId <= 0) return "redirect:404";
        int result = couponService.delReceiveCoupon(couponId);
        if (result <= 0) return "redirect:404";
        return "redirect:coupons";
    }

    @GetMapping("/delCoupon")
    public String delCoupon(@RequestParam Integer couponId) {
        if (couponId <= 0) return "redirect:404";
        int result = couponService.delCoupon(couponId);
        if (result <= 0) return "redirect:404";
        return "redirect:coupons";
    }


    @GetMapping("/receiveLogs")
    public ModelAndView receivedLogs(HttpSession session) {
        ModelAndView model = new ModelAndView();
        CouponLogFilterCondition condition = null;

        if (session.getAttribute("receiveLogs_queryCondition") != null) {
            condition = (CouponLogFilterCondition) session.getAttribute("receiveLogs_queryCondition");
        } else {
            condition = new CouponLogFilterCondition();
        }

        int total = couponService.getReceivedLogCount(condition);
        List<Coupon> list = couponService.getReceivedLogList(condition);

        Map<String, Object> pageMap = new HashMap<String, Object>();
        pageMap.put("index", condition.getPage());
        pageMap.put("size", condition.getSize());
        pageMap.put("total", total);

        model.getModel().put("pageMap", pageMap);
        model.getModel().put("logs", list);
        model.setViewName("receivedlogs");

        return model;
    }

    @PostMapping("/receiveLogs/filter")
    public String receiveLogsFilter(CouponLogFilterCondition condition, BindingResult errors, String filterJson, HttpSession session) {

        session.setAttribute("receiveLogs_queryCondition", condition);
        session.setAttribute("receiveLogs_filterJson", filterJson);

        return "redirect:/receiveLogs";
    }

    @PostMapping("/receiveLogs/reset")
    public String resetReceiveLogs(HttpSession session) {

        Enumeration<String> names = session.getAttributeNames();

        while (names.hasMoreElements()) {
            String name = names.nextElement();

            if (name.startsWith("receiveLogs_")) {
                session.removeAttribute(name);
            }
        }

        return "redirect:/receiveLogs";
    }

    @GetMapping("/verificationLogs")
    public ModelAndView verificationLogs(HttpSession session) {
        ModelAndView model = new ModelAndView();
        CouponLogFilterCondition condition = null;

        if (session.getAttribute("verificationLogs_queryCondition") != null) {
            condition = (CouponLogFilterCondition) session.getAttribute("verificationLogs_queryCondition");
        } else {
            condition = new CouponLogFilterCondition();
        }

        int total = couponService.getVerificationLogCount(condition);
        List<Coupon> list = couponService.getVerificationLogList(condition);

        Map<String, Object> pageMap = new HashMap<String, Object>();
        pageMap.put("index", condition.getPage());
        pageMap.put("size", condition.getSize());
        pageMap.put("total", total);

        model.getModel().put("pageMap", pageMap);
        model.getModel().put("logs", list);
        model.setViewName("verificationlogs");

        return model;
    }

    @PostMapping("/verificationLogs/filter")
    public String verificationLogsFilter(CouponLogFilterCondition condition, BindingResult errors, String filterJson, HttpSession session) {

        session.setAttribute("verificationLogs_queryCondition", condition);
        session.setAttribute("verificationLogs_filterJson", filterJson);

        return "redirect:/verificationLogs";
    }

    @PostMapping("/verificationLogs/reset")
    public String resetVerificationLogs(HttpSession session) {

        Enumeration<String> names = session.getAttributeNames();

        while (names.hasMoreElements()) {
            String name = names.nextElement();

            if (name.startsWith("verificationLogs_")) {
                session.removeAttribute(name);
            }
        }

        return "redirect:/verificationLogs";
    }

    @GetMapping("/couponLogs")
    public ModelAndView couponLogs(HttpSession session) {
        ModelAndView model = new ModelAndView();
        CouponLogFilterCondition condition = null;
//session.removeAttribute("couponLogs_queryCondition");session.removeAttribute("couponLogs_filterJson");
        if (session.getAttribute("couponLogs_queryCondition") != null) {
            condition = (CouponLogFilterCondition) session.getAttribute("couponLogs_queryCondition");
        } else {
            condition = new CouponLogFilterCondition();
        }

        BigDecimal costPriceSum = couponService.getCouponLogsSum(condition);
        int total = couponService.getCouponLogsCount(condition);
        List<Coupon> list = couponService.getCouponLogsList(condition);

        Map<String, Object> pageMap = new HashMap<String, Object>();
        pageMap.put("index", condition.getPage());
        pageMap.put("size", condition.getSize());
        pageMap.put("total", total);

        model.getModel().put("costPriceSum", costPriceSum);
        model.getModel().put("pageMap", pageMap);
        model.getModel().put("logs", list);
        model.setViewName("couponlogs");

        return model;
    }

    @GetMapping("parkingCouponLogs")
    public ModelAndView parkingCouponLogs(HttpSession session) {
        ModelAndView model = new ModelAndView();
        CouponLogFilterCondition condition = null;
        if (session.getAttribute("parkingCouponLogs_queryCondition") != null) {
            condition = (CouponLogFilterCondition) session.getAttribute("parkingCouponLogs_queryCondition");
        } else {
            condition = new CouponLogFilterCondition();
        }

        BigDecimal costPriceSum = couponService.getParkingCouponLogsSum(condition);
        int total = couponService.getParkingCouponLogsCount(condition);
        List<ParkingCouponInfo> list = couponService.getParkingCouponLogsList(condition);

        Map<String, Object> pageMap = new HashMap<String, Object>();
        pageMap.put("index", condition.getPage());
        pageMap.put("size", condition.getSize());
        pageMap.put("total", total);

        model.getModel().put("costPriceSum", costPriceSum);
        model.getModel().put("pageMap", pageMap);
        model.getModel().put("logs", list);
        model.setViewName("parkingcouponlogs");

        return model;
    }

    @GetMapping("parkingCouponPayLogs")
    public ModelAndView parkingCouponPayLogs(HttpSession session) {
        ModelAndView model = new ModelAndView();
        CouponLogFilterCondition condition = null;
        if (session.getAttribute("parkingCouponPayLogs_queryCondition") != null) {
            condition = (CouponLogFilterCondition) session.getAttribute("parkingCouponPayLogs_queryCondition");
        } else {
            condition = new CouponLogFilterCondition();
        }

        //BigDecimal costPriceSum = couponService.getParkingCouponLogsSum(condition);
        int total = couponService.getParkingCouponPayLogsCount(condition);
        List<ParkingCouponInfo> list = couponService.getParkingCouponPayLogsList(condition);

        Map<String, Object> pageMap = new HashMap<String, Object>();
        pageMap.put("index", condition.getPage());
        pageMap.put("size", condition.getSize());
        pageMap.put("total", total);

       // model.getModel().put("costPriceSum", costPriceSum);
        model.getModel().put("pageMap", pageMap);
        model.getModel().put("logs", list);
        model.setViewName("parkingcouponpaylogs");

        return model;
    }

    @PostMapping("/couponLogs/filter")
    public String filterReceiveCoupons(CouponLogFilterCondition condition, BindingResult errors, String filterJson, HttpSession session) {

        session.setAttribute("couponLogs_queryCondition", condition);
        session.setAttribute("couponLogs_filterJson", filterJson);

        return "redirect:/couponLogs";
    }

    @PostMapping("/parkingCouponLogs/filter")
    public String filterParkingCoupons(CouponLogFilterCondition condition, BindingResult errors, String filterJson, HttpSession session) {

        session.setAttribute("parkingCouponLogs_queryCondition", condition);
        session.setAttribute("parkingCouponLogs_filterJson", filterJson);

        return "redirect:/parkingCouponLogs";
    }

    @PostMapping("/parkingCouponPayLogs/filter")
    public String filterParkingCouponsPay(CouponLogFilterCondition condition, BindingResult errors, String filterJson, HttpSession session) {

        session.setAttribute("parkingCouponPayLogs_queryCondition", condition);
        session.setAttribute("parkingCouponPayLogs_filterJson", filterJson);

        return "redirect:/parkingCouponPayLogs";
    }

    @PostMapping("/couponLogs/reset")
    public String resetReceiveCoupons(HttpSession session) {

        Enumeration<String> names = session.getAttributeNames();

        while (names.hasMoreElements()) {
            String name = names.nextElement();

            if (name.startsWith("couponLogs_")) {
                session.removeAttribute(name);
            }
        }

        return "redirect:/couponLogs";
    }

    @PostMapping("/parkingCouponLogs/reset")
    public String resetParkingCoupons(HttpSession session) {

        Enumeration<String> names = session.getAttributeNames();

        while (names.hasMoreElements()) {
            String name = names.nextElement();

            if (name.startsWith("parkingCouponLogs_")) {
                session.removeAttribute(name);
            }
        }

        return "redirect:/parkingCouponLogs";
    }

    @PostMapping("/parkingCouponPayLogs/reset")
    public String resetParkingCouponsPay(HttpSession session) {

        Enumeration<String> names = session.getAttributeNames();

        while (names.hasMoreElements()) {
            String name = names.nextElement();

            if (name.startsWith("parkingCouponPayLogs_")) {
                session.removeAttribute(name);
            }
        }

        return "redirect:/parkingCouponPayLogs";
    }

    @GetMapping("/export_coupon_logs")
    public void exportMembers(HttpServletResponse response, HttpSession session) throws IOException {
        String mimeType = "application/octet-stream";
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", settingsProperties.getExcel());
        response.setHeader(headerKey, headerValue);
        response.setContentType(mimeType);

        CouponLogFilterCondition condition = null;

        if (session.getAttribute("couponLogs_queryCondition") != null) {
            condition = (CouponLogFilterCondition) session.getAttribute("couponLogs_queryCondition");
        } else {
            condition = new CouponLogFilterCondition();
        }

        condition.setSize(3000000); // excel 最大支持300万行
        List<Coupon> list = couponService.getCouponLogsList(condition);
        couponService.print2Excel(list, response.getOutputStream());
    }

    @GetMapping("/export_parkingCoupon_logs")
    public void exportParkingMembers(HttpServletResponse response, HttpSession session) throws IOException {
        String mimeType = "application/octet-stream";
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", settingsProperties.getExcel());
        response.setHeader(headerKey, headerValue);
        response.setContentType(mimeType);

        CouponLogFilterCondition condition = null;

        if (session.getAttribute("couponLogs_queryCondition") != null) {
            condition = (CouponLogFilterCondition) session.getAttribute("couponLogs_queryCondition");
        } else {
            condition = new CouponLogFilterCondition();
        }

        condition.setSize(3000000); // excel 最大支持300万行
        List<ParkingCouponInfo> list = couponService.getParkingCouponLogsList(condition);
        couponService.print2ExcelParking(list, response.getOutputStream());
    }
}
