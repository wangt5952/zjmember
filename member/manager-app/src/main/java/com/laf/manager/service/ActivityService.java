package com.laf.manager.service;

import com.laf.manager.dto.Activity;
import com.laf.manager.dto.ActivitySignUpLog;
import com.laf.manager.querycondition.activity.ActivityEditCondition;
import com.laf.manager.querycondition.activity.ActivityLogQueryCondition;
import com.laf.manager.querycondition.activity.ActivityQueryCondition;

import java.util.List;

public interface ActivityService {

    List<Activity> getActivityList(final ActivityQueryCondition condition);

    Activity getActivityById(final Integer activityId);

    int editActivity(ActivityEditCondition condition);

    int delActivity(final Integer activityId);

    int getActivityCount(final ActivityQueryCondition condition);

    List<Integer> getActivityCountMonthly(String year);

    List<ActivitySignUpLog> getActivityLogList(ActivityLogQueryCondition condition);

    int getActivityLogCount(ActivityLogQueryCondition condition);
}
