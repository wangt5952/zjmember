package com.laf.manager.controller;


import com.laf.manager.SettingsProperties;
import com.laf.manager.dto.Level;
import com.laf.manager.dto.Member;
import com.laf.manager.dto.ReceiveCouponInfo;
import com.laf.manager.enums.CouponType;
import com.laf.manager.enums.Occupation;
import com.laf.manager.querycondition.member.MemberFilterCondition;
import com.laf.manager.service.CouponService;
import com.laf.manager.service.LevelService;
import com.laf.manager.service.MallService;
import com.laf.manager.service.MemberService;
import com.sun.javafx.binding.StringFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@Slf4j
public class MemberController {

    @Autowired
    MallService mallService;

    @Autowired
    LevelService levelService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private SettingsProperties settingsProperties;

    @Autowired
    private MemberService memberService;

    @GetMapping("/members")
    public ModelAndView getMembers(HttpSession session) {
        ModelAndView model = new ModelAndView();

        MemberFilterCondition condition = null;

        if (session.getAttribute("members_queryCondition") != null) {
            condition = (MemberFilterCondition) session.getAttribute("members_queryCondition");
        } else {
            condition = new MemberFilterCondition();
        }

        BigDecimal amount = mallService.getCumulateAmountSum(condition);
        model.getModel().put("amount", amount);

        int total = mallService.getMembersCount(condition);
        Map<String, Object> pageMap = new HashMap<String, Object>();
        pageMap.put("index", condition.getPage());
        pageMap.put("size", condition.getSize());
        pageMap.put("total", total);

        model.getModel().put("pageMap", pageMap);

        List<Member> list = mallService.getMembers(condition);
        model.getModel().put("members", list);
        model.setViewName("members");

        List<Level> levels = levelService.getAllLevelByMall();
        model.getModel().put("levels", levels);

        List<ReceiveCouponInfo> coupons = couponService.getCouponInfoListFromType(CouponType.PUSH);
        model.getModel().put("coupons", coupons);

        return model;
    }

    @GetMapping("/member")
    public ModelAndView getMember(@RequestParam Integer memberId) {
        ModelAndView model = new ModelAndView();

        if (memberId <= 0) {
            model.setViewName("404");
            return model;
        }

        Member member = mallService.getMemberById(memberId);

        if (member == null) {
            model.setViewName("404");
            return model;
        }

        Map<String, String> memberMap = new HashMap<>();
        memberMap.put("mobile", member.getMobile());
        memberMap.put("name", member.getName());
        memberMap.put("sex", member.getSex() == 0 ? "男" : "女");
        memberMap.put("address", member.getAddress());
        memberMap.put("usablePoints", member.getUsable_points() + "");
        memberMap.put("cumulatePoints", member.getCumulate_points() + "");
        memberMap.put("level", member.getLevel() + "");
        memberMap.put("cumulateAmount", member.getCumulate_amount() + "");
        memberMap.put("interest", member.getInterest());
        memberMap.put("email", member.getEmail());
        memberMap.put("wechatAccount", member.getWechat_account());
        memberMap.put("memberCardNo", member.getMember_card_no());
        memberMap.put("status", String.valueOf(member.getStatus()));

        String occupation = Occupation.valueOf(member.getOccupation()).occuName();
        memberMap.put("occupation", occupation);

        String degreeOfEducation = "";
        switch (member.getDegree_of_education()) {
            case 0:
                degreeOfEducation = "小学";
                break;
            case 1:
                degreeOfEducation = "初中";
                break;
            case 2:
                degreeOfEducation = "高中/中专/技校/职高";
                break;
            case 3:
                degreeOfEducation = "大专";
                break;
            case 4:
                degreeOfEducation = "本科";
                break;
            case 5:
                degreeOfEducation = "硕士";
                break;
            case 6:
                degreeOfEducation = "博士";
                break;
        }
        memberMap.put("degreeOfEducation", degreeOfEducation);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        memberMap.put("registDate", format.format(new Date(member.getRegist_date())));
        memberMap.put("openId", member.getOpen_id());
        memberMap.put("birthday", format.format(member.getBirthday()));

        model.getModel().put("member", memberMap);
        model.setViewName("member");
        return model;
    }

    @PostMapping("/members/filter")
    public String filterMembers(MemberFilterCondition condition, BindingResult errors, String filterJson, HttpSession session) {
        session.setAttribute("members_queryCondition", condition);
        session.setAttribute("members_filterJson", filterJson);

        return "redirect:/members";
    }

    @PostMapping("/members/reset")
    public String resetMembers(HttpSession session) {
        Enumeration<String> names = session.getAttributeNames();

        while (names.hasMoreElements()) {
            String name = names.nextElement();

            if (name.startsWith("members_")) {
                session.removeAttribute(name);
            }
        }

        return "redirect:/members";
    }

    @PostMapping("/members/pushCoupons")
    public String pushCoupons(HttpSession session, String ids) {

        MemberFilterCondition condition = null;

        if (session.getAttribute("members_queryCondition") != null) {
            condition = (MemberFilterCondition) session.getAttribute("members_queryCondition");
        } else {
            condition = new MemberFilterCondition();
            condition.setMallId(settingsProperties.getMallId());
        }

        int result = couponService.pushCoupons(condition, ids);

        if (result <= 0) {
            return "redirect:/error";
        }

        return "redirect:/couponLogs";
    }

    @GetMapping("/membercharts")
    public ModelAndView getChartData() {
        List<Integer> timesData = memberService.getVisitTimesMonthly();
        List<Integer> countData = memberService.getVisitCountMonthly();
        List<Integer> activeData = memberService.getActiveMembersCountMonthly();
        List<Integer> birthdayData = memberService.getMemberCountByBirthday();
        List<Integer> registerData = memberService.getRegisterCountMonthly();

        String timesDataJson = "";
        for (int i = 0; i < timesData.size(); i++) {
            Integer data = timesData.get(i);
            timesDataJson += data + ((i+1) < timesData.size() ? "," : "");
        }

        String countDataJson = "";
        for (int i = 0; i < countData.size(); i++) {
            Integer data = countData.get(i);
            countDataJson += data + ((i+1) < countData.size() ? "," : "");
        }

        String activeDataJson = "";
        for (int i = 0; i < activeData.size(); i++) {
            Integer data = activeData.get(i);
            activeDataJson += data + ((i+1) < activeData.size() ? "," : "");
        }

        String birthdayDataJson = "";
        for (int i = 0; i < birthdayData.size(); i++) {
            Integer data = birthdayData.get(i);
            birthdayDataJson += data + ((i+1) < birthdayData.size() ? "," : "");
        }

        String registerDataJson = "";
        for (int i = 0; i < registerData.size(); i++) {
            Integer data = registerData.get(i);
            registerDataJson += data + ((i+1) < registerData.size() ? "," : "");
        }

        int femaleCount = memberService.getMemberCountBySex(0);
        int maleCount = memberService.getMemberCountBySex(1);
        String sexDataJson = femaleCount + "," + maleCount;

        int $20 = memberService.getMemberCountByAge(0, 30);
        int $30 = memberService.getMemberCountByAge(30, 40);
        int $40 = memberService.getMemberCountByAge(40, 50);
        int $50 = memberService.getMemberCountByAge(50, 60);
        int $60 = memberService.getMemberCountByAge(60, 120);
        String ageDataJson = StringFormatter.format("%d,%d,%d,%d,%d", $20, $30, $40, $50, $60).getValue();

        ModelAndView model = new ModelAndView();

        model.setViewName("memberschart");

        model.getModel().put("timesData", timesDataJson);
        model.getModel().put("countData", countDataJson);
        model.getModel().put("activeData", activeDataJson);
        model.getModel().put("birthdayData", birthdayDataJson);
        model.getModel().put("registerData", registerDataJson);
        model.getModel().put("sexData", sexDataJson);
        model.getModel().put("ageData", ageDataJson);

        return model;
    }

    @GetMapping("/export_members")
    public void exportMembers(HttpServletResponse response, HttpSession session) throws IOException {
        String mimeType = "application/octet-stream";
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", settingsProperties.getExcel());
        response.setHeader(headerKey, headerValue);
        response.setContentType(mimeType);

        MemberFilterCondition condition = null;

        if (session.getAttribute("members_queryCondition") != null) {
            condition = (MemberFilterCondition) session.getAttribute("members_queryCondition");

        } else {
            condition = new MemberFilterCondition();
        }

        condition.setSize(3000000); // excel 最大支持300万行
        List<Member> list = mallService.getMembers(condition);
        memberService.print2Excel(list, response.getOutputStream());
    }

}
