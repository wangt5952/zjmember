package com.laf.manager.querycondition.raffle;

import com.laf.manager.utils.datetime.DateTimeUtils;
import lombok.Data;

@Data
public class RaffleLogQueryCondition {

    private String memberName;

    private String raffleTitle;

    private String mobile;

    private Integer couponStatus = -1;

    /**
     * 1: 进行中
     * 2: 已结束
     */
    private Integer process = -1;

    private int mallId;

    private Integer page = 1;

    private Integer size = 10;

    public long getCurr() {
        DateTimeUtils utils = new DateTimeUtils();
        return utils.getMilliByToDay();
    }

    public int getOffset() {
        return (page - 1) * size;
    }
}
