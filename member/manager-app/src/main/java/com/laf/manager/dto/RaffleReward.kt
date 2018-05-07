package com.laf.manager.dto

data class RaffleReward(
        var trr_id: Int,
        var raffle_id: Int,
        var reward_name: String,
        var reward_type: Int,
        var reward_picture: String,
        var inventory: Int,
        var hit_rate: Double,
        var reward_value: Int,
        var sort_id: Short
) {
    constructor(
            raffle_id: Int,
            reward_name: String,
            reward_type: Int,
            reward_picture: String,
            inventory: Int,
            hit_rate: Double,
            reward_value: Int,
            sort_id: Short
    ) : this(0, raffle_id, reward_name, reward_type, reward_picture, inventory,
            hit_rate, reward_value, sort_id)

    constructor() : this(0, 0, "", 0, "", 0, 0.0, 0, 0)
}
