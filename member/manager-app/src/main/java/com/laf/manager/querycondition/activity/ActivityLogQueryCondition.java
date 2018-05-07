package com.laf.manager.querycondition.activity;

import com.laf.manager.utils.datetime.DateTimeUtils;
import lombok.Data;

@Data
public class ActivityLogQueryCondition {

    private String name;

    private String mobile;

    private String title;

    private Integer process = -1;

    private Integer signType = 0;

    private int mallId;

    private int size = 10;

    private int page = 1;

    public long getCurr() {
        DateTimeUtils utils = new DateTimeUtils();
        return utils.getMilliByToDay();
    }

    public int getOffset() {
        return (page - 1) * size;
    }
}
