package com.laf.manager.enums

enum class RaffleRewardType(val value: Int, val theName: String) {
    NONE(-1, "没有中奖"),
    COUPON(1, "优惠券"),
    GIFT(2, "礼品券"),
    POINTS(3, "积分");

    fun value() = this.value

    fun theName() = this.theName

    companion object {
        @JvmStatic
        fun valueOf(value: Int): RaffleRewardType {
            for (sources in RaffleRewardType.values()) {
                if (sources.value == value) {
                    return sources
                }
            }

            throw IllegalArgumentException("No matching constant for [$value]")
        }
    }
}