package com.laf.mall.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ShopControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void whenGetShopListSuccess() throws Exception {

        String result = mockMvc.perform(MockMvcRequestBuilders.post("/shop")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"mall_id\":10,\"keywords\":\"kdj\",\"size\":5,\"page\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andReturn().getResponse().getContentAsString();

        log.info(result);
    }

    @Test
    public void whenGetShopSuccess() throws Exception {
        String result = mockMvc.perform(get("/shop/10")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shop_name").value("苹果10"))
                .andReturn().getResponse().getContentAsString();

        log.info(result);
    }

    @Test
    public void whenGetIndustrySuccess() throws Exception {
        String result = mockMvc.perform(get("/mall/10/industries")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length").value(1))
                .andReturn().getResponse().getContentAsString();

        log.info(result);
    }

    @Test
    public void whenGetMapSuccess() throws Exception {
        String result = mockMvc.perform(get("/mall/10/planeMaps")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length").value(1))
                .andReturn().getResponse().getContentAsString();

        log.info(result);
    }
}