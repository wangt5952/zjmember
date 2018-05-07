package com.laf.manager.dao;

import com.laf.manager.dto.Level;
import com.laf.manager.querycondition.level.LevelQueryCondition;
import com.laf.manager.repository.LevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class LevelDao {

    @Autowired
    LevelRepository repository;

    public Level getLowestLevel(final Integer mall_id) {
        Level result;

        try {
            result = repository.selectLevelByLow(mall_id);
        } catch (EmptyResultDataAccessException e) {
            result = null;
        }

        return result;
    }

    public List<Level> getAllLevel(final Integer mallId) {
        return repository.selectAllLevelsByMall(mallId);
    }

    public List<Level> getLevels(final LevelQueryCondition condition) {
        return repository.selectLevels(condition);
    }

    public Level getLevelById(final Integer levelId) {
        return repository.selectLevelById(levelId);
    }

    public int saveLevel(final Level level) {
        return repository.insertLevel(level);
    }

    public int updateLevel(final Level level) {
        return repository.updateLevel(level);
    }

    public int deleteLevel(final Integer levelId) {
        return repository.deleteLevelById(levelId);
    }

    public List<Level> getAllUsableLevelByMall(final Integer mallId) {
        return repository.selectAllUsableLevelByMall(mallId);
    }

    public int updateLevelUsableStatus(final Integer levelId, final Integer usable) {
        return repository.updateLevelUsableStatus(levelId, usable);
    }

    public int saveLevelForAmount(final Level level) {
        return repository.insertLevelForAmount(level);
    }

    public int editLevelForAmount(final Level level) {
        return repository.updateLevelForAmount(level);
    }

    public int getIsAmountInLevel(BigDecimal amount, int levelId) {
        return repository.selectIsAmountInLevel(amount, levelId);
    }

    public List<Level> getLevelWidthAmount(BigDecimal amount) {
        return repository.selectLevelWidthAmount(amount);
    }
}
