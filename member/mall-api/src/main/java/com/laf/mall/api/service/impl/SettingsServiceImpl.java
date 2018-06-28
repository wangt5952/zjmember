package com.laf.mall.api.service.impl;

import com.jayway.jsonpath.JsonPath;
import com.laf.mall.api.dao.LevelDao;
import com.laf.mall.api.dao.SettingsDao;
import com.laf.mall.api.dto.Level;
import com.laf.mall.api.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SettingsServiceImpl implements SettingsService {

    @Autowired
    SettingsDao settingsDao;

    @Autowired
    LevelDao levelDao;

    @Override
    public float getPointsCoefficient(int mallId) {
        return settingsDao.getPointsCoefficient(mallId);
    }

    @Override
    public int getPointsByLevel(int levelId) {
        List<Map<String, Object>> list = getPointsRule();
        int result = 0;

        for (Map<String, Object> map : list) {
            String level_id = map.get("level_id").toString();
            if (level_id.equals(String.valueOf(levelId))) {
                result = Integer.valueOf(map.get("points").toString()).intValue();
                break;
            }
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> getPointsRule() {
        String json = settingsDao.getPointsRule(1);
        List<Map<String, Object>> list = new ArrayList<>();

        if (!StringUtils.isEmpty(json)) {
            list = (List<Map<String, Object>>) JsonPath.parse(json).read("$", List.class);
        }

        if (list.size() == 0 || list.get(0) == null) {
            List<Level> levels = levelDao.getAllLevel(1);
            list = new ArrayList<>();

            for (Level level : levels) {
                Map<String, Object> map = new HashMap<>();
                map.put("level_id", String.valueOf(level.getLevel_id()));
                map.put("level_name", level.getLevel_name());
                map.put("points", "0");
                list.add(map);
            }
        }

        return list;
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
