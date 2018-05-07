package com.laf.mall.api.querycondition.ticket;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TicketNoQueryConditon {

    @NotNull(message = "交易单号不能为空")
    private String ticketNo;
}
