package com.laf.manager.service;

import com.laf.manager.querycondition.raffle.RaffleQueryCondition;

import java.util.List;
import java.util.Map;

public interface RaffleServiceJava {

    List<Map<String, Object>> getRaffleList(RaffleQueryCondition condition);

    int getRaffleCount(RaffleQueryCondition condition);
}
