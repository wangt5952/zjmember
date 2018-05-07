package com.laf.manager.dao

import com.laf.manager.dto.RaffleInfo
import com.laf.manager.dto.RaffleLog
import com.laf.manager.querycondition.raffle.RaffleFilterCondition
import com.laf.manager.repository.RaffleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class RaffleDao {
    @Autowired
    lateinit var repository: RaffleRepository

    fun saveRaffle(raffleInfo: RaffleInfo) = repository.insertRaffleInfo(raffleInfo)

    fun getRaffleByCode(activityCode: String): RaffleInfo? {
        val raffles = repository.selectRaffleByCode(activityCode)

        if (raffles.isNotEmpty())
            return raffles[0]
        else
            return null
    }

    fun getRaffleById(raffleId: Int): RaffleInfo? {
        val raffles = repository.selectRaffleById(raffleId)

        if (raffles.isNotEmpty())
            return raffles[0]
        else
            return null
    }

    fun updateRaffle(raffleInfo: RaffleInfo) = repository.updateRaffleInfo(raffleInfo)

    fun multipleGetRaffleLogs(it: RaffleFilterCondition): List<RaffleLog> {
        val logs = repository.multipleSelectRaffleLogs(it)

        if (logs.isNotEmpty()) {
            return logs
        } else {
            return ArrayList<RaffleLog>()
        }
    }

    fun multipleGetRaffleLogsCount(it: RaffleFilterCondition): Int {
        val count = repository.multipleSelectRaffleLogsCount(it)
        return count
    }
}