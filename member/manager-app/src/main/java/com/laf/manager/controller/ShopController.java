package com.laf.manager.controller;

import com.jayway.jsonpath.JsonPath;
import com.laf.manager.SettingsProperties;
import com.laf.manager.dto.Industry;
import com.laf.manager.dto.Mall;
import com.laf.manager.dto.PlaneMap;
import com.laf.manager.dto.Shop;
import com.laf.manager.querycondition.shop.ShopQueryCondition;
import com.laf.manager.querycondition.shop.ShopSaveCondition;
import com.laf.manager.querycondition.ticket.TicketQueryCondition;
import com.laf.manager.service.MallService;
import com.laf.manager.service.ShopService;
import com.laf.manager.utils.file.FileProperties;
import com.laf.manager.vo.PageVo;
import lombok.extern.slf4j.Slf4j;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class ShopController {

    @Autowired
    ShopService shopService;

    @Autowired
    MallService mallService;

    @Autowired
    FileProperties fileProperties;

    @Autowired
    SettingsProperties settingsProperties;

    @GetMapping("/shops")
    public ModelAndView getShopsForPost(HttpSession session) {
        ShopQueryCondition condition = null;

        if (session.getAttribute("shops_queryCondition") != null) {
            condition = (ShopQueryCondition) session.getAttribute("shops_queryCondition");
        } else {
            condition = new ShopQueryCondition();
        }

        List<Shop> list = shopService.getShopList(condition);

        int total = shopService.getCountShops(condition);
        List<Industry> industries = mallService.getIndustries();
        List<PlaneMap> maps = mallService.getPlaneMapList();

        PlaneMap map = new PlaneMap(0, "全部楼层");
        maps.add(0, map);

        Industry industry = new Industry(0, "全部业态");
        industries.add(0, industry);

        ModelAndView model = new ModelAndView();

        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("index", condition.getPage());
        pageMap.put("size", condition.getSize());
        pageMap.put("total", total);

        model.getModel().put("pageMap", pageMap);
        model.getModel().put("shops", list);
        model.getModel().put("industries", industries);
        model.getModel().put("maps", maps);
        model.setViewName("shops");

        return model;
    }

    @GetMapping("/delShop")
    public String deleteShop(@RequestParam Integer shopId) {
        log.info("deleteshop");
        int result = shopService.deleteShop(shopId);

        return "redirect:/shops";
    }

    @GetMapping("/shop")
    public ModelAndView getShop(@RequestParam(required = false) Integer shopId) {
        final int id = shopId == null ? 0 : shopId.intValue();
        ModelAndView model = new ModelAndView();

        if (id != 0) {
            Shop shop = shopService.getShopById(id);
            if (shop == null) {
                return new ModelAndView("404");
            }

            model.getModel().put("shop", shop);
        }

        Mall mall = mallService.getMallByAppId(settingsProperties.getAppid());

        model.getModel().put("mallName", (mall == null ? "mallName1" : mall.getMall_name()));

        List<Industry> industries = mallService.getIndustries();
        List<PlaneMap> maps = mallService.getPlaneMapList();
        model.getModel().put("industries", industries);
        model.getModel().put("maps", maps);

        model.setViewName("shop");

        return model;
    }

    @PostMapping("/shops/filter")
    public String filterActivities(ShopQueryCondition condition, BindingResult errors, String filterJson, HttpSession session) {
        session.setAttribute("shops_queryCondition", condition);
        session.setAttribute("shops_filterJson", filterJson);

        return "redirect:/shops";
    }

    @PostMapping("/saveShop")
    public String editMap(MultipartFile logo_file, HttpServletRequest request, ShopSaveCondition condition, BindingResult errors)
            throws IOException {

        if (errors.hasErrors()) {
            return "redirect:error";
        }

        String shopImages = condition.getShopImages();
        String[] images = null;
        if (StringUtils.isEmpty(shopImages)) {
            images = new String[0];
        } else {
            shopImages = shopImages.endsWith(",") ? shopImages.substring(0, shopImages.lastIndexOf(",")) : shopImages;
            images = shopImages.split(",");
        }

        if (request instanceof MultipartHttpServletRequest) {
            List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
            // [{"id":"1","mapUrl": "http://121.196.208.176:9354/upload/images/article_1509690733343.png"}]
            String pictures = "[";
            int id = 1;

            for (MultipartFile file : files) {
                if (file == null || !file.getName().equals("file")) continue;

                String uploadFileName = file.getOriginalFilename();

                if (StringUtils.isEmpty(uploadFileName)) continue;

                String suffixName = uploadFileName.substring(uploadFileName.lastIndexOf("."));

                String folder = fileProperties.getPath();
                String fileName = "shop_" + new Date().getTime() + suffixName;

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                File localFile = new File(folder, fileName);
                file.transferTo(localFile);
                String url = fileProperties.getDomain() + fileProperties.getBase() + fileName;

                for (int i = 0; i < images.length; i++) {
                    if (!images[i].contains("http:")) {
                        images[i] = url;
                        break;
                    }
                }
            }

            if (images.length > 0) {
                for (String image : images) {
                    if (!image.startsWith("http")) continue;
                    pictures += "{\"id\":" + id++ + ",\"mapUrl\":\"" + image + "\"},";
                }

                if (pictures.contains(",")) {
                    pictures = pictures.substring(0, pictures.lastIndexOf(","));
                    pictures += "]";
                    condition.setPictures(pictures);
                } else {
                    pictures = "";
                }
            } else {
                pictures = "";
            }

            condition.setPictures(pictures);
        }

        String picture = "";

        String logo = logo_file.getName();

        if (logo_file != null && logo_file.getName().equals("logo_file")) {

            String uploadFileName = logo_file.getOriginalFilename();

            if (!StringUtils.isEmpty(uploadFileName)) {

                String suffixName = uploadFileName.substring(uploadFileName.lastIndexOf("."));

                String folder = fileProperties.getPath();
                String fileName = "shop_logo_" + new Date().getTime() + suffixName;

                File localFile = new File(folder, fileName);
                logo_file.transferTo(localFile);
                String url = fileProperties.getDomain() + fileProperties.getBase() + fileName;
                picture = url;
            }

            condition.setLogo(picture);
        }


        int result = shopService.editShop(condition);

        return "redirect:shops";
    }

    @GetMapping("/export_shops")
    public void exportMembers(HttpServletResponse response, HttpSession session) throws IOException {
        String mimeType = "application/octet-stream";
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", settingsProperties.getExcel());
        response.setHeader(headerKey, headerValue);
        response.setContentType(mimeType);

        ShopQueryCondition condition = null;

        if (session.getAttribute("shops_queryCondition") != null) {
            condition = (ShopQueryCondition) session.getAttribute("shops_queryCondition");

        } else {
            condition = new ShopQueryCondition();
        }

        condition.setSize(3000000); // excel 最大支持300万行
        List<Shop> list = shopService.getShopList(condition);
        shopService.print2Excel(list, response.getOutputStream());
    }
}
