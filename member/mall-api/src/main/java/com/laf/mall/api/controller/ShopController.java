package com.laf.mall.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.laf.mall.api.dto.Shop;
import com.laf.mall.api.querycondition.ShopQueryCondition;
import com.laf.mall.api.service.ShopService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    ShopService shopService;

    @GetMapping("{shopId:\\d+}")
    @JsonView(Shop.ShopAppView.class)
    @ApiOperation(value = "商户详情接口")
    public ResponseEntity getShop(@PathVariable Integer shopId) {
        Shop shop = shopService.getShopDetail(shopId);

        if (shop == null) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity<Shop>(shop, HttpStatus.OK);
    }

    @PostMapping
    @JsonView(Shop.ShopAppListView.class)
    @ApiOperation(value = "商户列表接口")
    public ResponseEntity getShopList(@Valid @RequestBody ShopQueryCondition condition, BindingResult errors) {

        if (errors.hasErrors()) {
            String msg = errors.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
        }

        List<Shop> list = shopService.getShopList(condition);

        if (list.isEmpty()) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity<List<Shop>>(list, HttpStatus.OK);
    }
}
