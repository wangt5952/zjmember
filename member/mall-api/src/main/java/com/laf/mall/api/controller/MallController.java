package com.laf.mall.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.laf.mall.api.dao.MallDao;
import com.laf.mall.api.dto.Industry;
import com.laf.mall.api.dto.Mall;
import com.laf.mall.api.dto.PlaneMap;
import com.laf.mall.api.service.ShopService;
import com.laf.mall.api.utils.file.FileProperties;
import com.sun.org.apache.regexp.internal.RE;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/mall")
public class MallController {

    @Autowired
    MallDao dao;

    @Autowired
    ShopService shopService;

    @Autowired
    FileProperties fileProperties;

//    @GetMapping("/{mallId:\\d+}")
//    @JsonView(Mall.MallAppView.class)
//    public Mall getMallDetail(@PathVariable int mallId) {
//        Mall mall = dao.getMallById(mallId);
//
//        return mall;
//    }

    @GetMapping("/{appId:\\S+}")
    @JsonView(Mall.MallAppView.class)
    @ApiOperation(value = "商场详情接口")
    public ResponseEntity getMallDetail(@ApiParam(value = "微信公共号AppID") @PathVariable String appId) {
        Mall mall = dao.getMallByAppId(appId);

        if (mall == null) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity<Mall>(mall, HttpStatus.OK);
    }

//    @GetMapping
//    @JsonView(Mall.MallAppView.class)
//    public List<Mall> getMallList(MallQueryCondition condition,@RequestParam(required = false)
//                                  @PageableDefault(page = 1, size = 5, sort = "aaa,desc")
//                                  Pageable pageable) {
//
//        log.info(ReflectionToStringBuilder.toString(pageable, ToStringStyle.MULTI_LINE_STYLE));
//        List<Mall> list = new ArrayList<>();
//        list.add(new Mall());
//        list.add(new Mall());
//        list.add(new Mall());
//
//        return list;
//    }


    @GetMapping("/{mallId:\\d+}/industries")
    @JsonView(Industry.IndustryAppView.class)
    @ApiOperation(value = "业态列表接口")
    public ResponseEntity getIndustries(@ApiParam(value = "商场Id") @PathVariable Integer mallId) {
        List<Industry> list = shopService.getIndustries(mallId);

        if (list == null || ListUtils.EMPTY_LIST == list) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<List<Industry>>(list, HttpStatus.OK);
    }

    @GetMapping("/{mallId:\\d+}/planeMaps")
    @JsonView(PlaneMap.PlaneMapAppView.class)
    @ApiOperation(value = "平面地图列表接口")
    public ResponseEntity getPlaneMaps(@ApiParam(value = "商场Id") @PathVariable Integer mallId) {
        List<PlaneMap> list = shopService.getPlaneMap(mallId);

        if (list == null || ListUtils.EMPTY_LIST == list) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<List<PlaneMap>>(list, HttpStatus.OK);
    }

    @PostMapping("/uploadImage")
//    @JsonView(FileInfo.FileAllJsonView.class)
    @ApiOperation(value = "图片上传接口")
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

    /**
     * 测试RequsetBody注解 json 转 HashMap
     * 测试结果： 可以正确注入
     */
//    @PostMapping("/objcet2map")
//    public ResponseEntity object2map(@RequestBody HashMap<String, Object> map) {
//        for (String key : map.keySet()) {
//            String s = map.get(key).toString();
//        }
//
//        return new ResponseEntity(HttpStatus.OK);
//    }
}
