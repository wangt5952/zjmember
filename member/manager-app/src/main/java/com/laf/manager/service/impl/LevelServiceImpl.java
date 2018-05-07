package com.laf.manager.service.impl;

import com.laf.manager.SettingsProperties;
import com.laf.manager.dao.LevelDao;
import com.laf.manager.dto.Level;
import com.laf.manager.querycondition.level.LevelSaveCondition;
import com.laf.manager.querycondition.level.LevelSaveForAmountCondition;
import com.laf.manager.service.LevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LevelServiceImpl implements LevelService {

    @Autowired
    LevelDao levelDao;

    @Autowired
    SettingsProperties settingsProperties;

    @Override
    public List<Level> getAllLevelByMall() {
        return levelDao.getAllLevel(settingsProperties.getMallId());
    }

    @Override
    public int editLevel(LevelSaveCondition condition) {
        condition.setLevelId(condition.getLevelId() == null ? 0 : condition.getLevelId());
        condition.setMallId(condition.getMallId() == null ? settingsProperties.getMallId() : condition.getMallId());
        Level level = getLevelById(condition.getLevelId());
        int result;

        if (level == null) {
            level = new Level();
            level.setMall_id(condition.getMallId());
            level.setLevel_name(condition.getLevelName());
            level.setPoints_high(condition.getPointsHigh());
            level.setPoints_low(condition.getPointsLow());
            result = levelDao.saveLevel(level);

        } else {
            level.setMall_id(condition.getMallId());
            level.setLevel_name(condition.getLevelName());
            level.setPoints_high(condition.getPointsHigh());
            level.setPoints_low(condition.getPointsLow());
            result = levelDao.updateLevel(level);
        }

        return result;
    }

    @Override
    public int editLevelForAmount(LevelSaveForAmountCondition condition) {
        condition.setLevelId(condition.getLevelId() == null ? 0 : condition.getLevelId());
        condition.setMallId(condition.getMallId() == null ? settingsProperties.getMallId() : condition.getMallId());
        Level level = getLevelById(condition.getLevelId());
        int result;

        if (level == null) {
            level = new Level();
            level.setMall_id(condition.getMallId());
            level.setLevel_name(condition.getLevelName());
            level.setAmount_low(condition.getAmountLow());
            level.setAmount_high(condition.getAmountHigh());
            result = levelDao.saveLevelForAmount(level);

        } else {
            level.setMall_id(condition.getMallId());
            level.setLevel_name(condition.getLevelName());
            level.setAmount_low(condition.getAmountLow());
            level.setAmount_high(condition.getAmountHigh());
            result = levelDao.editLevelForAmount(level);
        }

        return result;
    }

    @Override
    public Level getLevelById(Integer levelId) {
        return levelDao.getLevelById(levelId);
    }

    @Override
    public int deleteLevelById(Integer levelId) {
        return levelDao.deleteLevel(levelId);
    }

    @Override
    public List<Level> getAllUsableLevelByMall() {
        return levelDao.getAllUsableLevelByMall(settingsProperties.getMallId());
    }

    @Override
    public int updateLevelUsableTrue(final Integer levelId) {
        return levelDao.updateLevelUsableStatus(levelId, 0);
    }

    @Override
    public int updateLevelUsableFalse(final Integer levelId) {
        return levelDao.updateLevelUsableStatus(levelId, 1);
    }
}
