package com.laf.mall.api.repository;

import com.laf.mall.api.dto.Admin;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AdminRepositoryTest {
    @Autowired
    AdminRepository adminRepository;

    @Test
    public void selectById() {
        Admin admin = adminRepository.getAdminById(3);
        log.info(admin.getMobile());
        assertNotNull(admin);
    }
}