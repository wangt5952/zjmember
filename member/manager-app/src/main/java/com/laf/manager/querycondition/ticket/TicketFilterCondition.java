package com.laf.manager.querycondition.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketFilterCondition {

    private String username = "";

    private String mobile = "";

    private String ticketNo = "";

    private Long createDateStart = 0L;

    private Long createDateEnd = 0L;

    private Integer shop = -1;

    private Integer status = -1;

    private BigDecimal amountStart = new BigDecimal(0D);

    private BigDecimal amountEnd = new BigDecimal(0D);

    private Long actionDateStart = 0L;

    private Long actionDateEnd = 0L;

    private Long handleDateStart = 0L;

    private Long handleDateEnd = 0L;

    private Integer mallId = 0;

    private int size = 10;

    private int page = 1;

    public Long getCreateDateStart() {
        return createDateStart == null? 0L : createDateStart;
    }

    public Long getCreateDateEnd() {
        return createDateEnd == null ? 0L : createDateEnd + getTime();
    }

    public Long getActionDateStart() {
        return actionDateStart == null ? 0L : actionDateStart;
    }

    public Long getActionDateEnd() {
        return actionDateEnd == null ? 0L : actionDateEnd + getTime();
    }

    public Long getHandleDateStart() {
        return handleDateStart == null ? 0L : handleDateStart;
    }

    public Long getHandleDateEnd() {
        return handleDateEnd == null ? 0L : handleDateEnd + getTime();
    }

    public int getOffset() {
        return (page - 1) * size;
    }

    private long getTime() {
        long time = (((23 * 60 + 59) * 60) + 59) * 1000;

        return time;
    }
}
