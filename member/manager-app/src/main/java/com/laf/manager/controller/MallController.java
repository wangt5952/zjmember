package com.laf.manager.controller;

import com.laf.manager.SettingsProperties;
import com.laf.manager.dto.Industry;
import com.laf.manager.dto.Mall;
import com.laf.manager.dto.PlaneMap;
import com.laf.manager.dto.Shop;
import com.laf.manager.querycondition.industry.IndustryEditCondition;
import com.laf.manager.querycondition.mall.MallEditCondition;
import com.laf.manager.querycondition.planemap.MapSaveCondition;
import com.laf.manager.service.MallService;
import com.laf.manager.service.ShopService;
import com.laf.manager.utils.file.FileProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
public class MallController {

    @Autowired
    FileProperties fileProperties;

    @Autowired
    SettingsProperties settingsProperties;

    @Autowired
    MallService mallService;

    @Autowired
    ShopService shopService;

    @PostMapping("/saveMall")
    public String handleData(HttpServletRequest request, MallEditCondition condition, BindingResult errors)
            throws IOException {

        if (errors.hasErrors()) {
            return "redirect:error";
        }

        String mallImages = condition.getMallImages();
        String[] images = null;
        if (StringUtils.isEmpty(mallImages)) {
            images = new String[0];
        } else {
            mallImages = mallImages.endsWith(",") ? mallImages.substring(0, mallImages.lastIndexOf(",")) : mallImages;
            images = mallImages.split(",");
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
                String fileName = "mall_" + new Date().getTime() + suffixName;

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


        int result = mallService.editMall(condition);

        if (result <= 0) {
            return "redirect:error";
        }

        return "redirect:/";
    }

    @PostMapping("/uploadImage")
    public ResponseEntity upload(MultipartFile file) throws IOException {

        if (file == null || !file.getName().equals("file")) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        String uploadFileName = file.getOriginalFilename();
        String suffixName = uploadFileName.substring(uploadFileName.lastIndexOf("."));

        String folder = fileProperties.getPath();
        String fileName = "article_" + new Date().getTime() + suffixName;

        File localFile = new File(folder, fileName);
        file.transferTo(localFile);

        String url = fileProperties.getDomain() + fileProperties.getBase() + fileName;
//        FileInfo image = new FileInfo(url);

        return new ResponseEntity(url, HttpStatus.OK);
    }

    @GetMapping("/mall")
    public ModelAndView getMall() {
        ModelAndView model = new ModelAndView();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();

        if (name.equals("user1")) {
            List<Shop> shops = shopService.getShopAllList();

            model.getModel().put("shops", shops);
            model.setViewName("handlepoints");
            return model;
        }

        Mall mall = mallService.getMallByAppId(settingsProperties.getAppid());
        if (mall == null) {
            model.setViewName("error");
        } else {
            model.getModel().put("mall", mall);
            model.setViewName("mall");
        }

        return model;
    }

    @GetMapping("/industries")
    public ModelAndView getIndustries() {
        List<Industry> list = mallService.getIndustries();
        ModelAndView model = new ModelAndView();

//        if (list.isEmpty()) {
//            model.setViewName("error");
//        } else {
        model.getModel().put("industries", list);
        model.setViewName("industry");
//        }

        return model;

    }

    @GetMapping("/industry")
    public ModelAndView getIndustry(@RequestParam(required = false) Integer industryId) {
        int id = industryId == null ? 0 : industryId.intValue();

        Industry industry = mallService.getIndustry(id);
        ModelAndView model = new ModelAndView();

        if (industry != null) {
            model.getModel().put("industry", industry);
        }

        model.setViewName("industryEdit");

        return model;
    }

    @PostMapping("/saveIndustry")
    public String editIndustry(IndustryEditCondition condition, BindingResult errors) {
        if (errors.hasErrors()) {
            return "redirect:error";
        }

        Industry industry = new Industry();
        industry.setSort_id(condition.getSortId());
        industry.setIndustry_name(condition.getIndustryName());
        industry.setIndustry_id(condition.getIndustryId());

        int result = mallService.editIndustry(industry);

        if (result <= 0) {
            return "redirect:error";
        }

        return "redirect:industries";
    }

    @GetMapping("/delIndustry")
    public String deleteIndustry(@RequestParam Integer industryId) {

        mallService.deleteIndustry(industryId);

        return "redirect:/industries";
    }

    @PostMapping("/saveMap")
    public String editMap(MultipartFile file, MapSaveCondition condition, BindingResult errors)
            throws IOException {

        if (errors.hasErrors()) {
            return "redirect:error";
        }

        String picture = "";

        if (file != null && file.getName().equals("file")) {

            String uploadFileName = file.getOriginalFilename();

            if (!StringUtils.isEmpty(uploadFileName)) {

                String suffixName = uploadFileName.substring(uploadFileName.lastIndexOf("."));

                String folder = fileProperties.getPath();
                String fileName = "plane_" + new Date().getTime() + suffixName;

                File localFile = new File(folder, fileName);
                file.transferTo(localFile);
                String url = fileProperties.getDomain() + fileProperties.getBase() + fileName;
                picture = url;
            }
        }

        PlaneMap map = new PlaneMap();
        map.setMap_id(condition.getMapId());
        map.setMap_name(condition.getMapName());
        map.setSort_id(condition.getSortId());
        map.setMap_picture(picture);

        int result = mallService.editPlaneMap(map);

        return "redirect:planeMaps";
    }

    @GetMapping("/planeMaps")
    public ModelAndView getPlaneMaps() {
        List<PlaneMap> list = mallService.getPlaneMapList();

        ModelAndView model = new ModelAndView();

        model.getModel().put("maps", list);
        model.setViewName("planemaps");

        return model;
    }

    @GetMapping("/planeMap")
    public ModelAndView getPlaneMap(@RequestParam(required = false) Integer mapId) {
        int id = mapId == null ? 0 : mapId.intValue();
        PlaneMap map = mallService.getPlaneMapById(id);

        ModelAndView model = new ModelAndView();

        if (map != null) {
            model.getModel().put("map", map);
        }

        model.setViewName("planemap");

        return model;
    }

    @GetMapping("/delMap")
    public String deleteMap(@RequestParam Integer mapId) {

        mallService.deletePlaneMap(mapId);

        return "redirect:/planeMaps";
    }
}
