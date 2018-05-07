package com.laf.manager.service.impl;

import com.laf.manager.SettingsProperties;
import com.laf.manager.core.exception.MallDBException;
import com.laf.manager.dao.ActivityDao;
import com.laf.manager.dao.ArticlesDao;
import com.laf.manager.dto.Activity;
import com.laf.manager.dto.ActivitySignUpLog;
import com.laf.manager.querycondition.activity.ActivityEditCondition;
import com.laf.manager.querycondition.activity.ActivityLogQueryCondition;
import com.laf.manager.querycondition.activity.ActivityQueryCondition;
import com.laf.manager.service.ActivityService;
import com.laf.manager.utils.file.FileProperties;
import com.laf.manager.utils.qrcode.QRCode;
import com.laf.manager.utils.qrcode.WechatProperties;
import com.laf.manager.utils.qrcode.ZxingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ActivityServiceImpl implements ActivityService {


    @Autowired
    ActivityDao activityDao;

    @Autowired
    ArticlesDao articlesDao;

    @Autowired
    SettingsProperties settingsProperties;

    @Autowired
    FileProperties fileProperties;

    @Autowired
    WechatProperties wechatProperties;

    @Override
    public List<Activity> getActivityList(ActivityQueryCondition condition) {
        condition.setMallId(settingsProperties.getMallId());
        List<Activity> list = activityDao.getActivityList(condition);

        return list;
    }

    @Override
    public Activity getActivityById(Integer activityId) {

        return activityDao.getActivityById(activityId);
    }

    @Override
    @Transactional
    public int editActivity(ActivityEditCondition condition) throws MallDBException {
        int result;
        Activity activity = activityDao.getActivityById(condition.getActivityId());

        if (activity == null) {
            activity = new Activity();

            activity.setMall_id(settingsProperties.getMallId());
            activity.setIncentive_points(condition.getIncentivePoints());
            activity.setStatus(condition.getStatus());
            activity.set_incentive(true);
            activity.setSign_up_points(condition.getSignUpPoints());
            activity.setIs_sign_up(condition.getIsSignUp());
            activity.setActivity_time_end(condition.getActivityTimeEnd());
            activity.setActivity_time_start(condition.getActivityTimeStart());
            activity.setTitle(condition.getTitle());
            activity.set_comment(condition.getIsComment());
            activity.setIs_sign_in(condition.getIsSignIn());
            activity.setAddress(condition.getAddress());
            activity.setPicture(condition.getPicture());
//            activity.setQr_code(condition.getQrCode());
            activity.setSign_up_end(condition.getSignUpEnd());
            activity.setSign_up_limited(condition.getSignUpLimited());
            activity.setIntro(condition.getIntro());

            int newId = articlesDao.saveArticle(condition.getIntro(), settingsProperties.getMallId());
            if (newId <= 0) throw new MallDBException();

            activity.setIntro_id(newId);
            result = activityDao.saveActivity(activity);

            if (result <= 0) throw new MallDBException();

            activity.setActivity_id(result);
            if (activity.isIs_sign_in()) {

                int created = createQRCode(activity);
                if (created == -1) throw new MallDBException();
log.info("[{}]", activity.getQr_code());
                result = activityDao.updateActivityQRCode(activity.getActivity_id(), activity.getQr_code());
                if (result <= 0) throw new MallDBException();
            }

        } else {
            activity.setMall_id(settingsProperties.getMallId());
            activity.setIncentive_points(condition.getIncentivePoints());
            activity.setStatus(condition.getStatus());
            activity.set_incentive(true);
            activity.setSign_up_points(condition.getSignUpPoints());
            activity.setIs_sign_up(condition.getIsSignUp());
            activity.setActivity_time_end(condition.getActivityTimeEnd());
            activity.setActivity_time_start(condition.getActivityTimeStart());
            activity.setTitle(condition.getTitle());
            activity.set_comment(condition.getIsComment());
            activity.setAddress(condition.getAddress());
            if(activity.isIs_sign_in() != condition.getIsSignIn()) {
                if (condition.getIsSignIn()) {
                    result = createQRCode(activity);
                    if (result == -1) throw new MallDBException();
                }
            }
            activity.setIs_sign_in(condition.getIsSignIn());
            log.info("[{}]", activity.getQr_code());

            if (!StringUtils.isEmpty(condition.getPicture())) activity.setPicture(condition.getPicture());

            activity.setSign_up_end(condition.getSignUpEnd());
            activity.setSign_up_limited(condition.getSignUpLimited());
            activity.setIntro(condition.getIntro());

            result = activityDao.updateActivity(activity);
            if (result <= 0) throw new MallDBException();

            result = articlesDao.editArticle(activity.getIntro_id(), condition.getIntro());
            if (result <= 0) throw new MallDBException();

        }

        return result;
    }

    @Override
    @Transactional
    public int delActivity(Integer activityId) throws MallDBException {
        Activity activity = activityDao.getActivityById(activityId);

        if (activity == null) {
            return 0;
        }

        int result = activityDao.deleteAtivity(activityId);
        if (result <= 0) throw new MallDBException();

        result = articlesDao.deleteArticle(activity.getIntro_id());
        if (result <= 0) throw new MallDBException();

        return result;
    }

    @Override
    public int getActivityCount(ActivityQueryCondition condition) {
        condition.setMallId(settingsProperties.getMallId());
        return activityDao.getActivityCount(condition);
    }

    @Override
    public List<Integer> getActivityCountMonthly(String year) {
        return activityDao.getActivityCountMonthly(year, settingsProperties.getMallId());
    }

    @Override
    public List<ActivitySignUpLog> getActivityLogList(ActivityLogQueryCondition condition) {
        return activityDao.getActivityLogList(condition);
    }

    @Override
    public int getActivityLogCount(ActivityLogQueryCondition condition) {
        return activityDao.getActivityLogCount(condition);
    }

    private int createQRCode(Activity activity) {
        QRCode qrCode = new QRCode();
        qrCode.setWidth(300);
        qrCode.setHeight(300);
        qrCode.setFormat("png");
        qrCode.setContent(wechatProperties.getDomain() + wechatProperties.getPath() + "?activityId=" + activity.getActivity_id());
        String folder = fileProperties.getPath();
        String fileName = "qr_activity_" + new Date().getTime() + "." + qrCode.getFormat();
        qrCode.setFileName(fileName);
        qrCode.setFolder(folder);

        String url = fileProperties.getDomain() + fileProperties.getBase() + fileName;
        activity.setQr_code(url);
        return ZxingUtils.createQRCode(qrCode);
    }
}
