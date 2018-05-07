package com.laf.manager.dao;

import com.laf.manager.dto.Ticket;
import com.laf.manager.querycondition.ticket.TicketFilterCondition;
import com.laf.manager.querycondition.ticket.TicketNoPassSaveCondition;
import com.laf.manager.querycondition.ticket.TicketQueryCondition;
import com.laf.manager.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TicketDao {

    @Autowired
    TicketRepository repository;

    public int saveMultipleTicket(final Ticket ticket) {
        return repository.insertMultipleTicket(ticket);
    }

    public int removeTicketById(final Integer ticketId) {
        return repository.deleteTicketBy(ticketId);
    }

    public int updateTicket(final Ticket ticket) {
        return repository.updateTicket(ticket);
    }

    public int updateTicketForNoPass(TicketNoPassSaveCondition condition) {
        return repository.updateTicketForNoPass(condition);
    }

    public Ticket getTicketById(final Integer ticketId) {
        Ticket $ = null;

        try {
            $ = repository.selectTicketById(ticketId);
        } catch (EmptyResultDataAccessException e) {
            $ = null;
        }

        return $;
    }

    public int getTicketsCount(final Integer mallId) {
        return repository.selectTicketsCount(mallId);
    }

    public List<Ticket> multipleQuery(final TicketFilterCondition condition) {
        List<Ticket> $ = null;

        try {
            $ = repository.multipleSelectTickets(condition);
        } catch (EmptyResultDataAccessException e) {
            $ = new ArrayList<>();
        }

        return $;
    }

    public int multipleTicketsCount(final TicketFilterCondition condition) {
        return repository.multipleSelectTicketsCount(condition);
    }

    public int getTicketByTicketNoCount(final String ticketNo) {
        return repository.selectTicketByTicketNoCount(ticketNo);
    }
}
