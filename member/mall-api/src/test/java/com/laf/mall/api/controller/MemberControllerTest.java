package com.laf.mall.api.controller;

import com.laf.mall.api.querycondition.member.MemberRegistCondition;
import com.laf.mall.api.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MemberControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    MemberService memberService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getMemberSuccess() throws Exception {
        String result = mockMvc.perform(get("/member")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"openId\":\"asdfjlads2323dsafjakls11\",\"mallId\":1}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        log.info(result);
    }

    @Test
    public void registSuccess() throws Exception {
        final String content = "{\"name\":\"lafhh\", \"sex\":1, \"birthday\":" + new Date().getTime() + ", " +
                "\"openId\":\"asdfjlads2323dsafjakls22\", \"mallId\":1,\"mobile\":\"13813931721\"}";
        String result = mockMvc.perform(post("/member")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        log.info(result);
    }

    @Test
    public void testSendSmsSuccess() throws Exception {

        String result = mockMvc.perform(post("/member/vcode")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"mallId\": 1,\"mobile\":\"13813931721\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        log.info(result);
    }

    @Test
    public void whenUpdateSuccess() throws Exception {
        final String content = "{\"name\":\"laf02240804\", \"sex\":1, \"birthday\":" + new Date().getTime() + ", " +
                "\"mobile\":\"13813931721\",\"enablePublicWa\":1,\"memberId\":1}";

        String result = mockMvc.perform(put("/member")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        log.info(result);
    }

    @Test
    public void whenCheckValidCodeSuccess() throws Exception {
        final String content = "{\"mobile\":\"13813931721\",\"vcode\":884862}";

        String result = mockMvc.perform(post("/member/vcodeCheck")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        log.info(result);
    }

    @Test
    public void whenUploadSuccess() throws Exception {
//        File file = new File("/Users/apple/Desktop/hh/1504839083.png");
//        FileInputStream fis = new FileInputStream(file);

        RandomAccessFile f = new RandomAccessFile("/Users/apple/Desktop/hh/1504839083.png", "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();

        String result = mockMvc.perform(fileUpload("/member/uploadTicket/1")
                .file(new MockMultipartFile("file", "QQ20171003-172017.png", "multipart/form-data", bytes)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        log.info(result);
    }

    @Test
    public void whenDeleteTicket() throws Exception {
        String result = mockMvc.perform(delete("/member/ticket/2")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        log.info(result);
    }

    @Test
    public void whenGetTicketsSuccess() throws Exception {
        String result = mockMvc.perform(get("/member/1/tickets")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andReturn().getResponse().getContentAsString();

        log.info(result);
    }

    @Test
    public void whenRegistServicesSuccess() throws Exception {
        String result = mockMvc.perform(post("/member/registServices")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        log.info(result);
    }

    @Test
    public void whenRegistClerkSuccess() throws Exception {
        String result = mockMvc.perform(get("/member/registClerk")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        log.info(result);
    }

    @Autowired
    MemberService service;

    @Test
    public void transTest() {
        MemberRegistCondition condition = new MemberRegistCondition();
        condition.setBirthday(new Date().getTime());
        condition.setMallId(1);
        condition.setMobile("13313388888");
        condition.setOpenId("asfadfa");
        condition.setName("aaabbb");
        condition.setSex(1);
        try {
            service.registMember(condition);
        } catch (Exception e) {
            log.error("db error");
        }
    }

    @Test
    public void getInfoByOpenIdInMall() throws Exception {

        String result = mockMvc.perform(get("/member")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("appId", "wx3041b222eaad5c8a")
                .param("openId", "asdfjlads2323dsafjakls11"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        log.info(result);
    }
}