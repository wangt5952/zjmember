package com.laf.mall.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.laf.mall.api.dto.Coupon;
import com.laf.mall.api.dto.ReceiveCouponInfo;
import com.laf.mall.api.querycondition.coupon.CouponForShopQueryCondition;
import com.laf.mall.api.querycondition.coupon.CouponQueryCondition;
import com.laf.mall.api.querycondition.coupon.MyCouponListQueryCondition;
import com.laf.mall.api.service.CouponService;
import com.laf.mall.api.utils.weixin.WeixinUtil;
import com.laf.manager.core.support.SimpleResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping("/list")
    @JsonView(ReceiveCouponInfo.ReceiveCouponInfoAppListView.class)
    @ApiOperation(value = "获取领取型优惠券列表接口")
    public ResponseEntity getCouponInfoList(@Valid @RequestBody CouponQueryCondition condition, BindingResult errors) {

        if (errors.hasErrors()) {
            String msg = "";
            for (ObjectError error : errors.getAllErrors()) {
                msg += error.getDefaultMessage() + ",";
            }
            return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
        }

        List<ReceiveCouponInfo> list = couponService.getCouponInfoList(condition);

        if (list.isEmpty()) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity<List<ReceiveCouponInfo>>(list, HttpStatus.OK);
    }

    @GetMapping("/{couponId:\\d+}")
    @JsonView(ReceiveCouponInfo.ReceiveCouponInfoView.class)
    @ApiOperation(value = "获取优惠券详情接口")
    public ResponseEntity getCouponInfoById(@PathVariable Integer couponId, @RequestParam Integer memberId) {

        ReceiveCouponInfo info = couponService.getCouponInfoById(couponId, memberId);

        if (info == null) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity(info, HttpStatus.OK);
    }

    @GetMapping("/relatecoupons")
    @JsonView(ReceiveCouponInfo.ReceiveCouponInfoView.class)
    @ApiOperation(value = "获取关联型优惠券列表接口")
    public ResponseEntity getRelateCouponInfoList(@RequestParam Integer mallId) {

        if (mallId <= 0) return new ResponseEntity<>(new SimpleResponse("参数无效"), HttpStatus.BAD_REQUEST);

        List<ReceiveCouponInfo> list = couponService.getRelateCouponInfoList(mallId);

        if (list.isEmpty()) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity(list, HttpStatus.OK);
    }

    @PostMapping("/listforshop")
    @JsonView(ReceiveCouponInfo.ReceiveCouponInfoAppListView.class)
    @ApiOperation(value = "获取商户相关的优惠券列表接口")
    public ResponseEntity getCouponInfoListByShopId(@Valid @RequestBody CouponForShopQueryCondition condition, BindingResult errors) {

        if (errors.hasErrors()) {
            String msg = "";
            for (ObjectError error : errors.getAllErrors()) {
                msg = error.getDefaultMessage() + ",";
            }
            return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
        }

        List<ReceiveCouponInfo> list = couponService.getCouponInfoListByShopId(condition);

        if (list.isEmpty()) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity<List<ReceiveCouponInfo>>(list, HttpStatus.OK);
    }
}
