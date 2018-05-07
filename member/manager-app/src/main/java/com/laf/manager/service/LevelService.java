package com.laf.manager.service;

import com.laf.manager.dto.Level;
import com.laf.manager.querycondition.level.LevelSaveCondition;
import com.laf.manager.querycondition.level.LevelSaveForAmountCondition;

import java.util.List;

public interface LevelService {

    List<Level> getAllLevelByMall();

    int editLevel(final LevelSaveCondition condition);

    Level getLevelById(final Integer levelId);

    int deleteLevelById(final Integer levelId);

    List<Level> getAllUsableLevelByMall();

    int updateLevelUsableTrue(final Integer levelId);

    int updateLevelUsableFalse(final Integer levelId);

    int editLevelForAmount(final LevelSaveForAmountCondition condition);
}
