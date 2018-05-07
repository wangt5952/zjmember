package com.laf.mall.api.repository;

import com.laf.mall.api.dto.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
//@Transactional
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void insertMemeberSuccess() throws Exception {
        Member member = new Member();
        member.setName("yzg");
        member.setMobile("13770971125");
        member.setAddress("nj");
        member.setLevel("vip");
        member.setOpen_id("asdfjlads2323dsafjakls11");
        member.setBirthday(new Date());

        int result = memberRepository.insertMemeber(member);

        assertNotEquals(0, result);
    }

    @Test
    public void insertMemeberInMallSuccess() throws Exception {
        int result = memberRepository.insertMemberInMall(123445, 1, "asdfjlads2323dsafjakls11");

        assertNotEquals(0, result);
    }

    @Test
    public void registMember() throws Exception {
        for (int i = 0; i < 30; i++) {
            Member member = new Member();
            member.setName("yzg");
            member.setMobile("13770971125" + i);
            member.setAddress("nj");
            member.setLevel("vip");
            member.setOpen_id("asdfjlads2323dsafjakls11");
            member.setBirthday(new Date());

            int id = memberRepository.insertMemeber(member);
            assertNotEquals(0, id);

            int result = memberRepository.insertMemberInMall(id, 10, "asdfjlads2323dsafjakls11");
            Assert.assertTrue(result != 0);
            log.info("{}", result);
        }
    }

    @Test
    public void selectMemberByOpenIdAndMallIdSuccess() throws Exception {
        String openId = "asdfjlads2323dsafjakls11";

        Member member = memberRepository.selectMemberById(1, 1);

        if (member != null) {
//            log.info("======={}=========", member.getMobile());
            log.info(member.toString());
        }

        assertNotNull(member);

    }

    @Test
    public void selectMemberByOpenIdSuccess() throws Exception {
        String openId = "asdfjlads2323dsafjakls11";

        Member member = memberRepository.selectMemberByMobile("13770971125");

        if (member != null) {
//            log.info("======={}=========", member.getMobile());
            log.info(member.toString());
        }

        assertNotNull(member);

    }

    @Test
    public void selectMemberByOpenIdInMall() throws Exception {
        String appId = "wx3041b222eaad5c8a";
        String openId = "asdfjlads2323dsafjakls11";

        Member member = memberRepository.selectMemberByOpenIdInMall(appId, openId);

        Assert.assertNotNull(member);
        log.info("member.mobile={}", member.getMobile());

    }

    @Test
    public void selectMobileSuccess() throws Exception {
        String mobile = "13770971125";

        int count = memberRepository.selectMobileCount(mobile);

        log.info("========{}=========", count);

        assertNotEquals(0, count);

    }

    @Test
    public void updateMember() {
        Member member = new Member();
        member.setOpen_id("asdfjlads2323dsafjakls");
        member.setName("laf");
        member.setMobile("13770971125");
        member.setBirthday(new Date(1507739604136L));
        member.setEnable_public_wa(false);

        int result = memberRepository.updateMember(member);

        log.info("======={}=========", result);

        assertNotEquals(0, result);
    }

    @Test
    public void updateMobile() {

        String mobile = "138139317821";
        String openId = "asdfjlads2323dsafjakls11";

        int result = memberRepository.updateMobile(mobile, 1);
        assertNotEquals(0, result);

        Member member = memberRepository.selectMemberById(1, 1);
        assertNotNull(member);

        assertEquals(mobile, member.getMobile());

    }
}