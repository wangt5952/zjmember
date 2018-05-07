package com.laf.mall.api.dao;

import com.laf.mall.api.dto.Level;
import com.laf.mall.api.repository.LevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LevelDao {

    @Autowired
    LevelRepository repository;

    public Level getLowestLevel(final Integer mall_id) {
        return repository.selectLevelByLow(mall_id);
    }

    public List<Level> getAllLevel(final Integer mallId) {
        return repository.selectAllLevelsByMall(mallId);
    }
}
