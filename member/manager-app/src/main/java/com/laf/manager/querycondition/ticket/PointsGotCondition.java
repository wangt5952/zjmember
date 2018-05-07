package com.laf.manager.querycondition.ticket;

import com.laf.manager.utils.datetime.DateTimeUtils;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PointsGotCondition {

    private Integer levelId;

    private Integer shopId;

    private Long birthday;

    private BigDecimal amount;

    private Long shoppingDate;

    public Long getShoppingDate() {
        return new DateTimeUtils().getMilliWithoutTime(shoppingDate);
    }
}
