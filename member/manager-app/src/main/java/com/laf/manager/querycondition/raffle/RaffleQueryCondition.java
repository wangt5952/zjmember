package com.laf.manager.querycondition.raffle;

import lombok.Data;

@Data
public class RaffleQueryCondition {

    private int mallId;

    private int size = 10;

    private int page = 1;

    public int getOffset() {
        return (page - 1) * size;
    }
}
