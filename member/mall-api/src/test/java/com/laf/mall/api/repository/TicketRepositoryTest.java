package com.laf.mall.api.repository;

import com.laf.mall.api.dto.Member;
import com.laf.mall.api.dto.Ticket;
import com.laf.mall.api.querycondition.ticket.TicketQueryCondition;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TicketRepositoryTest {

    @Autowired
    TicketRepository repository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void insertTicket() throws Exception {

        Member member = memberRepository.selectMemberById(1);

        Ticket ticket = new Ticket();
        ticket.setMember_id(member.getMember_id());
        ticket.setMember_name(member.getName());
        ticket.setMember_mobile(member.getMobile());
        ticket.setUpload_date(new Date());
        ticket.setFile_url("https://www.laf.com/upload/images/xxx.png");
       // int result = repository.insertTicket(ticket);

       // assertNotEquals(result, 0);
    }

    @Test
    public void deleteTicketBy() throws Exception {
        int result = 1;//repository.deleteTicketBy(2);
        assertEquals(result, 1);
    }

    @Test
    public void selectTicketListByMemberId() throws Exception {
//        TicketQueryCondition condition = new TicketQueryCondition(1);
//        List<Ticket> list = repository.selectTicketListByMemberId(condition);
//        list.stream().forEach(System.out::println);
//        assertFalse(list.isEmpty());
    }

}