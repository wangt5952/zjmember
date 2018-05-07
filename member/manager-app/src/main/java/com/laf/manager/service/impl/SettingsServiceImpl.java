package com.laf.manager.service.impl;

import com.jayway.jsonpath.JsonPath;
import com.laf.manager.SettingsProperties;
import com.laf.manager.dao.LevelDao;
import com.laf.manager.dao.SettingsDao;
import com.laf.manager.dto.Level;
import com.laf.manager.dto.Reward;
import com.laf.manager.enums.RewardCategory;
import com.laf.manager.service.SettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
@Service
public class SettingsServiceImpl implements SettingsService {

    @Autowired
    SettingsDao settingsDao;

    @Autowired
    LevelDao levelDao;

    @Autowired
    SettingsProperties settingsProperties;

    @Override
    public int initSettings() {
        return settingsDao.saveSettings(settingsProperties.getMallId());
    }

    @Override
    public int editPointsRule(String ruleJson) {
        return settingsDao.editPointsRule(ruleJson, settingsProperties.getMallId());
    }

    @Override
    public int editCouponRule(String ruleJson) {
        return settingsDao.editCouponRule(ruleJson, settingsProperties.getMallId());
    }

    @Override
    public int editPointsCoefficient(float pointsCoefficient) {
        return settingsDao.editPointsCoefficient(pointsCoefficient, settingsProperties.getMallId());
    }

    @Override
    public List<Map<String, Object>> getPointsRule() {
        String json = settingsDao.getPointsRule(settingsProperties.getMallId());
        List<Map<String, Object>> list = new ArrayList<>();

        if (!StringUtils.isEmpty(json)) {
            list = (List<Map<String, Object>>) JsonPath.parse(json).read("$", List.class);
        }

        if (list.size() == 0 || list.get(0) == null) {
            List<Level> levels = levelDao.getAllLevel(settingsProperties.getMallId());
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

    @Override
    public Map<String, Object> getCouponRule() {
        String json = settingsDao.getCouponRule(settingsProperties.getMallId());
        Map<String, Object> map = new HashMap<>();


        if (!StringUtils.isEmpty(json)) {
            map = (Map<String, Object>) JsonPath.parse(json).read("$", Map.class);
        }

        if (map.size() == 0) {
            map = new HashMap<>();
            map.put("activity_date", String.valueOf(new Date().getTime()));
            map.put("activity_coupon_id", "-1");
            map.put("activity_coupon_name", "");
            map.put("date", "0");
            map.put("coupon_id", "-1");
            map.put("coupon_name", "");
        }

        return map;
    }

    @Override
    public float getPointsCoefficient() {
        return settingsDao.getPointsCoefficient(settingsProperties.getMallId());
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
    public Reward getRewardFromCategory(RewardCategory category) {
        return settingsDao.getRewardFromId(category.value());
    }

    @Override
    public int editReward(Reward reward) {
        return settingsDao.editReward(reward);
    }
}
