package com.laf.manager.querycondition.ticket;

import lombok.Data;

@Data
public class TicketNoPassSaveCondition {

    private int ticketId;

    private int vcId;

    private String vcName;

    private String vcPhone;

    private long handleDate;

    private String responses;

    private int handleStatus;
}
