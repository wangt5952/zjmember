package com.laf.manager.dto

data class RaffleInfo(
        var raffle_id: Int,
        var title: String,
        var raffle_time_start: Long,
        var raffle_time_end: Long,
        var required_points: Int,
        var intro_id: Int,
        var intro: String,
        var mall_id: Int,
        var is_active: Boolean,
        var times: Int,
        var daily: Boolean,
        var activity_code: String,
        var picture: String?,
        var rewards: List<RaffleReward>
) {
    constructor(
            raffle_id: Int,
            title: String,
            raffle_time_start: Long,
            raffle_time_end: Long,
            required_points: Int,
            intro_id: Int,
            intro: String,
            mall_id: Int,
            times: Int,
            activity_code: String
    ) : this(raffle_id, title, raffle_time_start, raffle_time_end, required_points,intro_id,intro,
            mall_id,true,times,false,activity_code, null, emptyList())

    constructor(
        raffle_id: Int,
        title: String,
        raffle_time_start: Long,
        raffle_time_end: Long,
        required_points: Int,
        activity_code: String
    ) : this(raffle_id, title, raffle_time_start, raffle_time_end, required_points, 0, "", 0, false, 0, false, activity_code, "", emptyList())
}



