package com.laf.mall.api.querycondition.activity;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ActivityQueryCondition {

    @NotNull(message = "mallId必填")
    private Integer mallId;

    private Integer memberId = 0;

    private Integer size = 5;

    private Integer page = 1;

    private String sort = "activity_time_end";

    private String direction = "desc";

    public int getOffset() {
        return (page - 1) * size;
    }

    public long getCurrTime() {
        return new Date().getTime();
    }
}
