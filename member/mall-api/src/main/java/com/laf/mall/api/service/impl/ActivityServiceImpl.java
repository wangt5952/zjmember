package com.laf.mall.api.service.impl;

import com.laf.mall.api.dao.ActivityDao;
import com.laf.mall.api.dao.CouponDao;
import com.laf.mall.api.dao.MemberDao;
import com.laf.mall.api.dto.Activity;
import com.laf.mall.api.dto.ActivitySignUpLog;
import com.laf.mall.api.dto.Member;
import com.laf.mall.api.dto.ReceiveCouponInfo;
import com.laf.mall.api.menu.PointsSources;
import com.laf.mall.api.querycondition.activity.ActivityQueryCondition;
import com.laf.mall.api.querycondition.coupon.CouponReceiveCondition;
import com.laf.mall.api.querycondition.points.PointsActionCondition;
import com.laf.mall.api.service.ActivityService;
import com.laf.mall.api.service.CouponService;
import com.laf.mall.api.service.PointsService;
import com.laf.mall.api.utils.datetime.DateTimeUtils;
import com.laf.manager.core.exception.MallDBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    ActivityDao activityDao;

    @Autowired
    PointsService pointsService;

    @Autowired
    MemberDao memberDao;

    @Autowired
    CouponService couponService;

    @Autowired
    DateTimeUtils dateTimeUtils;

    @Override
    public Activity getActivityDetail(Integer activityId, Integer memberId) {

        Activity activity = activityDao.getActivityById(activityId);
        if (activity == null) return null;

        long currTime = new Date().getTime();

        if (currTime > activity.getActivity_time_end()) {
            activity.setLimitPromptCode(0);//活动结束
            return activity;
        }

        if (activity.is_sign_up()) {
            int isSignUp = activityDao.getMemberSignUp(activityId, memberId);
            if (isSignUp > 0) {
                activity.setLimitPromptCode(1);//已报名
                return activity;
            }

            if (dateTimeUtils.getMilliWithoutTime(currTime) > activity.getSign_up_end()) {
                activity.setLimitPromptCode(2);//报名已截止
                return activity;
            }

            int signUpCount = activityDao.getSignUpCount(activityId);
            activity.setSign_up_residue(activity.getSign_up_limited() - signUpCount);
            if (activity.getSign_up_limited() <= signUpCount) {
                activity.setLimitPromptCode(3);//名额已满
                return activity;
            }

            int useablePoints = memberDao.getUsablePoints4Member(memberId);
            if (activity.getSign_up_points() > 0 && useablePoints < activity.getSign_up_points()) {
                activity.setLimitPromptCode(4);//积分不足
                return activity;
            }
            activity.setLimitPromptCode(5);//可报名
        } else {
            activity.setLimitPromptCode(6);//不用报名
        }
        return activity;
    }

    @Override
    public Activity getActivityDetail4Member(Integer activityId, Integer memberId) {
        return activityDao.getActivityById(activityId, memberId);
    }

    @Override
    public List<Activity> getActivityList(ActivityQueryCondition condition) {
        return activityDao.getActivityList(condition, 1);
    }

    @Override
    public List<Activity> getActivityList4Member(ActivityQueryCondition condition) {
        return activityDao.getActivityList(condition, 0);
    }

    /**
     * @param memberId
     * @param activityId
     * @param mallId
     * @return 0 会员不存在
     * 1 活动不存在
     * 2 未报名
     * 3 重复签到
     * 8 可以签到
     * 9 不可签到
     */
    @Override
    public int enableSignIn(Integer memberId, Integer activityId, Integer mallId) {
        Member member = memberDao.getMemberById(memberId, mallId);
        if (member == null) return 0; //会员不存在

        Activity activity = activityDao.getActivityByMall(mallId, activityId);
        if (activity == null) return 1; //活动不存在或不属于该商城

        if (!activity.is_sign_in()) return 9;

        int result = activityDao.getMemberSignUp(activityId, memberId);
        if (activity.is_sign_up()) { //需要报名的活动
            if (result <= 0) return 2; //未报名

            result = activityDao.getMemberSignUp(activityId, memberId, 2);
            if (result > 0) return 3; //重复签到

        } else { //不需要报名的活动
            if (result > 0) return 3; //重复签到
        }

        return 8;
    }

    @Override
    @Transactional
    public int signUp(Integer memberId, Integer activityId) throws MallDBException {
        Activity activity = activityDao.getActivityById(activityId, memberId);
        Member member = memberDao.getMemberById(memberId);
        if (member == null) return -3;

        if (activity == null) return -1; // 活动不存在

        if (!activity.is_sign_up()) return 0;

        ActivitySignUpLog activitySignUpLog = new ActivitySignUpLog();
        activitySignUpLog.setSign_type(1);
        activitySignUpLog.setMember_id(memberId);
        activitySignUpLog.setActivity_id(activityId);
        activitySignUpLog.setMall_id(activity.getMall_id());
        activitySignUpLog.setSign_up_time(new Date());

        int result = 0;

        if (activity.getSign_type() == 0) {
            int signUpCount = activityDao.getSignUpCount(activityId);

            if (activity.getSign_up_limited() <= signUpCount) return 0;

            result = activityDao.saveActivitySignLog(activitySignUpLog);

            if (result > 0 && activity.getSign_up_points() > 0) {
                result = savePoints(activity.getMall_id(), memberId, activity.getSign_up_points() * -1, PointsSources.ACTIVITY.$1);

                if (result <= 0) throw new MallDBException(-3);

                int newUseablePoints = member.getUsable_points() - activity.getSign_up_points();
                result = memberDao.updatePoints(memberId, member.getCumulate_points(), newUseablePoints);
            }

            if (result <= 0) throw new MallDBException(-3);

        } else {
            result = -2; // 重复报名
        }

        return result;
    }

    @Override
    public int signIn(Integer memberId, Integer activityId) {
        Activity activity = activityDao.getActivityById(activityId, memberId);

        if (activity == null) return -1; // 活动不存在

        ActivitySignUpLog activitySignUpLog = new ActivitySignUpLog();
        activitySignUpLog.setSign_type(2);
        activitySignUpLog.setMember_id(memberId);
        activitySignUpLog.setActivity_id(activityId);
        activitySignUpLog.setMall_id(activity.getMall_id());
        activitySignUpLog.setSign_in_time(new Date());

        int result = 0;

        if (activity.getSign_type() == 0) {
            if (!activity.is_sign_in()) return 0;

            result = activityDao.saveActivitySignLog(activitySignUpLog);

            if (result > 0 && activity.is_incentive()) {
                result = savePoints(activity.getMall_id(), memberId, activity.getIncentive_points(), PointsSources.ACTIVITY.$1);
            }
        } else if (activitySignUpLog.getSign_type() == 1) {
            activitySignUpLog.setSign_up_time(new Date(activity.getSign_up_time()));
            result = activityDao.editActivitySignLog(activitySignUpLog);
        } else {
            result = -2; // 重复签到
        }

        return result;
    }

    /**
     * 两种活动需要签到：1.不报名签到；2.报名签到
     *
     * @param memberId
     * @param activityId
     * @param mallId
     * @return 0 会员不存在
     * 1 活动不存在
     * 2 未报名
     * 3 重复签到
     * 4 签到失败(数据库操作失败，非逻辑错误)
     * 5 签到成功(没有奖励)
     * 6 签到送券成功
     * 7 签到送积分成功
     * 9 不可签到
     *
     * @throws MallDBException
     */
    @Override
    @Transactional
    public int signIn(Integer memberId, Integer activityId, Integer mallId) throws MallDBException {

        Member member = memberDao.getMemberById(memberId, mallId);
        if (member == null) return 0; //会员不存在,返回0

        Activity activity = activityDao.getActivityByMall(mallId, activityId);
        if (activity == null) return 1; //活动不存在或不属于该商城

        if (!activity.is_sign_in()) return 9;

        ActivitySignUpLog activitySignUpLog = new ActivitySignUpLog();
        activitySignUpLog.setSign_type(2);
        activitySignUpLog.setMember_id(memberId);
        activitySignUpLog.setActivity_id(activityId);
        activitySignUpLog.setMall_id(activity.getMall_id());
        activitySignUpLog.setSign_in_time(new Date());

        int result = 0;

        int signUp = activityDao.getMemberSignUp(activityId, memberId);
        if (activity.is_sign_up()) { //需要报名的活动
            if (signUp <= 0) return 2; //未报名

            signUp = activityDao.getMemberSignUp(activityId, memberId, 2);
            if (signUp > 0) return 3; //重复签到

            result = activityDao.editActivitySignLog(activitySignUpLog);

        } else { //不需要报名的活动
            if (signUp > 0) return 3; //重复签到

            result = activityDao.saveActivitySignLog(activitySignUpLog);
        }

        if (result <= 0) return 4;

        int code = 5;
        List<ReceiveCouponInfo> list = activityDao.getIncentiveCouponInfoList(activityId);
        for (ReceiveCouponInfo info : list) {
            CouponReceiveCondition receiveCondition = new CouponReceiveCondition();
            receiveCondition.setCouponId(info.getCoupon_id());
            receiveCondition.setCouponStatus(1);
            receiveCondition.setMallId(info.getMall_id());
            receiveCondition.setMemberId(memberId);
            receiveCondition.setSources(2);
            receiveCondition.setSourceId(activityId);
            result = couponService.saveCouponForMember(receiveCondition);
            if (result <= 0) throw new MallDBException(4);
        }
        if (list.size() > 0) code = 6;

        if (activity.getIncentive_points() > 0) {
            result = savePoints(activity.getMall_id(), memberId, activity.getIncentive_points(), PointsSources.ACTIVITY.$1);
            if (result <= 0) throw new MallDBException(4);

            int newCumulatePoints = member.getCumulate_points() + activity.getIncentive_points();
            int newUseablePoints = member.getUsable_points() + activity.getIncentive_points();
            result = memberDao.updatePoints(memberId, newCumulatePoints, newUseablePoints);

            if (result <= 0) throw new MallDBException(4);
            else code = 7;
        }
        return code;
    }

    private int savePoints(int mallId, int memberId, int points, int sources) {
        Member member = memberDao.getMemberById(memberId);

        if (member == null) return 0;

        PointsActionCondition condition = new PointsActionCondition();
        condition.setMallId(mallId);
        condition.setMemberId(memberId);
        condition.setMemberMobile(member.getMobile());
        condition.setMemberName(member.getName());
        condition.setPoints(points);
        condition.setSources(sources);
        condition.setHandleDate(new Date());

        return pointsService.addPointsExcludeConsume(condition);
    }
}
