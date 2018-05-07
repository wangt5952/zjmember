package com.laf.manager.querycondition.raffle

class RaffleEditCondition : RaffleCondition {

        var raffleId: Int? = 0
        var raffleTitle = ""
        var raffleTimeStart = 0L
        var raffleTimeEnd = 0L
        var requiredPoints = 0
        var activityCode = ""
        var rewardsData = ""
        var introId: Int? = 0
        var intro = ""
        var pictureName = ""
        var pictures = listOf<String>()
}