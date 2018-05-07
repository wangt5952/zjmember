package com.laf.manager.dao;

import com.laf.manager.dto.PlaneMap;
import com.laf.manager.repository.PlaneMapRepository;
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

    public PlaneMap getPlaneMapById(final Integer mapId) {
        return planeMapRepository.selectPlaneMapById(mapId);
    }

    public int savePlaneMap(PlaneMap map) {
        return planeMapRepository.insertPlaneMap(map);
    }

    public int updatePlaneMap(PlaneMap map) {
        return planeMapRepository.updatePlaneMap(map);
    }

    public int deletePlaneMap(final Integer mapId) {
        return planeMapRepository.deletePlaneMap(mapId);
    }
}
