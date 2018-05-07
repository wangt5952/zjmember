package com.laf.mall.api.repository;

import com.laf.mall.api.dto.Activity;
import com.laf.mall.api.dto.ActivitySignUpLog;
import com.laf.mall.api.querycondition.activity.ActivityQueryCondition;
import com.laf.mall.api.utils.datetime.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.*;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ActivityRepositoryTest {

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    DateTimeUtils dateTimeUtils;

    @Test
    public void selectActivityById() throws Exception {
        Activity activity = activityRepository.selectActivityById(1);

        if (activity != null) {
            log.info(activity.getTitle());
            log.info(activity.getIntro());
        }

        assertNotNull(activity);
    }

    @Test
    public void selectActivityById1() throws Exception {
        Activity activity = activityRepository.selectActivityById(1, 1);

        if (activity != null) {
            log.info(activity.getTitle());
            log.info(dateTimeUtils.getDateTimeByMilli(activity.getSign_up_time()).toString());
        }

        assertNotNull(activity);
    }

    @Test
    public void selectActivityList() throws Exception {
        ActivityQueryCondition condition = new ActivityQueryCondition();

        condition.setMallId(1);
        condition.setMemberId(1);
        List<Activity> list = activityRepository.selectActivityList(condition, 0);

        assertNotNull(list);
        assertTrue(list.size() > 0);

        list.stream().forEach(System.out::println);
    }

    @Test
    public void insertActivitySignLog() throws Exception {
        ActivitySignUpLog activitySignUpLog = new ActivitySignUpLog();
        activitySignUpLog.setActivity_id(1);
        activitySignUpLog.setMall_id(1);
        activitySignUpLog.setMember_id(2);
        activitySignUpLog.setSign_type(2);
        activitySignUpLog.setSign_in_time(new Date());
        int result = activityRepository.insertSignUpLog(activitySignUpLog);

        assertEquals(result, 1);
    }

    @Test
    public void updateActivitySignLog() throws Exception {
        ActivitySignUpLog activitySignUpLog = new ActivitySignUpLog();
        activitySignUpLog.setActivity_id(1);
        activitySignUpLog.setMember_id(2);
        activitySignUpLog.setMall_id(1);
        activitySignUpLog.setSign_up_time(new Date());
        activitySignUpLog.setSign_type(1);

        int result = activityRepository.updateSignUpLog(activitySignUpLog);

        assertEquals(result, 1);
    }

    @Test
    public void selectSignUpCount() throws Exception {

        int result = activityRepository.selectSignUpCount(1);

        assertTrue(result > 0);

        log.info("======{}========", result);
    }

}