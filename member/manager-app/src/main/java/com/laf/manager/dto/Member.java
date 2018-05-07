package com.laf.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Member {

    private int member_id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 会员手机号
     */
    private String mobile;
    /**
     * 性别
     */
    private int sex;
    private String sex_name;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 职业
     */
    private int occupation;

    /**
     * 地址
     */
    private String address;

    /**
     * 教育程度
     */
    private int degree_of_education;

    /**
     * 收入范围
     */
    private int income_range;

    /**
     * 兴趣爱好
     */
    private String interest;

    /**
     * 微信号
     */
    private String wechat_account;

    /**
     * 微信号是否公开
     */
    private boolean enable_public_wa;

    /**
     * 累计积分
     */
    private int cumulate_points;

    /**
     * 可用积分
     */
    private int usable_points;

    private int mall_id;

    /**
     * 累计消费
     */
    private BigDecimal cumulate_amount;

    /**
     * 会员等级(名称)
     */
    private String level;

    /**
     * 会员等级(id)
     */
    private int level_id;

    /**
     * 微信公共号openId
     */
    private String open_id;

    /**
     * 会员卡号
     */
    private String member_card_no;

    /**
     * 注册时间
     */
    private Date regist_date;

    /**
     * Email
     */
    private String email;

    /**
     * 信息修改次数
     */
    private int edit_flag;

    private int status;

    /**
     * 会员生日修改次数
     */
    private int birthday_modified;

    public long getRegist_date() {
        return this.regist_date == null ?
            new Date().getTime() : this.regist_date.getTime();
    }

    public long getBirthday() {
        return this.birthday == null ?
            new Date().getTime() : this.birthday.getTime();
    }

    public String getSex_name() {
        return sex == 0 ? "男" : "女";
    }
}
