package com.laf.manager.querycondition.activity;

import lombok.Data;

import java.util.Date;

@Data
public class ActivityQueryCondition {

    private int mallId = 0;

//    private String keywords;

    private String title = "";

    /**
     * 0 未开始
     * 1 进行中
     * 2 已结束
     */
    private Integer process = -1;

    /**
     * 0 报名
     * 1 签到
     */
    private Integer signInOrSignUp = -1;

    private Integer signUpCountMin = 0;

    private Integer signUpCountMax = 0;

    private Integer signInCountMin = 0;

    private Integer signInCountMax = 0;

    private Integer isIncentive = -1;

    private int size = 10;

    private int page = 1;

    private String sort = "activity_time_end";

    private String direction = "desc";

    public int getOffset() {
        return (page - 1) / size;
    }

    public long getCurrTime() {
        return new Date().getTime();
    }
}
