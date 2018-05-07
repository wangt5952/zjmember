package com.laf.mall.api.service;

import com.laf.mall.api.querycondition.ticket.TicketNoPassSaveCondition;
import com.laf.mall.api.querycondition.ticket.TicketNoQueryConditon;

public interface TicketService {

    /**
     * 处理未通过小票
     * @param condition
     * @return
     */
    int operateTicketForNoPass(TicketNoPassSaveCondition condition);

    int getTicketByTicketNoCount(TicketNoQueryConditon conditon);
}
