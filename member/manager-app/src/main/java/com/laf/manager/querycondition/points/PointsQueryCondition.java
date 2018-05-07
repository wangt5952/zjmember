package com.laf.manager.querycondition.points;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ToString
public class PointsQueryCondition {

    private Integer mallId;

    private Integer shopId;

    private long shoppingDateFrom;

    private long shoppingDateTo;

    private String mobile;

    private int page = 1;

    private int size = 10;

    /**
     * 排序顺序：asc 升序；desc 降序
     */
//    private String direction = "desc";

    public int getOffset() {
        return (page - 1) * size;
    }

    public String getSort() {
        return "handle_date";
    }

    public String getDirection() {
        return "desc";
    }
}
