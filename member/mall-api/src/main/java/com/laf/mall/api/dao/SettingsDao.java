package com.laf.mall.api.dao;

import com.laf.mall.api.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SettingsDao {

    @Autowired
    SettingsRepository repository;

    public float getPointsCoefficient(int mallId) {
        return repository.selectPointsCoefficient(mallId);
    }

}
