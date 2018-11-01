package com.laf.manager.repository;

import com.laf.manager.dto.PlaneMap;
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
    PlaneMapRepository repository;

    @Test
    public void selectAllPlaneMaps() throws Exception {
        List<PlaneMap> list = repository.selectAllPlaneMaps(10);
        Assert.assertTrue(list != null && list.size() != 0);
        for (PlaneMap map : list) {
            log.info(map.getMap_name());
        }
    }

    @Test
    public void selectPlaneMapById() throws Exception {
        PlaneMap map = repository.selectPlaneMapById(2);
        Assert.assertTrue(map != null);
        log.info(map.getMap_name());
    }

    @Test
    public void insertPlaneMap() throws Exception {
        for (int i = 0; i < 10; i++) {
            PlaneMap map = new PlaneMap();
            map.setMall_id(10);
            map.setMap_name((i + 1) + "F");
            map.setMap_picture("123");
            map.setSort_id(i + 1);
            //int result = repository.insertPlaneMap(map);
           // Assert.assertTrue(result == 1);
        }
    }

    @Test
    public void updatePlaneMap() throws Exception {
        PlaneMap map = new PlaneMap();
        map.setMap_id(2);
        map.setMall_id(10);
        map.setMap_name("1F");
        map.setMap_picture("localhost");
        map.setSort_id(2);
       // int result = repository.updatePlaneMap(map);
       // Assert.assertTrue(result == 1);
    }

    @Test
    public void deletePlaneMap() throws Exception {
//        List<PlaneMap> maps = repository.selectAllPlaneMaps(10);
        int result = repository.deletePlaneMap(2);
        Assert.assertTrue(result == 1);
    }
}
