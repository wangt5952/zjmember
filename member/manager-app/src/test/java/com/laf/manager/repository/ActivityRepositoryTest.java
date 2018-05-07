package com.laf.manager.repository;

import com.laf.manager.dto.Activity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ActivityRepositoryTest {

    @Autowired
    ActivityRepository repository;

    @Test
    public void selectActivityById() throws Exception {
    }

    @Test
    public void selectActivityList() throws Exception {
    }

    @Test
    public void selectSignUpCount() throws Exception {
    }

    @Test
    public void insertActivity() throws Exception {
        Activity a = new Activity();
        a.setMall_id(10);
        repository.insertActivity(a);
    }

    @Test
    public void updateActivity() throws Exception {
    }

    @Test
    public void deleteAtivity() throws Exception {
    }


}
