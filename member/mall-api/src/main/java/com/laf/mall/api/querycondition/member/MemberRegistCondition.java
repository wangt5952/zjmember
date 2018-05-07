package com.laf.mall.api.querycondition.member;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MemberRegistCondition {

    @NotNull(message = "会员名称必填")
    private String name;

    @NotNull(message = "会员性别必填")
    private Integer sex;

    @NotNull(message = "会员生日必填")
    private Long birthday;

    @NotNull(message = "会员手机必填")
    private String mobile;

    @NotNull(message = "会员openId必填")
    private String openId;

    @NotNull(message = "商场Id必填")
    private Integer mallId;
}
