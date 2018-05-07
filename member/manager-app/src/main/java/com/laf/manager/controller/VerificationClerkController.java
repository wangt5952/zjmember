package com.laf.manager.controller;

import com.jayway.jsonpath.JsonPath;
import com.laf.manager.dto.VerificationClerk;
import com.laf.manager.querycondition.ticket.TicketQueryCondition;
import com.laf.manager.service.VerificationClerkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class VerificationClerkController {

    @Autowired
    VerificationClerkService verificationClerkService;

    @GetMapping("/vClerks")
    public ModelAndView getClerks(@RequestParam(required = false) String pageJson) {
        ModelAndView model = new ModelAndView();
        TicketQueryCondition condition = new TicketQueryCondition();
        int total = verificationClerkService.getTotal();

        Map<String, Object> pageMap = null;
        List<VerificationClerk> list = null;

        if (!StringUtils.isEmpty(pageJson)) {
            /**
             * {"index":1, "size":10,"total":100}
             */
            pageMap = (Map<String, Object>) JsonPath.parse(pageJson).read("$", Map.class);
            int page = Integer.valueOf(pageMap.get("index").toString());
            int size = Integer.valueOf(pageMap.get("size").toString());
            condition.setPage(page);
            condition.setSize(size);
            pageMap.put("total", total);
            list = verificationClerkService.getAllVerificationClerks(size, (page - 1) * size);
        } else { // default page info
            pageMap = new HashMap<String, Object>();
            pageMap.put("index", 1);
            pageMap.put("size", 10);
            pageMap.put("total", total);
            list = verificationClerkService.getAllVerificationClerks(10, 0);
        }

        model.getModel().put("pageMap", pageMap);



        model.getModel().put("verificationClerks", list);
        model.setViewName("vclerks");

        return model;
    }

    @PostMapping("/vClerks")
    public ModelAndView getClerksFromPost(@RequestParam(required = false) String pageJson) {
        ModelAndView model = new ModelAndView();

        int total = verificationClerkService.getTotal();

        TicketQueryCondition condition = new TicketQueryCondition();
        Map<String, Object> pageMap = null;
        List<VerificationClerk> list = null;

        if (!StringUtils.isEmpty(pageJson)) {
            /**
             * {"index":1, "size":10,"total":100}
             */
            pageMap = (Map<String, Object>) JsonPath.parse(pageJson).read("$", Map.class);
            int page = Integer.valueOf(pageMap.get("index").toString());
            int size = Integer.valueOf(pageMap.get("size").toString());
            condition.setPage(page);
            condition.setSize(size);
            pageMap.put("total", total);
            list = verificationClerkService.getAllVerificationClerks(size, (page-1)*size);
        } else { // default page info
            pageMap = new HashMap<String, Object>();
            pageMap.put("index", 1);
            pageMap.put("size", 10);
            pageMap.put("total", total);
            list = verificationClerkService.getAllVerificationClerks(10, 0);
        }

        model.getModel().put("pageMap", pageMap);


        model.getModel().put("verificationClerks", list);
        model.setViewName("vclerks");

        return model;
    }

    @PostMapping("/vClerkPass")
    public String checkClerk(@RequestParam String checkJson) {
        ModelAndView model = new ModelAndView();

        int total = verificationClerkService.getTotal();

//        TicketQueryCondition condition = new TicketQueryCondition();
        Map<String, Object> pageMap = null;

        /**
         * {"index":1, "size":10,"total":100}
         */
        pageMap = (Map<String, Object>) JsonPath.parse(checkJson).read("$", Map.class);
//        int page = Integer.valueOf(pageMap.get("index").toString());
//        int size = Integer.valueOf(pageMap.get("size").toString());
//        int vcId = Integer.valueOf(pageMap.get("vcId").toString());
//        int status = Integer.valueOf(pageMap.get("status").toString());
//        condition.setPage(page);
//        condition.setSize(size);
//        pageMap.put("total", total);

        Map<String, Object> checkMap = null;

        if (!StringUtils.isEmpty(checkJson)) {
            checkMap =  (Map<String, Object>) JsonPath.parse(checkJson).read("$", Map.class);
            int vcId = Integer.valueOf(checkMap.get("vcId").toString());
            int status = Integer.valueOf(checkMap.get("status").toString());

            verificationClerkService.updateVcStatus(vcId, status);
        }


//        model.getModel().put("pageMap", pageMap);

//        List<VerificationClerk> list = verificationClerkService.getAllVerificationClerks(10, 0);
//
//        model.getModel().put("verificationClerks", list);
//        model.setViewName("vclerks");
//
//        return model;

        return "redirect:vClerks";
    }

    @PostMapping("/delVclerk")
    public String delClerk(@RequestParam String delJson) {
        ModelAndView model = new ModelAndView();

        int total = verificationClerkService.getTotal();

//        TicketQueryCondition condition = new TicketQueryCondition();
        Map<String, Object> pageMap = null;

        /**
         * {"index":1, "size":10,"total":100}
         */
        pageMap = (Map<String, Object>) JsonPath.parse(delJson).read("$", Map.class);
//        int page = Integer.valueOf(pageMap.get("index").toString());
//        int size = Integer.valueOf(pageMap.get("size").toString());
//        int vcId = Integer.valueOf(pageMap.get("vcId").toString());
//        int status = Integer.valueOf(pageMap.get("status").toString());
//        condition.setPage(page);
//        condition.setSize(size);
//        pageMap.put("total", total);

        Map<String, Object> checkMap = null;

        if (!StringUtils.isEmpty(delJson)) {
            checkMap =  (Map<String, Object>) JsonPath.parse(delJson).read("$", Map.class);
            int vcId = Integer.valueOf(checkMap.get("vcId").toString());

            verificationClerkService.deleteById(vcId);
        }


//        model.getModel().put("pageMap", pageMap);

//        List<VerificationClerk> list = verificationClerkService.getAllVerificationClerks(10, 0);
//
//        model.getModel().put("verificationClerks", list);
//        model.setViewName("vclerks");
//
//        return model;

        return "redirect:vClerks";
    }
}
