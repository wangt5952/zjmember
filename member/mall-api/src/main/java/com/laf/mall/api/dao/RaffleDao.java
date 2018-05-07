package com.laf.mall.api.dao;

import com.laf.mall.api.dto.RaffleInfo;
import com.laf.mall.api.dto.RaffleLog;
import com.laf.mall.api.dto.RaffleReward;
import com.laf.mall.api.querycondition.raffle.MyRewardQueryCondition;
import com.laf.mall.api.repository.RaffleRepository;
import com.laf.mall.api.vo.MyRewardInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RaffleDao {

    @Autowired
    private RaffleRepository repository;

    public RaffleInfo getRaffleInfoByActivityCode(final String activityCode, final int mallId) {
        return repository.selectRaffleInfoByActivityCode(activityCode, mallId);
    }

    public int saveRaffleLog(final RaffleLog log) {
        return repository.insertRaffleLog(log);
    }

    public List<MyRewardInfo> getMyRewardList(MyRewardQueryCondition condition) {
        return repository.selectMyRewardList(condition);
    }

    public int getRaffleLogCount(final int memberId, final int raffleId) {
        return repository.selectRaffleLogCount(memberId, raffleId, false);
    }

    public RaffleReward getRewardById(final int trrId) {
        return repository.selectRewardById(trrId);
    }

    public int editRewardInventory(final int trrId, final int inventory) {
        return repository.updateRewardInventory(trrId, inventory);
    }
}
