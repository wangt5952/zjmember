package com.laf.mall.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.laf.mall.api.dto.Coupon;
import com.laf.mall.api.dto.RaffleInfo;
import com.laf.mall.api.dto.RaffleReward;
import com.laf.mall.api.querycondition.raffle.MyRewardQueryCondition;
import com.laf.mall.api.querycondition.raffle.RaffleRewardSaveCondition;
import com.laf.mall.api.service.RaffleService;
import com.laf.mall.api.vo.MyRewardInfo;
import com.laf.manager.core.exception.MallDBException;
import com.laf.manager.core.support.SimpleResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
@RequestMapping("/raffle")
public class RaffleController {

    @Autowired
    private RaffleService raffleService;

    @GetMapping("/{activityCode}/info")
    @JsonView({RaffleInfo.RaffleInfoView.class})
    @ApiOperation(value = "抽奖信息接口")
    public ResponseEntity getRaffleInfo(@PathVariable String activityCode, @RequestParam Integer mallId) {

        if (StringUtils.isEmpty(activityCode) || mallId <= 0) return new ResponseEntity(new SimpleResponse("参数无效"), HttpStatus.BAD_REQUEST);

        RaffleInfo info = raffleService.getRaffleInfo(activityCode, mallId);

        if (info == null) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/{activityCode}/verify", method = RequestMethod.HEAD)
    @ApiOperation(value = "验证抽奖资格接口")
    public ResponseEntity verifyRaffle(@PathVariable String activityCode, @RequestParam Integer memberId) {
        if (StringUtils.isEmpty(activityCode) || memberId <= 0) return new ResponseEntity(new SimpleResponse("参数无效"), HttpStatus.BAD_REQUEST);

        int result = raffleService.verifyRaffle(memberId, activityCode);

        if (result == -1) return new ResponseEntity(HttpStatus.FORBIDDEN);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/{activityCode}/toRaffle")
    @ApiOperation(value = "抽奖接口")
    public ResponseEntity toRaffle(@PathVariable String activityCode, @Valid @RequestBody RaffleRewardSaveCondition condition, BindingResult errors) {

        if (errors.hasErrors()) {
            String msg = handlePostJsonParamError(errors);

            return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isEmpty(activityCode))
            return new ResponseEntity(new SimpleResponse("参数无效"), HttpStatus.BAD_REQUEST);

        int result;
        try {
            result = raffleService.toRaffle(activityCode, condition);
        } catch (MallDBException e) {
            result = e.getResult();
        }

        if (result < 1) return new ResponseEntity(HttpStatus.FORBIDDEN);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/myReward")
    @JsonView(MyRewardInfo.MyRewardInfoListView.class)
    @ApiOperation(value = "我的奖品列表接口")
    public ResponseEntity getMyRewardList(@Valid @RequestBody MyRewardQueryCondition condition, BindingResult errors) {

        if (errors.hasErrors()) {
            String msg = handlePostJsonParamError(errors);

            return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
        }

        List<MyRewardInfo> list = raffleService.getMyRewardList(condition);

        if (list.isEmpty()) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity(list, HttpStatus.OK);
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
