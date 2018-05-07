package com.laf.manager.dao;

import com.laf.manager.core.support.SnowFlake;
import com.laf.manager.dto.Member;
import com.laf.manager.dto.Points;
import com.laf.manager.querycondition.member.MemberFilterCondition;
import com.laf.manager.querycondition.points.PointsActionCondition;
import com.laf.manager.querycondition.points.PointsLogFilterCondition;
import com.laf.manager.querycondition.points.PointsQueryCondition;
import com.laf.manager.repository.PointsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PointsDao {

    @Autowired
    private PointsRepository pointsRepository;

    public List<Points> getPointsList(PointsQueryCondition condition) {
        return pointsRepository.selectPointsList(condition);
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

    public int savePointsExcludeConsume(Points points) {
        SnowFlake snowFlake = new SnowFlake();
        points.setMplog_id(snowFlake.nextId2String());
        return pointsRepository.insertPointsExcludeConsume(points);
    }

    public int getLogsCount(final Integer mallId) {
        return pointsRepository.selectLogsCount(mallId);
    }

    public List<Points> multipleQuery(final PointsLogFilterCondition condition) {
        List<Points> $ = null;

        try {
            $ = pointsRepository.multipleSelectPoints(condition);
        } catch (EmptyResultDataAccessException e) {
            $ = new ArrayList<>();
        }

        return $;
    }

    public int multipleMembersCount(final PointsLogFilterCondition condition) {
        return pointsRepository.multipleSelectPointsCount(condition);
    }

    public int selectPointsSum(final PointsLogFilterCondition condition) {
        return pointsRepository.selectPointsSum(condition);
    }
}
