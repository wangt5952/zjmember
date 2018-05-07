package com.laf.mall.api.controller;

import com.laf.mall.api.querycondition.ticket.TicketNoPassSaveCondition;
import com.laf.mall.api.querycondition.ticket.TicketNoQueryConditon;
import com.laf.mall.api.service.TicketService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    TicketService ticketService;

    @PostMapping("/ticketfail")
    @ApiOperation(value = "处理小票未通过接口")
    public ResponseEntity ticketFail(@Valid @RequestBody TicketNoPassSaveCondition condition, BindingResult errors) {

        if (errors.hasErrors()) {
            String msg = handlePostJsonParamError(errors);

            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }

        int result = ticketService.operateTicketForNoPass(condition);

        if (result <= 0) return new ResponseEntity("未找到资源", HttpStatus.NOT_FOUND);

        return new ResponseEntity(result, HttpStatus.OK);
    }


    @PostMapping("/verifyticketno")
    @ApiOperation(value = "校验交易单号是否重复接口")
    public ResponseEntity verifyTicketNo(@Valid @RequestBody TicketNoQueryConditon condition, BindingResult errors) {

        if (errors.hasErrors()) {
            String msg = handlePostJsonParamError(errors);

            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }

        int count = ticketService.getTicketByTicketNoCount(condition);

        if (count > 0) return new ResponseEntity(HttpStatus.FORBIDDEN);

        return new ResponseEntity(count, HttpStatus.OK);
    }

    private String handlePostJsonParamError(BindingResult errors) {
        String errMsg = "[";

        for (ObjectError error : errors.getAllErrors()) {
            errMsg += error.getDefaultMessage() + ",";
        }

        errMsg += "]";

        return errMsg;
    }
}
