package com.laf.manager.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Assessment {

    public interface AssessmentListView {}
    public interface AssessmentAppView extends AssessmentListView {}

    /**
     * 评价id(主键)
     */
    @JsonView(AssessmentListView.class)
    private int assessment_id;

    /**
     * 会员id
     */
    @JsonView(AssessmentListView.class)
    private int member_id;

    /**
     * 会员名称
     */
    @JsonView(AssessmentListView.class)
    private String member_name;

    /**
     * 会员手机号
     */
    @JsonView(AssessmentListView.class)
    private String member_mobile;

    /**
     * 评价时间
     */
    @JsonView(AssessmentListView.class)
    private Date action_time;

    /**
     * 商户id
     */
    @JsonView(AssessmentListView.class)
    private int shop_id;

    /**
     * 商户名称
     */
    @JsonView(AssessmentListView.class)
    private String shop_name;

    /**
     * 评价状态
     */
    @JsonView(AssessmentListView.class)
    private int status;

    /**
     * 回复
     */
    @JsonView(AssessmentAppView.class)
    private String reply;

    /**
     * 评论内容
     */
    @JsonView(AssessmentAppView.class)
    private String comments;

    /**
     * 相关商场
     */
    @JsonView(AssessmentListView.class)
    private int mall_id;

    public long getAction_time() {
        return action_time == null ? 0L : action_time.getTime();
    }
}
