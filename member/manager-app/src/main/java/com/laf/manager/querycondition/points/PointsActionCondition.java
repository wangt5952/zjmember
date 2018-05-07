package com.laf.manager.querycondition.points;

import com.laf.manager.core.support.SnowFlake;
import lombok.Data;

import java.util.Date;

@Data
public class PointsActionCondition {

    private Integer memberId;

    private String memberName;

    private String memberMobile;

    private Integer points;

    private Date handleDate;

    private Integer sources;

    public String getMplogId() {
        SnowFlake snowFlake = new SnowFlake();
        return snowFlake.nextId2String();
    }

    private Integer mallId;
}
