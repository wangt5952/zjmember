package com.laf.manager.querycondition.member;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ClerkRegistCondition {
    /**
     * 姓名
     */
    @NotNull(message = "用户名不能为空")
    private String name;

    /**
     * 联系电话
     */
    @NotNull(message = "联系电话不能为空")
    private String phone;

    /**
     * 商户Id
     */
    @NotNull(message = "商户Id不能为空")
    private Integer shopId;

    /**
     * 会员Id
     */
    @NotNull(message = "会员Id不能为空")
    private Integer memberId;

    /**
     * 商场Id
     */
    @NotNull(message = "商场员Id不能为空")
    private Integer mallId;

    private boolean isManager = false;
}
