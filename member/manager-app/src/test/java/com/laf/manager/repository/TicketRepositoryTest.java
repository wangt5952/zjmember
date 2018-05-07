package com.laf.manager.repository;

import com.laf.manager.dto.Ticket;
import com.laf.manager.querycondition.ticket.TicketQueryCondition;
import com.laf.manager.utils.datetime.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TicketRepositoryTest {

    @Autowired
    TicketRepository repository;

    @Autowired
    DateTimeUtils dateTimeUtils;

    @Test
    public void insertTicket() throws Exception {
        for (int i = 0; i < 20; i++) {
            Ticket ticket = new Ticket();
            ticket.setMember_id(1);
            ticket.setMember_name("laf02240804");
            ticket.setMember_mobile("138139317");
            ticket.setUpload_date(new Date());
            ticket.setMall_id(10);
            ticket.setFile_url("/dfd/dfd/fd");
            int result = repository.insertTicket(ticket);

            Assert.assertTrue(result > 0);
            log.info("[new ticket id == {}]", result);
        }
    }

    @Test
    public void deleteTicketBy() throws Exception {
        repository.deleteTicketBy(22);
    }

    @Test
    public void selectTicketList() throws Exception {

        TicketQueryCondition condition = new TicketQueryCondition();
        condition.setMallId(10);
        condition.setPage(2);
        condition.setSize(6);
        List<Ticket> list = repository.selectTicketList(condition);
        for (Ticket t : list) {
            int id = t.getTicket_id();
            log.info("id {}", id);
        }
    }

    @Test
    public void selectTicketListByMemberId() throws Exception {
    }

    @Test
    public void updateTicket() throws Exception {
        Ticket t = new Ticket();
        //vc_id=?,vc_name=?,vc_phone=?,handle_date=?,responses=?,handle_status=?,ticket_no=?,shop_id=?,shopping_date=?,amounts=?
        t.setVc_id(1);
        t.setHandle_date(new Date());
        t.setResponses("已处理");
        t.setHandle_status(0);
        t.setTicket_no("244uui48822");
        t.setShop_id(37);
        t.setShopping_date(new Date(dateTimeUtils.getMilliByDate(2017, 10, 2)));
        t.setAmounts(new BigDecimal(120));
        t.setTicket_id(2);
        int result = repository.updateTicket(t);
        Assert.assertTrue(result > 0);
        log.info("update : {}", result);
    }

}
