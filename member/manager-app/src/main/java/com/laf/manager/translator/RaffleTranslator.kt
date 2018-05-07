package com.laf.manager.translator

import com.jayway.jsonpath.JsonPath
import com.laf.manager.SettingsProperties
import com.laf.manager.dto.RaffleInfo
import com.laf.manager.dto.RaffleReward
import com.laf.manager.querycondition.raffle.RaffleEditCondition
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.ArrayList

@Component
class RaffleTranslator {

    @Autowired
    lateinit var settingsProperties: SettingsProperties

    fun condition2Info(condition: RaffleEditCondition): RaffleInfo? {

        val json = condition.rewardsData
        val list = JsonPath.parse(json).read("$", List::class.java) as List<Map<String, Any>>
        val rewards = ArrayList<RaffleReward>()
        val raffleId = if (condition.raffleId == null)  0 else condition.raffleId
        val introId = if (condition.introId == null) 0 else condition.introId

        if (list.size != condition.pictures.size) return null

        var sort: Short = 1
        list.forEach { map ->
            val reward = RaffleReward()
            reward.raffle_id = raffleId!!
            reward.reward_name = map["name"].toString()
            reward.reward_type = Integer.valueOf(map["type"].toString())
            reward.inventory = Integer.valueOf(map["inventory"].toString())
            reward.hit_rate = java.lang.Double.valueOf(map["hit_rate"].toString())
            reward.reward_value = Integer.valueOf(map["value"].toString())
            if (condition.pictures[sort-1].isEmpty())
                reward.reward_picture = map["pictureName"].toString()
            else
                reward.reward_picture = condition.pictures[sort-1]
            reward.sort_id = sort++
            rewards.add(reward)
        }

        val raffle = RaffleInfo(
                raffleId!!,
                condition.raffleTitle,
                condition.raffleTimeStart,
                condition.raffleTimeEnd,
                condition.requiredPoints,
                introId!!,
                condition.intro,
                settingsProperties.mallId,
                99999,
                condition.activityCode

        )

        raffle.rewards = rewards

        return raffle
    }
}