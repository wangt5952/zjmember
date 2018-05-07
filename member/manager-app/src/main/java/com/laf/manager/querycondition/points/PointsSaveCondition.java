package com.laf.manager.querycondition.points;

import lombok.Data;

import java.util.Date;

@Data
public class PointsSaveCondition extends PointsBaseCondition {

    private int points;

    private Date handleDate;

    private int sources;

    public long getHandleDate() {
        return handleDate.getTime();
    }
}
