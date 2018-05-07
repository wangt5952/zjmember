package com.laf.mall.api.querycondition.points;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class PointsPayCashCondition {

    @NotNull(message = "shopId不能为空")
    @Min(value= 1, message = "shopId无效")
    private Integer shopId;

    @NotNull(message = "points不能为空")
    private Integer points;

    @NotNull(message = "mallId不能为空")
    private Integer mallId;

    public Date getCurrTime() {
        return new Date();
    }
}
