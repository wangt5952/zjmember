package com.laf.manager.querycondition.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberFilterCondition {

    private String username = "";

    private String mobile = "";

    private String carNumber = "";

    private long registerDateStart;

    private long registerDateEnd;

    private Integer level = -1;

    private Integer occupation = -1;

    private BigDecimal cumulateAmountStart;

    private BigDecimal cumulateAmountEnd;

    private int cumulatePointsStart;

    private int cumulatePointsEnd;

    private long birthdayStart;

    private long birthdayEnd;

    private int status = -1;

    private int mallId = 0;

    private int size = 10;

    private int page = 1;

//    public Long getRegisterDateStart() {
//        return registerDateStart == null? 0L : registerDateStart;
//    }
//
    public long getRegisterDateEnd() {
        return registerDateEnd + getTime();
    }
//
//    public Long getBirthdayStart() {
//        return birthdayStart == null ? 0L : birthdayStart;
//    }
//
    public long getBirthdayEnd() {
        return birthdayEnd + getTime();
    }

    public BigDecimal getCumulateAmountStart() {
        return cumulateAmountStart == null ? new BigDecimal(0D) : cumulateAmountStart;
    }

    public BigDecimal getCumulateAmountEnd() {
        return cumulateAmountEnd == null ? new BigDecimal(0D) : cumulateAmountEnd;
    }

    public int getOffset() {
        return (page - 1) * size;
    }

    private long getTime() {
        long time = (((23 * 60 + 59) * 60) + 59) * 1000;

        return time;
    }
}
