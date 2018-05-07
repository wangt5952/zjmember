package com.laf.manager.service;

import com.laf.manager.dto.Ticket;
import com.laf.manager.querycondition.ticket.*;

import java.util.List;

public interface TicketService {

    /**
     * 获取小票列表
     * @param condition
     * @return
     */
    List<Ticket> getTicketList(TicketFilterCondition condition);

    /**
     * 处理小票，包括计算积分，小票信息的录入
     * @param condition
     * @return
     */
    int operateTicket(TicketSaveCondition condition);

    int operateHandlePoints(TicketSaveCondition condition);

    /**
     * 计算消费金额能够兑换的积分
     * @param condition
     * @return
     */
    int getPoints(PointsGotCondition condition);

    /**
     * 删除小票
     * @param ticketId
     * @return
     */
    int removeTicketById(final Integer ticketId);

    /**
     * 处理未通过小票
     * @param condition
     * @return
     */
    int operateTicketForNoPass(TicketNoPassSaveCondition condition);

    Ticket getTicketDetail(final Integer ticketId);

    int getTicketsCount();

    int getMultipleTicketsCount(TicketFilterCondition condition);

    int getTicketByTicketNoCount(final String ticketNo);

}
