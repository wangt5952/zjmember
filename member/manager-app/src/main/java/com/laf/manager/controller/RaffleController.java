package com.laf.manager.controller;

import com.laf.manager.SettingsProperties;
import com.laf.manager.core.exception.MallDBException;
import com.laf.manager.dto.RaffleInfo;
import com.laf.manager.dto.RaffleLog;
import com.laf.manager.dto.RaffleReward;
import com.laf.manager.dto.ReceiveCouponInfo;
import com.laf.manager.enums.CouponType;
import com.laf.manager.querycondition.coupon.CouponQueryCondition;
import com.laf.manager.querycondition.giftcoupon.GiftCouponQueryCondition;
import com.laf.manager.querycondition.raffle.RaffleEditCondition;
import com.laf.manager.querycondition.raffle.RaffleFilterCondition;
import com.laf.manager.querycondition.raffle.RaffleFilterCondition1;
import com.laf.manager.querycondition.raffle.RaffleQueryCondition;
import com.laf.manager.service.CouponService;
import com.laf.manager.service.GiftCouponService;
import com.laf.manager.service.RaffleService;
import com.laf.manager.service.RaffleServiceJava;
import com.laf.manager.translator.RaffleTranslator;
import com.laf.manager.utils.file.FileProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import sun.security.x509.RFC822Name;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
public class RaffleController {

    @Autowired
    private RaffleService raffleService;

    @Autowired
    private RaffleServiceJava raffleServiceJava;

    @Autowired
    private CouponService couponService;

    @Autowired
    private GiftCouponService giftCouponService;

    @Autowired
    private SettingsProperties settingsProperties;

    @Autowired
    private FileProperties fileProperties;

    @Autowired
    private RaffleTranslator raffleTranslator;

    @GetMapping("/raffles")
    public ModelAndView getRaffles(HttpSession session) {
        RaffleQueryCondition condition;

        if (session.getAttribute("raffle_condition") != null) {
            condition = (RaffleQueryCondition) session.getAttribute("raffle_condition");
        } else {
            condition = new RaffleQueryCondition();
        }
        condition.setMallId(settingsProperties.getMallId());
        List<Map<String, Object>> list = raffleServiceJava.getRaffleList(condition);

        int total = raffleServiceJava.getRaffleCount(condition);
        HashMap<String, Object> pageMap = new HashMap<>();
        pageMap.put("index", condition.getPage());
        pageMap.put("size", condition.getSize());
        pageMap.put("total", total);

        ModelAndView model = new ModelAndView();
        model.getModel().put("raffles", list);
//        model.getModel().put("pageMap", pageMap);
        model.setViewName("raffles");

        return model;
    }

    @PostMapping("/raffles/filter")
    public String raffleFilter(String jsonFilter, RaffleQueryCondition condition, BindingResult errors, HttpSession session) {
        session.setAttribute("raffle_filter", jsonFilter);
        session.setAttribute("raffle_condition", condition);

        return "redirect:/raffles";
    }

    @GetMapping("/raffle")
    public ModelAndView getRaffle(@RequestParam(required = false) Integer raffleId) {

        int id = raffleId == null ? 0 : raffleId.intValue();
        ModelAndView modelAndView = new ModelAndView();

        RaffleInfo raffleInfo = null;

        if (id > 0) {
            raffleInfo = raffleService.getRaffleInfoById(id);
            // TODO: 2018/3/30 检查有无抽奖记录

        }

        CouponQueryCondition condition = new CouponQueryCondition();
        condition.setCouponType(CouponType.ASSOCIATION.value());
        List<ReceiveCouponInfo> couponInfo = couponService.getCouponInfoListByType(condition);
        String json = couponService.associationCoupons2Json(couponInfo);

        GiftCouponQueryCondition gCondition = new GiftCouponQueryCondition();
        List<ReceiveCouponInfo> giftCouponInfo = giftCouponService.getGiftCouponInfoListForRaffle(gCondition);
        String gJson = giftCouponService.associationCoupons2Json(giftCouponInfo);

        boolean edit_flag = raffleInfo != null;

        if (raffleInfo != null) {
            String base_image_url = fileProperties.getDomain();

            modelAndView.getModel().put("base_image_url", base_image_url);
            modelAndView.getModel().put("raffle_rewards", raffleInfo.getRewards());
            modelAndView.getModel().put("raffle", raffleInfo);
        }

        modelAndView.getModel().put("coupons", json);
        modelAndView.getModel().put("giftcoupons", gJson);
        modelAndView.getModel().put("edit_flag", edit_flag);
        modelAndView.setViewName("raffle");

        return modelAndView;
    }

    @PostMapping("/saveRaffle")
    public String editRaffle(HttpServletRequest request, RaffleEditCondition condition, BindingResult errors) throws IOException {

        if (errors.hasErrors()) {
            return "redirect:error";
        }

        List<String> pictures = new ArrayList<>();

        if (request instanceof MultipartHttpServletRequest) {
            List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");

            for (MultipartFile file : files) {
                if (file == null || !file.getName().equals("file")) continue;

                String uploadFileName = file.getOriginalFilename();

                if (StringUtils.isEmpty(uploadFileName)) {
                    pictures.add(condition.getPictureName());
                    continue;
                }

                String suffixName = uploadFileName.substring(uploadFileName.lastIndexOf("."));

                String folder = fileProperties.getPath();
                String fileName = "raffle_reward_" + new Date().getTime() + suffixName;

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                File localFile = new File(folder, fileName);
                file.transferTo(localFile);
                String url = fileProperties.getBase() + fileName;
                pictures.add(url);
            }
        }

        condition.setPictures(pictures);
        RaffleInfo raffleInfo = raffleTranslator.condition2Info(condition);

        int result;
        try {
            result = raffleService.saveRaffleInfo(raffleInfo);
        } catch (MallDBException e) {
            result = e.getResult();
        }

        if (result <= 0) return "redirect:error";

//        return "redirect:/raffles";
        return "redirect:/raffles";
    }

    @PostMapping("/raffleLogs/filter")
    public String raffleLogFilter(String filterJson, RaffleFilterCondition1 condition, BindingResult errors, HttpSession session) {

        session.setAttribute("rafflelog_filter", filterJson);
        session.setAttribute("rafflelog_condition", condition);

        return "redirect:/raffleLogs";
    }

    @PostMapping("/raffleLogs/reset")
    public String raffleLogReset(HttpSession session) {
        Enumeration<String> names = session.getAttributeNames();

        while (names.hasMoreElements()) {
            String name = names.nextElement();

            if (name.startsWith("rafflelog_")) {
                session.removeAttribute(name);
            }
        }

        return "redirect:/raffleLogs";
    }

    @GetMapping("/raffleLogs")
    public ModelAndView getRaffleLogs(HttpSession session) {
        RaffleFilterCondition condition;

        if (session.getAttribute("rafflelog_condition") != null) {
            RaffleFilterCondition1 condition1 = (RaffleFilterCondition1) session.getAttribute("rafflelog_condition");
            condition = new RaffleFilterCondition();
            condition.setTitle(condition1.getTitle());
            condition.setMobile(condition1.getMobile());
            condition.setUsername(condition1.getUsername());
            condition.setActionTimeStart(condition1.getActionTimeStart());
            condition.setActionTimeEnd(condition1.getActionTimeEnd());
            condition.setWin(condition1.getIsWin());
            condition.setMallId(condition1.getMallId());
            condition.setSize(condition1.getSize());
            condition.setPage(condition1.getPage());
        } else {
            condition = new RaffleFilterCondition();
        }

        List<RaffleLog> list = raffleService.multipleGetRaffleLogs(condition);

        int total = raffleService.multipleGetRaffleLogsCount(condition);
        HashMap<String, Object> pageMap = new HashMap<>();
        pageMap.put("index", condition.getPage());
        pageMap.put("size", condition.getSize());
        pageMap.put("total", total);

       ModelAndView model = new ModelAndView();
        model.getModel().put("logs", list);
        model.getModel().put("pageMap", pageMap);
        model.setViewName("rafflelogs");

        return model;
    }

}
