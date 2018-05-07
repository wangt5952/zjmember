package com.laf.mall.api.dao;

import com.laf.mall.api.dto.PlaneMap;
import com.laf.mall.api.repository.PlaneMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlaneMapDao {

    @Autowired
    private PlaneMapRepository planeMapRepository;

    public List<PlaneMap> getAllPlaneMaps(final Integer mallId) {
        return planeMapRepository.selectAllPlaneMaps(mallId);
    }
}
