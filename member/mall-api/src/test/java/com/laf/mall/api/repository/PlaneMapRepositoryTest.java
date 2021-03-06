package com.laf.mall.api.repository;

import com.laf.mall.api.dto.PlaneMap;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PlaneMapRepositoryTest {

    @Autowired
    private PlaneMapRepository planeMapRepository;

    @Test
    public void selectAll() {
        List<PlaneMap> list = planeMapRepository.selectAllPlaneMaps(10);
        Assert.assertNotNull(list);
        Assert.assertTrue(!list.isEmpty());
    }

}