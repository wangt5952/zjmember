package com.laf.mall.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.laf.mall.api.dto.Activity;
import com.laf.mall.api.querycondition.activity.ActivityQueryCondition;
import com.laf.mall.api.service.ActivityService;
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
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    ActivityService activityService;

    @GetMapping("/{activityId:\\d+}")
    @JsonView(Activity.ActivityAppView.class)
    @ApiOperation(value = "活动详情接口")
    public ResponseEntity getActivityDetail(@PathVariable Integer activityId, @RequestParam Integer memberId) {

        if (activityId <= 0 || memberId <= 0) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Activity activity = activityService.getActivityDetail(activityId, memberId);

        if (activity == null) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity<Activity>(activity, HttpStatus.OK);
    }

    @PostMapping
    @JsonView(Activity.ActivityListView.class)
    @ApiOperation(value = "活动列表接口")
    public ResponseEntity getActivityList(@Valid @RequestBody ActivityQueryCondition condition, BindingResult errors) {
        if (errors.hasErrors()) {
            String msg = handlePostJsonParamError(errors);

            return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
        }

        List<Activity> list = activityService.getActivityList(condition);

        if (list.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<List<Activity>>(list, HttpStatus.OK);
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
