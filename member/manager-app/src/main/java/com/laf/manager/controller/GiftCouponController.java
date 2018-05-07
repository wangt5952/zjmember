package com.laf.manager.controller;

import com.jayway.jsonpath.JsonPath;
import com.laf.manager.SettingsProperties;
import com.laf.manager.dto.Coupon;
import com.laf.manager.dto.ReceiveCouponInfo;
import com.laf.manager.dto.Shop;
import com.laf.manager.querycondition.coupon.CouponLogFilterCondition;
import com.laf.manager.querycondition.giftcoupon.GiftCouponEditCondition;
import com.laf.manager.querycondition.giftcoupon.GiftCouponQueryCondition;
import com.laf.manager.service.GiftCouponService;
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
public class GiftCouponController {

    @Autowired
    GiftCouponService giftCouponService;

    @Autowired
    SettingsProperties settingsProperties;

    @Autowired
    ShopService shopService;

    @Autowired
    FileProperties fileProperties;

    @GetMapping("/giftCoupons")
    public ModelAndView getGiftCoupons(GiftCouponQueryCondition condition) {
        ModelAndView model = new ModelAndView();

        condition.setMallId(settingsProperties.getMallId());
        List<ReceiveCouponInfo> list = giftCouponService.getGiftCouponInfoList(condition);

        int total = giftCouponService.getGiftCouponsCount(condition);

        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("index", condition.getPage());
        pageMap.put("size", condition.getSize());
        pageMap.put("total", total);

        model.getModel().put("gCoupons", list);
        model.getModel().put("pageMap", pageMap);
        model.setViewName("giftcoupons");

        return model;
    }

    @PostMapping("/giftCoupons")
    public ModelAndView getGiftCoupons(String pageJson, BindingResult errors) {
        Map<String, Object> pageMap = (Map<String, Object>) JsonPath.parse(pageJson).read("$", Map.class);

        GiftCouponQueryCondition condition = new GiftCouponQueryCondition();
        condition.setMallId(settingsProperties.getMallId());
        condition.setPage((Integer) pageMap.get("index"));
        condition.setSize((Integer) pageMap.get("size"));
        List<ReceiveCouponInfo> list = giftCouponService.getGiftCouponInfoList(condition);

        int total = giftCouponService.getGiftCouponsCount(condition);
        pageMap.put("total", total);

        ModelAndView model = new ModelAndView();
        model.getModel().put("gCoupons", list);
        model.getModel().put("pageMap", pageMap);
        model.setViewName("giftcoupons");

        return model;
    }

//    @PostMapping("/giftCoupons/filter")
//    public String giftCouponsFilter(GiftCouponQueryCondition condition, BindingResult errors) {
//
////        session.setAttribute("giftcoupons_querycondition", condition);
////        session.setAttribute("giftcoupons_filter", filterJson);
//
//        return "redirect:/giftCoupons";
//    }

    @PostMapping("/giftCoupons/reset")
    public String resetGiftCoupons(HttpSession session) {
        Enumeration<String> names = session.getAttributeNames();

        while (names.hasMoreElements()) {
            String name = names.nextElement();

            if (name.startsWith("giftcoupons_")) {
                session.removeAttribute(name);
            }
        }

        return "redirect:/giftCoupons";
    }

    @GetMapping("/giftCoupon")
    public ModelAndView getGiftCoupon(@RequestParam(required = false) Integer couponId) {
        ModelAndView model = new ModelAndView();
        ReceiveCouponInfo coupon;

        if (couponId != null) {
            coupon = giftCouponService.getGiftCouponInfoById(couponId);
            if (coupon == null) return new ModelAndView("404");

            model.getModel().put("coupon", coupon);

        } else {
            coupon = new ReceiveCouponInfo();
        }

        List<Shop> shops = shopService.getShopAllList();

        model.getModel().put("shops", shops);
        model.getModel().put("verificationTypes", coupon.getVerificationTypes());

        model.setViewName("giftcoupon");
        return model;
    }

    @PostMapping("/saveGiftCoupon")
    public String editGiftCoupon(MultipartFile file, GiftCouponEditCondition condition, BindingResult errors) throws IOException {

        if (errors.hasErrors()) {
            return "redirect:error";
        }

        String picture = "";

        if (file != null && file.getName().equals("file")) {
            String uploadFileName = file.getOriginalFilename();

            if (!StringUtils.isEmpty(uploadFileName)) {

                String suffixName = uploadFileName.substring(uploadFileName.lastIndexOf("."));
                String folder = fileProperties.getPath();
                String fileName = "giftcoupon_" + new Date().getTime() + suffixName;

                File localFile = new File(folder, fileName);
                file.transferTo(localFile);
                String url = fileProperties.getDomain() + fileProperties.getBase() + fileName;
                picture = url;
            }
        }

        condition.setPicture(picture);
        giftCouponService.editGiftCouponInfo(condition);

        return "redirect:/giftCoupons";
    }

    @GetMapping("/giftCouponLogs")
    public ModelAndView giftCouponLogs(HttpSession session) {
        ModelAndView model = new ModelAndView();
        CouponLogFilterCondition condition = null;

        if (session.getAttribute("giftCouponLogs_queryCondition") != null) {
            condition = (CouponLogFilterCondition) session.getAttribute("giftCouponLogs_queryCondition");
        } else {
            condition = new CouponLogFilterCondition();
        }

        BigDecimal costPriceSum = giftCouponService.getCouponLogsSum(condition);
        int total = giftCouponService.getCouponLogsCount(condition);
        List<Coupon> list = giftCouponService.getCouponLogsList(condition);

        Map<String, Object> pageMap = new HashMap<String, Object>();
        pageMap.put("index", condition.getPage());
        pageMap.put("size", condition.getSize());
        pageMap.put("total", total);

        model.getModel().put("costPriceSum", costPriceSum);
        model.getModel().put("pageMap", pageMap);
        model.getModel().put("logs", list);
        model.setViewName("giftcouponlogs");

        return model;
    }

    @PostMapping("/giftCouponLogs/filter")
    public String filterGiftCouponLogs(CouponLogFilterCondition condition, BindingResult errors, String filterJson, HttpSession session) {

        session.setAttribute("giftCouponLogs_queryCondition", condition);
        session.setAttribute("giftCouponLogs_filterJson", filterJson);

        return "redirect:/giftCouponLogs";
    }

    @PostMapping("/giftCouponLogs/reset")
    public String resetGiftCouponLogs(HttpSession session) {

        Enumeration<String> names = session.getAttributeNames();

        while (names.hasMoreElements()) {
            String name = names.nextElement();

            if (name.startsWith("giftCouponLogs_")) {
                session.removeAttribute(name);
            }
        }

        return "redirect:/giftCouponLogs";
    }

    @GetMapping("/export_gift_coupon_logs")
    public void exportMembers(HttpServletResponse response, HttpSession session) throws IOException {
        String mimeType = "application/octet-stream";
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", settingsProperties.getExcel());
        response.setHeader(headerKey, headerValue);
        response.setContentType(mimeType);

        CouponLogFilterCondition condition = null;

        if (session.getAttribute("giftCouponLogs_queryCondition") != null) {
            condition = (CouponLogFilterCondition) session.getAttribute("giftCouponLogs_queryCondition");
        } else {
            condition = new CouponLogFilterCondition();
        }

        condition.setSize(3000000); // excel 最大支持300万行
        List<Coupon> list = giftCouponService.getCouponLogsList(condition);
        giftCouponService.print2Excel(list, response.getOutputStream()); // TODO: 2018/4/16 导出
    }
}
