package com.laf.mall.api.querycondition.raffle;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MyRewardQueryCondition {

    @NotNull(message = "memberId不能为空")
    private int memberId;

    private Integer page = 1;

    private Integer size = 5;

    public int getOffset() {
        return (page - 1) * 5;
    }
}
