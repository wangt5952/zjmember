package com.laf.mall.api.repository;

import com.laf.mall.api.dto.VerificationClerk;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class VerificationClerkRepositoryTest {
    @Autowired
    VerificationClerkRepository repository;

    @Test
    public void insertVerificationClerkForServices() throws Exception {
        VerificationClerk services = new VerificationClerk();
        services.setVc_name("laf");
        services.setVc_type(0);
        services.setDepartment(1);
        services.setReg_date(new Date());
        services.setMall_id(1);
        services.setPhone("88888888");
        services.setMember_id(3);

        int result = repository.insertVerificationClerkForServices(services);

        assertEquals(result, 1);
    }

    @Test
    public void insertVerificationClerkForClerk() throws Exception {
        VerificationClerk clerk = new VerificationClerk();
        clerk.setVc_name("laf");
        clerk.setVc_type(1);
        clerk.setShop_id(13);
        clerk.setReg_date(new Date());
        clerk.setMall_id(1);
        clerk.setPhone("88888888");
        clerk.setMember_id(6);

        int result = repository.insertVerificationClerkForServices(clerk);

        assertEquals(result, 1);
    }

}