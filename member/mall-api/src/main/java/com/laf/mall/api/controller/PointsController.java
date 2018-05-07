package com.laf.mall.api.controller;

import com.laf.mall.api.querycondition.points.PromotionQueryCondition;
import com.laf.mall.api.service.PointsService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@Slf4j
@RequestMapping("/points")
public class PointsController {

    @Autowired
    PointsService pointsService;

    @PutMapping("/{mallId:\\d+}/simple")
    @ApiOperation(value = "计算(simple)消费兑换积分接口")
    public ResponseEntity calculate(@PathVariable Integer mallId) {

        int result = pointsService.calculatePoints(mallId);

        if (result == 0) return new ResponseEntity(HttpStatus.NOT_IMPLEMENTED);

        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @GetMapping("/simple")
    @ApiOperation(value = "获得(simple)消费额接口")
    public ResponseEntity getAmountSimple(@RequestParam Integer levelId, @RequestParam Integer shopId) {
        BigDecimal amount = pointsService.getSimpleAmount(levelId, shopId);

        if (amount == null || amount.doubleValue() <= 0)
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity(amount.doubleValue(), HttpStatus.OK);

    }

    @PutMapping("/{ruleId:\\d+}/promotion")
    @ApiOperation(value = "计算(促销)消费兑换积分接口")
    public ResponseEntity calculatePromotion(@PathVariable Integer ruleId) {

        int result = pointsService.calculatePromotionPoints(ruleId);

        if (result == 0) return new ResponseEntity(HttpStatus.NOT_IMPLEMENTED);

        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @GetMapping("/promotion")
    @ApiOperation(value = "获得(促销)消费额接口")
    public ResponseEntity getAmountPromotion(@RequestParam Integer levelId,
                                             @RequestParam Integer shopId,
                                             @RequestParam Long today,
                                             @RequestParam Long birthday) {

        PromotionQueryCondition condition = new PromotionQueryCondition();
        condition.setLevelId(levelId);
        condition.setShopId(shopId);
        condition.setBirthday(birthday);
        condition.setToday(today);

        BigDecimal amount = pointsService.getPromotionAmount(condition);

        if (amount == null || amount.doubleValue() <= 0)
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity(amount.doubleValue(), HttpStatus.OK);

    }

    @DeleteMapping("/promotion/{ruleId:\\d+}")
    @ApiOperation(value = "删除(促销)积分规则接口")
    public ResponseEntity deletePromotionRule(@PathVariable Integer ruleId) {

        int result = pointsService.deletePromotionRule(ruleId);

        if (result == 0) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/simple/{ruleId:\\d+}")
    @ApiOperation(value = "删除(simple)积分规则接口")
    public ResponseEntity deleteSimpleRule(@PathVariable Integer ruleId) {
        int result = pointsService.deleteSimpleRule(ruleId);

        if (result == 0) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
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
