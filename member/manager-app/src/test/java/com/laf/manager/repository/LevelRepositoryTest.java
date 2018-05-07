package com.laf.manager.repository;

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
    LevelRepository repository;

    @Test
    public void selectLevelByLow() throws Exception {
        repository.selectLevelByLow(10);
    }

}