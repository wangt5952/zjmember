package com.laf.mall.api.service.impl;

import com.laf.mall.api.dao.TicketDao;
import com.laf.mall.api.querycondition.ticket.TicketNoPassSaveCondition;
import com.laf.mall.api.querycondition.ticket.TicketNoQueryConditon;
import com.laf.mall.api.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class TicketServiceImpl implements TicketService {

    @Autowired
    TicketDao ticketDao;

    @Override
    public int operateTicketForNoPass(TicketNoPassSaveCondition condition) {
        condition.setVcId(1); // TODO: 2017/11/15 获取操作人员的信息
        condition.setHandleDate(new Date().getTime());
        condition.setHandleStatus(1);
        int result = ticketDao.updateTicketForNoPass(condition);

        return result;
    }

    @Override
    public int getTicketByTicketNoCount(TicketNoQueryConditon condition) {
        return ticketDao.getTicketByTicketNoCount(condition);
    }
}
