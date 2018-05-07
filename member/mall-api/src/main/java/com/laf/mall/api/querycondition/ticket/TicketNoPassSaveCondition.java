package com.laf.mall.api.querycondition.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketNoPassSaveCondition {

    private int ticketId;

    private int vcId;

    private String vcName;

    private String vcPhone;

    private long handleDate;

    private String responses;

    private int handleStatus;
}
