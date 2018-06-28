package com.laf.mall.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.laf.mall.api.dto.*;
import com.laf.mall.api.querycondition.coupon.CouponQueryCondition;
import com.laf.mall.api.querycondition.coupon.MyCouponListQueryCondition;
import com.laf.mall.api.querycondition.coupon.MyCouponQueryCondition;
import com.laf.mall.api.service.ParkingCouponService;
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
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/parkingCoupon")
public class ParkingCouponController {

    @Autowired
    private ParkingCouponService couponService;

    @GetMapping("/getCarList")
    @ApiOperation(value = "获取会员车辆信息接口")
    public ResponseEntity getCouponInfoById(@RequestParam Integer memberId) {
        List<Parking>  list = couponService.getParkingList(memberId);
        ParkingInfo info = couponService.getParkingInfo();
        Map<String,Object> map = new HashMap<>();
        map.put("carInfo", list);
        map.put("intro", info.getIntro());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/member/{memberId:\\d+}/saveCarInfo")
    @ApiOperation(value = "更新会员车辆信息接口")
    public ResponseEntity saveCarInfo(@PathVariable Integer memberId, @RequestParam String carNumber) {
        int result = couponService.updateCarInfo(memberId,carNumber);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/getCarPayInfo")
    @ApiOperation(value = "获取车辆缴费信息接口")
    public ResponseEntity getCarPayInfo(@RequestParam Integer memberId, @RequestParam String carNumber) {
        ParkingPayInfo pay = new ParkingPayInfo();
        pay.setIn_date(new Date());
        pay.setParking_position("B区108");
        pay.setPrice(new BigDecimal(20));
        return new ResponseEntity<>(pay, HttpStatus.OK);
    }

    @PostMapping("/list")
    @JsonView(ReceiveCouponInfo.ReceiveCouponInfoAppListView.class)
    @ApiOperation(value = "获取停车券列表接口")
    public ResponseEntity getCouponInfoList(@Valid @RequestBody CouponQueryCondition condition, BindingResult errors) {
        if (errors.hasErrors()) {
            String msg = "";
            for (ObjectError error : errors.getAllErrors()) {
                msg += error.getDefaultMessage() + ",";
            }
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
        List<ReceiveCouponInfo> list = couponService.getCouponInfoList(condition);
        if (list.isEmpty()) return new ResponseEntity(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{couponId:\\d+}")
    @JsonView(ReceiveCouponInfo.ReceiveCouponInfoView.class)
    @ApiOperation(value = "获取停车券详情接口")
    public ResponseEntity getCouponInfoById(@PathVariable Integer couponId, @RequestParam Integer memberId) {
        ReceiveCouponInfo info = couponService.getCouponInfoById(couponId, memberId);
        if (info == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
        return new ResponseEntity(info, HttpStatus.OK);
    }

    @GetMapping("/member/{memberId:\\d+}/receiveCoupon/{couponId:\\d+}")
    @ApiOperation(value = "停车券领取接口")
    public ResponseEntity receiveCoupon(@PathVariable Integer memberId, @PathVariable Integer couponId) {
        if (memberId <= 0 || couponId <= 0)
            return new ResponseEntity(new SimpleResponse("参数无效"), HttpStatus.BAD_REQUEST);
        int result = 0;
        result = couponService.receiveCouponByMember(memberId, couponId);
        if (result == 8) return new ResponseEntity(result, HttpStatus.OK);
        else return new ResponseEntity(result, HttpStatus.FORBIDDEN);
    }

    @PostMapping("/member/{memberId:\\d+}/couponList")
    @JsonView(Coupon.CouponAppListView.class)
    @ApiOperation(value = "获取我的券列表接口")
    public ResponseEntity getCouponList(@PathVariable Integer memberId, @Valid @RequestBody MyCouponQueryCondition condition, BindingResult errors) {
        if (errors.hasErrors()) {
            String msg = handlePostJsonParamError(errors);
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
        if (memberId <= 0) return new ResponseEntity(new SimpleResponse("参数无效"), HttpStatus.BAD_REQUEST);
        MyCouponListQueryCondition myCondition = new MyCouponListQueryCondition();
        myCondition.setCouponStatus(condition.getCouponStatus());
        myCondition.setMallId(condition.getMallId());
        myCondition.setMemberId(memberId);
        myCondition.setPage(condition.getPage() == 0 ? 1 : condition.getPage());
        myCondition.setSize(condition.getSize() == 0 ? 5 : condition.getSize());
        List<Coupon> list = couponService.getCouponListByMember(myCondition);
        if (list.isEmpty()) return new ResponseEntity(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/member/{crlId:\\d+}")
    @JsonView(Coupon.CouponDetailView.class)
    @ApiOperation(value = "我的券详情接口")
    public ResponseEntity getCoupon(@PathVariable Integer crlId) {

        if (crlId <= 0) return new ResponseEntity(new SimpleResponse("参数无效"), HttpStatus.BAD_REQUEST);

        Coupon coupon = couponService.getCouponById(crlId);

        if (coupon == null) return new ResponseEntity(new SimpleResponse("券不存在"), HttpStatus.NOT_FOUND);

        return new ResponseEntity(coupon, HttpStatus.OK);
    }

    @PostMapping("/pay")
    @ApiOperation(value = "停车缴费")
    public ResponseEntity parkingPay(@Valid @RequestBody ParkingCouponInfo info) {
        int result = 0;
        if(info.getTicket_no() == null || info.getTicket_no().equals("") || info.getCrl_id() < 0 || info.getMember_id() < 0)
            return new ResponseEntity(new SimpleResponse("参数无效"), HttpStatus.BAD_REQUEST);
        Coupon coupon = null;
        if(info.getCrl_id() > 0){
            coupon = couponService.getCouponById(info.getCrl_id());
            if (coupon == null) return new ResponseEntity(new SimpleResponse("券不存在"), HttpStatus.NOT_FOUND);
        }
        Date inDate = new Date(info.getIn_date());
        result = couponService.parkingPay(info.getMember_id(),info.getCar_number(),info.getTicket_no(),coupon,info.getPrice(),inDate);
        return new ResponseEntity<>(result, HttpStatus.OK);
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
