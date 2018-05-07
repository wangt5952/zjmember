package com.laf.mall.api.service.impl;

import com.laf.mall.api.dao.SettingsDao;
import com.laf.mall.api.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingsServiceImpl implements SettingsService {

    @Autowired
    SettingsDao settingsDao;

    @Override
    public float getPointsCoefficient(int mallId) {
        return settingsDao.getPointsCoefficient(mallId);
    }

//    @Override
//    public int getPointsByLevel(int levelId) {
//        List<Map<String, Object>> list = getPointsRule();
//        int result = 0;
//
//        for (Map<String, Object> map : list) {
//            String level_id = map.get("level_id").toString();
//            if (level_id.equals(String.valueOf(levelId))) {
//                result = Integer.valueOf(map.get("points").toString()).intValue();
//                break;
//            }
//        }
//
//        return result;
//    }
}
