package com.laf.mall.api.querycondition.raffle;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RaffleRewardSaveCondition {

    @NotNull(message = "memberId不能为空")
    private Integer memberId;

    @NotNull(message = "mallId不能为空")
    private Integer mallId;

    @NotNull(message = "trrId不能为空")
    private Integer trrId;
}
