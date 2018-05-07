package com.laf.mall.api.querycondition.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MemberQueryCondition {

    @ApiModelProperty(value = "会员ID")
    private int memberId;

    @ApiModelProperty(value = "商场ID")
    private int mallId = 1;

}
