package com.laf.mall.api.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void addKeySuccess() {
        stringRedisTemplate.opsForValue().set("aaa", "111");
        assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));
    }

    @Test
    public void deleteKeySuccess() {
//        stringRedisTemplate.delete("aaa");
//
//        assertNull(stringRedisTemplate.opsForValue().get("aaa"));

//        LocalTime localTime = LocalTime.now();
        LocalDate localDate = LocalDate.now();
//
//        log.info(localDate.toString());
//
//        log.info(localTime.toString());
//
//        log.info(localDate.getDayOfMonth()+"");
//        log.info(localDate.getMonthValue()+"");



        LocalDate oldDate = Instant.ofEpochMilli(new Date().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
//        log.info(oldDate.toString());

        LocalDate newDate = LocalDate.of(2017, 2, 1);
//        log.info(newDate.toString());

        LocalDateTime time = LocalDateTime.of(newDate.getYear(), newDate.getMonth(), newDate.getDayOfMonth(), 0, 0, 0, 0);
        ZonedDateTime zdt = time.atZone(ZoneId.systemDefault());
        log.info(zdt.toInstant().toEpochMilli()+"");
//        log.info(new Date().getTime()+"");

//        log.info(newDate+"");
//        log.info(newDate.plusDays(1) + "");

    }

}