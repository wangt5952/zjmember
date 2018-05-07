package com.laf.manager.service;

import com.laf.manager.dto.Reward;
import com.laf.manager.enums.RewardCategory;

import java.util.List;
import java.util.Map;

public interface SettingsService {

    int initSettings();

    int editPointsRule(String ruleJson);

    int editCouponRule(String ruleJson);

    int editPointsCoefficient(float pointsCoefficient);

    List<Map<String, Object>> getPointsRule();

    Map<String, Object> getCouponRule();

    float getPointsCoefficient();

    int getPointsByLevel(int levelId);

    Reward getRewardFromCategory(RewardCategory category);

    int editReward(Reward reward);
}
