package com.laf.mall.api.querycondition.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ClerkRegistCondition {
    /**
     * 姓名
     */
    @NotNull(message = "用户名不能为空")
    @ApiModelProperty(value = "姓名")
    private String name;

    /**
     * 联系电话
     */
    @NotNull(message = "联系电话不能为空")
    @ApiModelProperty(value = "联系电话")
    private String phone;

    /**
     * 商户Id
     */
    @NotNull(message = "商户Id不能为空")
    @ApiModelProperty(value = "商户Id")
    private Integer shopId;

    /**
     * 会员Id
     */
    @NotNull(message = "会员Id不能为空")
    @ApiModelProperty(value = "会员Id")
    private Integer memberId;

    /**
     * 商场Id
     */
    @NotNull(message = "商场员Id不能为空")
    @ApiModelProperty(value = "商场Id")
    private Integer mallId;

    @ApiModelProperty(value = "是否是店铺管理者")
    private boolean isManager = false;
}
