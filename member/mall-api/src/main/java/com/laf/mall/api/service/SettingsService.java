package com.laf.mall.api.service;

import java.util.List;
import java.util.Map;

public interface SettingsService {

    float getPointsCoefficient(final int mallId);

//    int getPointsByLevel(int levelId);
     int getPointsByLevel(int levelId);

    List<Map<String, Object>> getPointsRule();
}
