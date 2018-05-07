package com.laf.mall.api.dao;

import com.laf.mall.api.dto.Points;
import com.laf.mall.api.querycondition.points.PointsActionCondition;
import com.laf.mall.api.querycondition.points.PointsQueryCondition;
import com.laf.mall.api.querycondition.points.PointsSaveCondition;
import com.laf.mall.api.repository.PointsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PointsDao {

    @Autowired
    private PointsRepository pointsRepository;

    public List<Points> getPointsList(PointsQueryCondition condition, final Integer memberId) {
        return pointsRepository.selectPointsList(condition, memberId);
    }

    public Points getPointsDetail(final String mplogId) {
        return pointsRepository.selectPointsDetail(mplogId);
    }

    public int savePoints(Points points) {
        return pointsRepository.insertPoints(points);
    }

    public int savePointsExcludeConsume(PointsActionCondition condition) {
        return pointsRepository.insertPointsExcludeConsume(condition);
    }
}
