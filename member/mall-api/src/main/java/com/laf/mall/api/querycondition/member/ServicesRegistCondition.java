package com.laf.mall.api.querycondition.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ServicesRegistCondition {

    /**
     * 姓名
     */
    @NotNull(message = "姓名不能为空")
    @ApiModelProperty(value = "姓名")
    private String name;

    /**
     * 联系电话
     */
    @NotNull(message = "联系电话不能为空")
    @ApiModelProperty(value = "联系电话")
    private String phone;

    /**
     * 部门
     */
    @NotNull(message = "部门不能为空")
    @ApiModelProperty(value = "部门")
    private Integer department;

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
}
