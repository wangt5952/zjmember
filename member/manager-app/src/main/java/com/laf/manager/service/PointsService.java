package com.laf.manager.service;

import com.laf.manager.dto.Points;
import com.laf.manager.querycondition.points.PointsLogFilterCondition;
import com.laf.manager.querycondition.points.PointsManualCondition;
import com.laf.manager.querycondition.points.PointsQueryCondition;

import java.io.OutputStream;
import java.util.List;

public interface PointsService {

    /**
     * 获取积分明细
     *
     * @param condition
     * @return
     */
    List<Points> getPointsList(PointsLogFilterCondition condition);

    int getLogsCount(PointsLogFilterCondition condition);

    int manual(PointsManualCondition condition);

    int getPointsSum(final PointsLogFilterCondition condition);

    void print2Excel(List<Points> members, OutputStream out);
}
