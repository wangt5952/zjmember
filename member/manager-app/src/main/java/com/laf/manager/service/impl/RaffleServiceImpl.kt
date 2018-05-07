package com.laf.manager.service.impl

import com.laf.manager.SettingsProperties
import com.laf.manager.core.exception.MallDBException
import com.laf.manager.dao.ArticlesDao
import com.laf.manager.dao.RaffleDao
import com.laf.manager.dao.RaffleRewardDao
import com.laf.manager.dto.RaffleInfo
import com.laf.manager.dto.RaffleLog
import com.laf.manager.querycondition.raffle.RaffleFilterCondition
import com.laf.manager.service.RaffleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RaffleServiceImpl : RaffleService {

    @Autowired
    lateinit var raffleDao: RaffleDao

    @Autowired
    lateinit var articlesDao: ArticlesDao

    @Autowired
    lateinit var raffleRewardDao: RaffleRewardDao

    @Autowired
    lateinit var settingsProperties: SettingsProperties


    override fun getRaffleList(): List<RaffleInfo> = emptyList()

    override fun getRaffleInfo(activityCode: String): RaffleInfo? {
        val raffle = raffleDao.getRaffleByCode(activityCode)

        if (raffle != null) {
            val rewards = raffleRewardDao.getRewardsByRaffleId(raffle.raffle_id)
            raffle.rewards = rewards
        }

        return raffle
    }

    override fun getRaffleInfoById(raffleId: Int): RaffleInfo? {
        val raffle = raffleDao.getRaffleById(raffleId)

        if (raffle != null) {
            val rewards = raffleRewardDao.getRewardsByRaffleId(raffleId)
            raffle.rewards = rewards
        }

        return raffle
    }

    @Transactional
    override fun saveRaffleInfo(raffleInfo: RaffleInfo): Int {
        var result = 0

        if (raffleInfo.raffle_id == null || raffleInfo.raffle_id == 0) { // insert
            val introKey = articlesDao.saveArticle(raffleInfo.intro, settingsProperties.mallId)
            if (introKey <= 0) return result

            raffleInfo.intro_id = introKey
            val key = raffleDao.saveRaffle(raffleInfo)

            if (key > 0) {
                for (reward in raffleInfo.rewards) {
                    reward.raffle_id = key
                    result = raffleRewardDao.saveRaffleReward(reward)

                    if (result <= 0) throw MallDBException()
                }
            } else {
                throw MallDBException()
            }
        } else {
//            val raffle = getRaffleInfo(raffleInfo.intro_id)
//            raffleInfo.raffle_id = raffle!!.raffle_id

            result = articlesDao.editArticle(raffleInfo.intro_id, raffleInfo.intro)

            if (result <= 0) return result

            result = raffleDao.updateRaffle(raffleInfo)

            if (result > 0) {
                result = raffleRewardDao.delRewardsByRaffleId(raffleInfo.raffle_id)
                if (result <= 0) throw MallDBException()

                for (reward in raffleInfo.rewards) {
                    result = raffleRewardDao.saveRaffleReward(reward)

                    if (result <= 0) throw MallDBException()
                }
            }
        }

        return result
    }

    override fun multipleGetRaffleLogsCount(it: RaffleFilterCondition): Int {
        return raffleDao.multipleGetRaffleLogsCount(it)
    }

    override fun multipleGetRaffleLogs(it: RaffleFilterCondition): List<RaffleLog> {
        return raffleDao.multipleGetRaffleLogs(it)
    }


}
