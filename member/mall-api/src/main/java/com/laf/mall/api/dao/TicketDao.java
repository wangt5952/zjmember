package com.laf.mall.api.dao;

import com.laf.mall.api.dto.Ticket;
import com.laf.mall.api.querycondition.ticket.TicketNoPassSaveCondition;
import com.laf.mall.api.querycondition.ticket.TicketNoQueryConditon;
import com.laf.mall.api.querycondition.ticket.TicketQueryCondition;
import com.laf.mall.api.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TicketDao {

    @Autowired
    TicketRepository repository;

    public int saveTicket(final Ticket ticket) {
        return repository.insertTicket(ticket);
    }

    public int removeTicketById(final Integer ticketId) {
        return repository.deleteTicketBy(ticketId);
    }

    public List<Ticket> getTicketListByMemberId(final TicketQueryCondition condition) {
        return repository.selectTicketListByMemberId(condition);
    }

    public int updateTicketForNoPass(TicketNoPassSaveCondition condition) {
        return repository.updateTicketForNoPass(condition);
    }

    public int getTicketByTicketNoCount(TicketNoQueryConditon condition) {
        return repository.selectTicketByTicketNoCount(condition);
    }
}
