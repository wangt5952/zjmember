package com.laf.manager.querycondition.ticket;

import com.laf.manager.utils.datetime.DateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketSaveCondition {

    @NotNull(message = "ticketId不能为空")
    private Integer ticketId;

    private Integer memberId;

    private Integer levelId;

    private Integer shopId;

//    private Integer mallId;

    private Long birthday;

    private String ticketNo;

    private String shopName;

    private Long shoppingDate;

    private BigDecimal amount;

    private String responses;

    private Integer cumulatePoints;

    private Integer usablePoints;

    private Integer points;

    private BigDecimal cumulateAmount;

    public Long getShoppingDate() {
        if (shoppingDate != null) return new DateTimeUtils().getMilliWithoutTime(shoppingDate);
        return null;
    }
}
