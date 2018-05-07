package com.laf.manager.dao;

import com.laf.manager.dto.Activity;
import com.laf.manager.dto.ActivitySignUpLog;
import com.laf.manager.querycondition.activity.ActivityLogQueryCondition;
import com.laf.manager.querycondition.activity.ActivityQueryCondition;
import com.laf.manager.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActivityDao {

    @Autowired
    ActivityRepository activityRepository;

    public Activity getActivityById(final Integer activityId) {
        return activityRepository.selectActivityById(activityId);
    }

//    public Activity getActivityById(final Integer activityId, final Integer memberId) {
//        return activityRepository.selectActivityById(activityId, memberId);
//    }

    public List<Activity> getActivityList(ActivityQueryCondition condition) {
        return activityRepository.selectActivityList(condition);
    }

//    public int saveActivitySignLog(ActivitySignUpLog activitySignUpLog) {
//        return activityRepository.insertSignUpLog(activitySignUpLog);
//    }

//    public int editActivitySignLog(ActivitySignUpLog activitySignUpLog) {
//        return activityRepository.insertSignUpLog(activitySignUpLog);
//    }

    public int getSignUpCount(final Integer activityId) {
        return activityRepository.selectSignUpCount(activityId);
    }

    public int saveActivity(Activity activity) {
        return activityRepository.insertActivity(activity);
    }

    public int updateActivity(Activity activity) {
        return activityRepository.updateActivity(activity);
    }

    public int updateActivityQRCode(final int activityId, final String url) {
        return activityRepository.updateActivityQRCode(activityId, url);
    }

    public int deleteAtivity(final Integer activityId) {
        return activityRepository.deleteAtivity(activityId);
    }

    public int getActivityCount(ActivityQueryCondition condition) {
        return activityRepository.selectActivityCount(condition);
    }

    public List<Integer> getActivityCountMonthly(String year, int mallId) {
        return activityRepository.selectActivityCountMonthly(year, mallId);
    }

    public List<ActivitySignUpLog> getActivityLogList(ActivityLogQueryCondition condition) {
        return activityRepository.selectActivityLogList(condition);
    }

    public int getActivityLogCount(ActivityLogQueryCondition condition) {
        return activityRepository.selectActivityLogCount(condition);
    }
}
