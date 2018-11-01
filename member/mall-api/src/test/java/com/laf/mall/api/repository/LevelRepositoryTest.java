package com.laf.mall.api.repository;

import com.laf.mall.api.dto.Level;
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

public class LevelRepositoryTest {

    @Autowired
    LevelRepository levelRepository;

    @Test
    public void selectLevelByLow() throws Exception {

    }

    @Test
    public void insertLevel() throws Exception {
        Level level = new Level();
        level.setLevel_name("vip3");
        level.setMall_id(1);
        level.setPoints_low(0);
        level.setPoints_high(1000);

       // int result = levelRepository.insertLevel(level);

      //  assertEquals(1, result);
    }

}