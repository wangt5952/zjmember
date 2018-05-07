package com.laf.manager.querycondition.points;

import lombok.Data;

import java.util.Date;

@Data
public class PointsManualCondition {
    private String mplogId;

    private int memberId;

    private String memberName;

    private String memberMobile;

    private int mallId;

    private int points;

    private int sources;

    private String desc;

    public Date getCurrTime() {
        return new Date();
    }
}
