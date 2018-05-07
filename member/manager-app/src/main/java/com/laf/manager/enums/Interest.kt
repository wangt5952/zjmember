package com.laf.manager.enums

enum class Interest(val value: Int, val theName: String) {
    NONE(-1, "无"),
    READ(0, "阅读"),
    MUSIC(1, "音乐"),
    NET_PLAY(2, "上网"),
    GAME(3, "游戏"),
    TOURISM(4, "旅游"),
    CAR(5, "汽车"),
    SHOPPING(6, "购物");

    fun value() = this.value

    fun theName() = this.theName

    companion object {
        @JvmStatic
        fun valueOf(value: Int): Interest {
            for (sources in Interest.values()) {
                if (sources.value == value) {
                    return sources
                }
            }

            throw IllegalArgumentException("No matching constant for [$value]")
        }
    }
}