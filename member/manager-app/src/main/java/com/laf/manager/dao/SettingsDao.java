package com.laf.manager.dao;

import com.laf.manager.dto.Reward;
import com.laf.manager.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SettingsDao {

    @Autowired
    SettingsRepository repository;

    public String getPointsRule(int mallId) {
        return repository.selectPointsRule(mallId);
    }

    public String getCouponRule(int mallId) {
        return repository.selectCouponRule(mallId);
    }

    public float getPointsCoefficient(int mallId) {
        return repository.selectPointsCoefficient(mallId);
    }

    public int editPointsRule(String rule, int mallId) {
        return repository.updatePointsRule(rule, mallId);
    }

    public int editCouponRule(String rule, int mallId) {
        return repository.updateCouponRule(rule, mallId);
    }

    public int editPointsCoefficient(float pointsCoefficient, int mallId) {
        return repository.updatePointsCoefficient(pointsCoefficient, mallId);
    }

    public int saveSettings(int mallId) {
        return repository.insertSettings(mallId);
    }

    public Reward getRewardFromId(int rewardId) {
        return repository.selectRewardFromId(rewardId);
    }

    public int editReward(Reward reward) {
        return repository.updateReward(reward);
    }
}
