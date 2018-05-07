package com.laf.mall.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.laf.mall.api.dto.GiftCoupon;
import com.laf.mall.api.dto.GiftCouponInfo;
import com.laf.mall.api.querycondition.coupon.CouponForShopQueryCondition;
import com.laf.mall.api.querycondition.coupon.MyCouponListQueryCondition;
import com.laf.mall.api.querycondition.coupon.MyCouponQueryCondition;
import com.laf.mall.api.querycondition.giftcoupon.GiftCouponQueryCondition;
import com.laf.mall.api.service.GiftCouponService;
import com.laf.manager.core.exception.MallDBException;
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
@RequestMapping("/giftCoupon")
public class GiftCouponController {

    @Autowired
    private GiftCouponService giftCouponService;

    @PostMapping("/info/list")
    @JsonView(GiftCouponInfo.GiftCouponInfoListView.class)
    @ApiOperation(value = "礼品券列表接口")
    public ResponseEntity getGiftCouponInfoList(@Valid @RequestBody GiftCouponQueryCondition condition, BindingResult errors) {

        if (errors.hasErrors()) {
            String msg = "";
            for (ObjectError error : errors.getAllErrors()) {
                msg += error.getDefaultMessage() + ",";
            }
            return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
        }

        List<GiftCouponInfo> list = giftCouponService.getGiftCouponInfoList(condition);

        if (list.isEmpty()) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity<List<GiftCouponInfo>>(list, HttpStatus.OK);
    }

    @GetMapping("/info/{couponId:\\d+}")
    @JsonView(GiftCouponInfo.GiftCouponInfoView.class)
    @ApiOperation(value = "礼品券详情接口")
    public ResponseEntity getGiftCouponInfoById(@PathVariable Integer couponId, @RequestParam Integer memberId) {

        GiftCouponInfo info = giftCouponService.getGiftCouponInfoById(couponId, memberId);

        if (info == null) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity(info, HttpStatus.OK);
    }

    @PostMapping("/info/forshop")
    @JsonView(GiftCouponInfo.GiftCouponInfoListView.class)
    @ApiOperation(value = "获取商户相关的礼品券列表接口")
    public ResponseEntity getCouponInfoListByShopId(@Valid @RequestBody CouponForShopQueryCondition condition, BindingResult errors) {

        if (errors.hasErrors()) {
            String msg = "";
            for (ObjectError error : errors.getAllErrors()) {
                msg = error.getDefaultMessage() + ",";
            }
            return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
        }

        List<GiftCouponInfo> list = giftCouponService.getGiftCouponInfoListByShopId(condition);

        if (list.isEmpty()) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity<List<GiftCouponInfo>>(list, HttpStatus.OK);
    }

    @PostMapping("/member/{memberId:\\d+}/list")
    @JsonView(GiftCoupon.CouponAppListView.class)
    @ApiOperation(value = "获取我的礼品券列表接口")
    public ResponseEntity getCouponList(@PathVariable Integer memberId, @Valid @RequestBody MyCouponQueryCondition condition, BindingResult errors) {

        if (errors.hasErrors()) {
            String msg = handlePostJsonParamError(errors);

            return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
        }

        if (memberId <= 0) return new ResponseEntity(new SimpleResponse("参数无效"), HttpStatus.BAD_REQUEST);

        MyCouponListQueryCondition myCondition = new MyCouponListQueryCondition();
        myCondition.setCouponStatus(condition.getCouponStatus());
        myCondition.setMallId(condition.getMallId());
        myCondition.setMemberId(memberId);
        myCondition.setPage(condition.getPage() == 0 ? 1 : condition.getPage());
        myCondition.setSize(condition.getSize() == 0 ? 5 : condition.getSize());
        List<GiftCoupon> list = giftCouponService.getCouponListByMember(myCondition);

        if (list.isEmpty()) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity<List<GiftCoupon>>(list, HttpStatus.OK);
    }

    @GetMapping("/member/{memberId:\\d+}/receiveCoupon/{couponId:\\d+}")
    @ApiOperation(value = "礼品券领取接口")
    public ResponseEntity receiveCoupon(@PathVariable Integer memberId, @PathVariable Integer couponId) {

        if (memberId <= 0 || couponId <= 0)
            return new ResponseEntity(new SimpleResponse("参数无效"), HttpStatus.BAD_REQUEST);

        int result = 0;

        try {
            result = giftCouponService.receiveCouponByMember(memberId, couponId);
        } catch (MallDBException e) {
            result = e.getResult();
        }

        if (result == 8) return new ResponseEntity(result, HttpStatus.OK);
        else return new ResponseEntity(result, HttpStatus.FORBIDDEN);
    }

    @GetMapping("/coupon/{crlId:\\d+}")
    @JsonView(GiftCoupon.CouponDetailView.class)
    @ApiOperation(value = "我的礼品券详情接口")
    public ResponseEntity getCoupon(@PathVariable Integer crlId) {

        if (crlId <= 0) return new ResponseEntity(new SimpleResponse("参数无效"), HttpStatus.BAD_REQUEST);

        GiftCoupon coupon = giftCouponService.getCouponById(crlId);

        if (coupon == null) return new ResponseEntity(new SimpleResponse("券不存在"), HttpStatus.NOT_FOUND);

        return new ResponseEntity(coupon, HttpStatus.OK);
    }

    @GetMapping("/verify")
    @ApiOperation(value = "礼品券核销接口")
    public ResponseEntity toVerify(@RequestParam Integer memberId, @RequestParam Integer crlId) {

        if (memberId <= 0 || crlId <= 0) {
            return new ResponseEntity(new SimpleResponse("参数无效"), HttpStatus.BAD_REQUEST);
        }

        int result = 0;

        try {
            result = giftCouponService.toVerify(memberId, crlId);
        } catch (MallDBException e) {
            result = e.getResult();
        }

        if (result <= 0) return new ResponseEntity(new SimpleResponse("核销失败"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity(new SimpleResponse("核销成功"), HttpStatus.OK);
    }

    private String handlePostJsonParamError(BindingResult errors) {
        String errMsg = "[";

        for (ObjectError error : errors.getAllErrors()) {
            errMsg += error.getDefaultMessage() + ",";
        }
        errMsg = errMsg.substring(0, errMsg.length());
        errMsg += "]";

        return errMsg;
    }
}
