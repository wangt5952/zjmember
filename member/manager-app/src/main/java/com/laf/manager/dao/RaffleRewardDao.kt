package com.laf.manager.dao

import com.laf.manager.dto.RaffleReward
import com.laf.manager.repository.RaffleRewardRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class RaffleRewardDao {

    @Autowired
    lateinit var repository: RaffleRewardRepository

    fun saveRaffleReward(reward: RaffleReward) = repository.insertRaffleReward(reward)

    fun getRewardsByRaffleId(raffleId: Int) = repository.selectRaffleRewardsByRaffleId(raffleId)

    fun delRewardsByRaffleId(raffleId: Int) = repository.deleteRaffleRewardsByRaffleId(raffleId)

}