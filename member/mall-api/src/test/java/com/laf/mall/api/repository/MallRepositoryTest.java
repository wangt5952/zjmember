package com.laf.mall.api.repository;

import com.laf.mall.api.dto.Mall;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MallRepositoryTest {

    @Autowired
    MallRepository mallRepository;

    @Test
    public void getMallById() throws Exception {
        Mall mall = mallRepository.selectMallByAppId("appid1");
        log.info(mall.toString());
        assertNotNull(mall);
    }

    @Test
    public void saveMall() throws Exception {
        Mall mall = new Mall();
        mall.setMall_name("test shopping-mall");
        mall.setMall_phone("025-88888888");

        int result = mallRepository.saveMall(mall);
        log.trace(result+"");

        assertNotEquals(0, result);
    }

}