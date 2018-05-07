package com.laf.mall.api.querycondition.points;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class PointsSaveCondition extends PointsBaseCondition {

    private int points;

    @NotNull
    private Date handleDate;

    private int sources;

    public long getHandleDate() {
        return handleDate.getTime();
    }
}
