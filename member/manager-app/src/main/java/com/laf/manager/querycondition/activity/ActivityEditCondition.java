package com.laf.manager.querycondition.activity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Data
@Slf4j
public class ActivityEditCondition {

    private Integer activityId;

    private String title;

    private String picture;

    private Date activityTimeStart;

    private Date activityTimeEnd;

    private String address;

    private Boolean isSignUp;

    private Date signUpEnd;

    private Integer signUpLimited;

    private Integer signUpPoints;

    private Boolean isComment;

    private Boolean isSignIn;

//    private Boolean isIncentive;

    private String intro;

    private int status;

    private Integer incentivePoints;

    private String qrCode;

    public void setActivityTimeStart(long activityTimeStart) {
        this.activityTimeStart = new Date(activityTimeStart);
    }

    public void setActivityTimeEnd(long activityTimeEnd) {
        this.activityTimeEnd = new Date(activityTimeEnd);
    }

    public void setSignUpEnd(long signUpEnd) {
        this.signUpEnd = new Date(signUpEnd);
    }

    public Integer getActivityId() {
        return activityId == null ? 0 : activityId;
    }

    public Integer getStatus() {
        return 1;
    }

    public Integer getSignUpPoints() {
        return signUpPoints == null ? 0 : signUpPoints;
    }

    public Integer getSignUpLimited() {
        return signUpLimited == null ? 0 : signUpLimited;
    }
}
