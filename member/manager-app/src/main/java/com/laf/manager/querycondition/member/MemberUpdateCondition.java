package com.laf.manager.querycondition.member;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
public class MemberUpdateCondition {

    private String name;

    private int sex = -1;

    private long birthday = 0L;

    private int occupation = -1;

    private String address;

    private int degreeOfEducation = -1;

    private int incomeRange = -1;

    private String interest;

    private Boolean enablePublicWa = null;

    @NotNull(message = "会员id必填")
    private Integer memberId;

    public void setEnablePublicWa(Boolean enablePublicWa) {
        this.enablePublicWa = enablePublicWa;
    }
    public Boolean getEnablePublicWa() {
        return this.enablePublicWa;
    }
}
