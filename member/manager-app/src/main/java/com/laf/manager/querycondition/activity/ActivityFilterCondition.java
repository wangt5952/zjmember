package com.laf.manager.querycondition.activity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityFilterCondition {

    // TODO: 2017/12/17 活动筛选相关
    private String username = "";

    private String mobile = "";

    private Long registerDateStart = 0L;

    private Long registerDateEnd = 0L;

    private Integer level = -1;

    private Integer occupation = -1;

    private BigDecimal cumulateAmountStart = new BigDecimal(0D);

    private BigDecimal cumulateAmountEnd = new BigDecimal(0D);

    private Integer cumulatePointsStart = 0;

    private Integer cumulatePointsEnd = 0;

    private Long birthdayStart = 0L;

    private Long birthdayEnd = 0L;

    private Integer mallId = 0;

    private int size = 10;

    private int page = 1;

    public Long getRegisterDateStart() {
        return registerDateStart == null? 0L : registerDateStart;
    }

    public Long getRegisterDateEnd() {
        return registerDateEnd == null ? 0L : registerDateEnd + getTime();
    }

    public Long getBirthdayStart() {
        return birthdayStart == null ? 0L : birthdayStart;
    }

    public Long getBirthdayEnd() {
        return birthdayEnd == null ? 0L : birthdayEnd + getTime();
    }

    public int getOffset() {
        return (page - 1) * size;
    }

    private long getTime() {
        long time = (((23 * 60 + 59) * 60) + 59) * 1000;

        return time;
    }
}
