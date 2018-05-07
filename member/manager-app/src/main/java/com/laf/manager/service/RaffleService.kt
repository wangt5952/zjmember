package com.laf.manager.service

import com.laf.manager.dto.RaffleInfo
import com.laf.manager.dto.RaffleLog
import com.laf.manager.querycondition.raffle.RaffleFilterCondition

interface RaffleService {
    fun getRaffleList(): List<RaffleInfo>

    fun getRaffleInfo(activityCode: String): RaffleInfo?

    fun getRaffleInfoById(raffleId: Int): RaffleInfo?

    fun saveRaffleInfo(raffleInfo: RaffleInfo): Int

    fun multipleGetRaffleLogs(it: RaffleFilterCondition): List<RaffleLog>

    fun multipleGetRaffleLogsCount(it: RaffleFilterCondition): Int
}