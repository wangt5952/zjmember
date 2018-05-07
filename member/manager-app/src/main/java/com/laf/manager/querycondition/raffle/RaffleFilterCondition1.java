package com.laf.manager.querycondition.raffle;

import lombok.Data;

@Data
public class RaffleFilterCondition1 {
    private String title = "";
    private String mobile = "";
    private String username = "";
    private Long actionTimeStart = 0L;
    private Long actionTimeEnd = 0L;
    private Integer isWin = -1;
    private Integer mallId = 0;
    private Integer size = 10;
    private Integer page = 1;

    public Long getActionTimeStart() {
        return actionTimeStart == null ? 0L : actionTimeStart;
    }

    public Long getActionTimeEnd() {
        return actionTimeEnd == null ? 0L : actionTimeEnd;
    }
}
