package com.laf.mall.api.querycondition.ticket;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class TicketUploadCondition {

    @NotNull(message = "商场id必填")
    private Integer mallId;

    @NotNull(message = "商户id必填")
    private Integer shopId;

    @NotNull(message = "交易时间必填")
    private Long shoppingDate;

    @NotNull(message = "交易金额必填")
    private BigDecimal amount;

    @NotNull(message = "获得积分必填")
    private Integer points = 0;

    public int getPoints() {
        return points == null ? 0 : points.intValue();
    }
}
