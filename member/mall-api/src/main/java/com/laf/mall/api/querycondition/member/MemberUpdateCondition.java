package com.laf.mall.api.querycondition.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ToString
public class MemberUpdateCondition {

    private String name;

    private int sex = -1;

    private long birthday = 0L;

    @ApiModelProperty(value = "职业")
    private int occupation = -1;

    private String address;

    @ApiModelProperty(value = "楼层ID")
    private int degreeOfEducation = -1;

    @ApiModelProperty(value = "收入范围")
    private int incomeRange = -1;

    private String interest;

    @ApiModelProperty(value = "是否允许公开微信号")
    private Boolean enablePublicWa = null;

    @NotNull(message = "会员id必填")
    private Integer memberId;

    private String wechatAccount;

    private String mobile;

    public void setEnablePublicWa(Boolean enablePublicWa) {
        this.enablePublicWa = enablePublicWa;
    }
    public Boolean getEnablePublicWa() {
        return this.enablePublicWa;
    }
}
