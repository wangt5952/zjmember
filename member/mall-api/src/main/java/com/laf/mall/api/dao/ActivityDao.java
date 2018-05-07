package com.laf.mall.api.dao;

import com.laf.mall.api.dto.Activity;
import com.laf.mall.api.dto.ActivitySignUpLog;
import com.laf.mall.api.dto.ReceiveCouponInfo;
import com.laf.mall.api.querycondition.activity.ActivityQueryCondition;
import com.laf.mall.api.repository.ActivityRepository;
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

    public Activity getActivityByMall(final Integer mallId, final Integer activityId) {
        return activityRepository.selectActivityByMall(mallId, activityId);
    }

    public Activity getActivityById(final Integer activityId, final Integer memberId) {
        return activityRepository.selectActivityById(activityId, memberId);
    }

    public List<Activity> getActivityList(ActivityQueryCondition condition, int from) {
        return activityRepository.selectActivityList(condition, from);
    }

    public int saveActivitySignLog(ActivitySignUpLog activitySignUpLog) {
        return activityRepository.insertSignUpLog(activitySignUpLog);
    }

    public int editActivitySignLog(ActivitySignUpLog activitySignUpLog) {
        return activityRepository.updateSignUpLog(activitySignUpLog);
    }

    public int getSignUpCount(final Integer activityId) {
        return activityRepository.selectSignUpCount(activityId);
    }

    public int getMemberSignUp(final Integer activityId, final Integer memberId, final Integer signType) {
        return activityRepository.selectMemberSignType(activityId, memberId, signType);
    }

    public int getMemberSignUp(final Integer activityId, final Integer memberId) {
        return activityRepository.selectMemberSignUp(activityId, memberId);
    }

    public List<ReceiveCouponInfo> getIncentiveCouponInfoList(final Integer activityId) {
        return activityRepository.selectIncentiveCouponInfoList(activityId);
    }
}
