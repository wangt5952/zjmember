package com.laf.mall.api.service;

import com.laf.mall.api.dto.Activity;
import com.laf.mall.api.querycondition.activity.ActivityQueryCondition;

import java.util.List;

public interface ActivityService {

    /**
     * 获取活动详情
     * @param activityId
     * @return 券详情信息
     */
    Activity getActivityDetail(final Integer activityId, final Integer memberId);

    /**
     * 获取我的活动详情
     * @param activityId
     * @param memberId
     * @return 券详情信息
     */
    Activity getActivityDetail4Member(final Integer activityId, final Integer memberId);

    /**
     * 获取活动券列表
     * @param condition
     * @return 优惠券列表
     */
    List<Activity> getActivityList(ActivityQueryCondition condition);

    /**
     * 获取我的活动列表
     * @param condition
     * @return 优惠券列表
     */
    List<Activity> getActivityList4Member(ActivityQueryCondition condition);


    /**
     * 判断会员是否可以签到
     * @param memberId
     * @param activityId
     * @param mallId
     * @return
     */
   int enableSignIn(Integer memberId, Integer activityId, Integer mallId);

        /**
         * 活动报名
         * @param memberId 会员Id
         * @param activityId 活动Id
         * @return
         */
    int signUp(final Integer memberId, final Integer activityId);

    /**
     * 活动签到
     * @param memberId 会员Id
     * @param activityId 活动Id
     * @return
     */
    int signIn(final Integer memberId, final Integer activityId);

    /**
     * 活动签到
     * @param memberId
     * @param activityId
     * @param mallId
     * @return
     */
    int signIn(Integer memberId, Integer activityId, Integer mallId);
}
