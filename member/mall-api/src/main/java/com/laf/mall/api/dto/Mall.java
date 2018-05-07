package com.laf.mall.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Mall {
    public interface MallAppView {};
    public interface MallAllView extends MallAppView {};

    @JsonView(MallAppView.class)
    private int mall_id;

    /**
     * 商场编号
     */
    @JsonView(MallAllView.class)
    private String mall_code;

    /**
     * 商场名称
     */
    @JsonView(MallAppView.class)
    private String mall_name;

    /**
     * 商场联系电话
     */
    @JsonView(MallAppView.class)
    private String mall_phone;

    /**
     * 所属地区，外键id
     */
    @JsonView(MallAppView.class)
    private String area;

    /**
     * 详细地址
     */
    @JsonView(MallAppView.class)
    private String address;

    /**
     * 负责人
     */
    @JsonView(MallAllView.class)
    private String manager;

    /**
     * 负责人电话
     */
    @JsonView(MallAllView.class)
    private String manager_phone;

    /**
     * 轮播图，json数组
     */
    @JsonView(MallAppView.class)
    private String pictures;

    /**
     * 官方首页
     */
    @JsonView(MallAppView.class)
    private String home;

    /**
     * 商场简介，文章，外键
     */
    @JsonView(MallAppView.class)
    private int intro_id;

    /**
     * 商场简介，文章，可转发
     */
    @JsonView(MallAppView.class)
    private String intro;

    /**
     * 经度
     */
    @JsonView(MallAppView.class)
    private double longitude;

    /**
     * 纬度
     */
    @JsonView(MallAppView.class)
    private double latitude;

    /**
     * 营业时间
     */
    @JsonView(MallAppView.class)
    private String business_hours;

    /**
     * 微信AppID
     */
    @JsonView(MallAppView.class)
    private String appId;

    /**
     * 微信AppSecret
     */
    @JsonView(MallAppView.class)
    private String appSecret;

    /**
     * 所属集团，外键id
     */
    @JsonView(MallAllView.class)
    private int group_id;

    /**
     * 排序字段
     */
    @JsonView(MallAppView.class)
    private int sort;
}
