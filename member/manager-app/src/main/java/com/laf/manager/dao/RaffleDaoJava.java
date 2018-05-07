package com.laf.manager.dao;

import com.laf.manager.querycondition.raffle.RaffleQueryCondition;
import com.laf.manager.repository.RaffleRepositoryJava;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RaffleDaoJava {

    @Autowired
    RaffleRepositoryJava repository;

    public List<Map<String, Object>> getRaffleList(RaffleQueryCondition condition) {
        return repository.selectRaffleList(condition);
    }

    public int getRaffleCount(RaffleQueryCondition condition) {
        return repository.selectRaffleCount(condition);
    }
}
