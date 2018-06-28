package com.laf.mall.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.laf.mall.api.dto.GagBill;
import com.laf.mall.api.service.GagService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/gag")
public class GoagoController {

    @Autowired
    GagService gagService;

    @GetMapping("bill")
    @JsonView(GagBill.GagListView.class)
    @ApiOperation(value = "获取购阿购账单信息")
    public ResponseEntity getGagBill(@RequestParam String qrCode) {
        GagBill bill = gagService.getBill(qrCode);
        return new ResponseEntity(bill, HttpStatus.OK);
    }

    @PostMapping("/uploadBill/{memberId:\\d+}")
    @ApiOperation(value = "小票购阿购扫码上传接口")
    public ResponseEntity upload(@PathVariable Integer memberId, @RequestParam String qrCode){
        int result = gagService.uploadBill(memberId, qrCode);
        return new ResponseEntity(result, HttpStatus.OK);
    }

}
