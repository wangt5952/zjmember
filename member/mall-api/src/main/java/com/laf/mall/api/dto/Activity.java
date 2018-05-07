package com.laf.mall.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.mysql.jdbc.util.Base64Decoder;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Activity {

    public interface ActivityListView {};
    public interface ActivityAppView extends ActivityListView {};

    @JsonView(ActivityListView.class)
    private int activity_id;

    @JsonView(ActivityListView.class)
    private String title;

    @JsonView(ActivityListView.class)
    private String picture;

    @JsonView(ActivityListView.class)
    private Date activity_time_start;

    public long getActivity_time_start() {
        return activity_time_start == null ?
                new Date().getTime() : activity_time_start.getTime();
    }

    @JsonView(ActivityListView.class)
    private Date activity_time_end;

    public long getActivity_time_end() {
        return activity_time_end == null ?
                new Date().getTime() : activity_time_end.getTime();
    }

    @JsonView(ActivityAppView.class)
    private String address;

    /**
     * 是否需要报名
     */
    @JsonView(ActivityListView.class)
    private boolean is_sign_up;

    /**
     * 报名截止日期
     */
    @JsonView(ActivityAppView.class)
    private Date sign_up_end;

    public long getSign_up_end() {
        return sign_up_end == null ?
                new Date().getTime() : sign_up_end.getTime();
    }

    /**
     * 报名限制人数
     */
    @JsonView(ActivityAppView.class)
    private int sign_up_limited;

    /**
     * 报名剩余人数
     */
    @JsonView(ActivityAppView.class)
    private int sign_up_residue;

    /**
     * 报名所需积分
     */
    @JsonView(ActivityAppView.class)
    private int sign_up_points;

    /**
     * 是否可以评论
     */
    @JsonView(ActivityAppView.class)
    private boolean is_comment;

    /**
     * 是否需要签到
     */
    @JsonView(ActivityListView.class)
    private boolean is_sign_in;

    /**
     * 是否有奖励
     */
    @JsonView(ActivityAppView.class)
    private boolean is_incentive;

    private int intro_id;

    @JsonView(ActivityAppView.class)
    private String intro;

    @JsonView(ActivityListView.class)
    private int status;

    /**
     * 活动奖励积分
     */
    @JsonView(ActivityAppView.class)
    private int incentive_points;

    private int sign_count;

    @JsonView(ActivityAppView.class)
    private String qr_code;

    @JsonView(ActivityAppView.class)
    private int mall_id;

    @JsonView(ActivityAppView.class)
    private int residue;

    /**
     * 报名时间
     */
    @JsonView(ActivityAppView.class)
    private Date sign_up_time;

    /**
     * 签到时间
     */
    @JsonView(ActivityAppView.class)
    private Date sign_in_time;

    /**
     * 报名类型 0:已报名(未签到) 1:已签到(已报名&已签到，不需要报名&已签到)
     */
    @JsonView(ActivityListView.class)
    private int sign_type;

    /**
     * 限制报名提示码
     */
    @JsonView(ActivityAppView.class)
    private int limitPromptCode;

    public long getSign_up_time() {
        return sign_up_time == null ?
                new Date().getTime() : sign_up_time.getTime();
    }

    public long getSign_in_time() {
        return sign_in_time == null ?
                new Date().getTime() : sign_in_time.getTime();
    }
}
