package com.laf.mall.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Member {

    public interface MemberAppView {};
    public interface MemberAllView extends MemberAppView {};

    @JsonView(MemberAppView.class)
    private int member_id;

    /**
     * 姓名
     */
    @JsonView(MemberAppView.class)
    private String name;

    /**
     * 会员手机号
     */
    @JsonView(MemberAppView.class)
    private String mobile;
    /**
     * 性别
     */
    @JsonView(MemberAppView.class)
    private int sex;

    /**
     * 生日
     */
    @JsonView(MemberAppView.class)
    private Date birthday;

    /**
     * 职业
     */
    @JsonView(MemberAppView.class)
    private int occupation;

    /**
     * 地址
     */
    @JsonView(MemberAppView.class)
    private String address;

    /**
     * 教育程度
     */
    @JsonView(MemberAppView.class)
    private int degree_of_education;

    /**
     * 收入范围
     */
    @JsonView(MemberAppView.class)
    private int income_range;

    /**
     * 兴趣爱好
     */
    @JsonView(MemberAppView.class)
    private String interest;

    /**
     * 微信号
     */
    @JsonView(MemberAppView.class)
    private String wechat_account;

    /**
     * 微信号是否公开
     */
    @JsonView(MemberAppView.class)
    private boolean enable_public_wa;

    /**
     * 累计积分
     */
    @JsonView(MemberAppView.class)
    private int cumulate_points;

    /**
     * 可用积分
     */
    @JsonView(MemberAppView.class)
    private int usable_points;

    @JsonView(MemberAllView.class)
    private int mall_id;

    /**
     * 累计消费
     */
    @JsonView(MemberAppView.class)
    private BigDecimal cumulate_amount;

    /**
     * 会员等级(名称)
     */
    @JsonView(MemberAppView.class)
    private String level;

    /**
     * 会员等级(id)
     */
    @JsonView(MemberAppView.class)
    private int level_id;

    /**
     * 微信公共号openId
     */
    @JsonView(MemberAppView.class)
    private String open_id;

    /**
     * 会员卡号
     */
    @JsonView(MemberAppView.class)
    private String member_card_no;

    /**
     * 注册时间
     */
    @JsonView(MemberAppView.class)
    private Date regist_date;

    /**
     * Email
     */
    private String email;

    /**
     * 信息修改次数
     */
    @JsonView(MemberAppView.class)
    private int edit_flag;

    /**
     * 会员生日修改次数
     */
    @JsonView(MemberAppView.class)
    private int birthday_modified;

    public long getRegist_date() {
        return this.regist_date == null ?
            new Date().getTime() : this.regist_date.getTime();
    }

    public long getBirthday() {
        return this.birthday == null ?
            new Date().getTime() : this.birthday.getTime();
    }
}
