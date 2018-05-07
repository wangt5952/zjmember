package com.laf.manager.service.impl;

import com.laf.manager.dao.RaffleDaoJava;
import com.laf.manager.dto.RaffleInfo;
import com.laf.manager.querycondition.raffle.RaffleQueryCondition;
import com.laf.manager.service.RaffleServiceJava;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RaffleServiceJavaImpl implements RaffleServiceJava {

    @Autowired
    RaffleDaoJava raffleDao;

    @Override
    public List<Map<String, Object>> getRaffleList(RaffleQueryCondition condition) {
        return raffleDao.getRaffleList(condition);
    }

    @Override
    public int getRaffleCount(RaffleQueryCondition condition) {
        return raffleDao.getRaffleCount(condition);
    }
}
