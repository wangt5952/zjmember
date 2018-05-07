package com.laf.manager.enums

enum class Degree(val value: Int, val theName: String) {
    NONE(-1, "无"),
    PRIMARY(0, "小学"),
    JUNIOR(1, "初中"),
    HIGH(2, "高中"),
    COLLEGE(3, "大专"),
    BACHELOR(4, "本科"),
    MASTER(5, "硕士"),
    DOCTORAlE(6, "博士");

    fun value() = this.value

    fun theName() = this.theName

    companion object {
        @JvmStatic
        fun valueOf(value: Int): Degree {
            for (sources in values()) {
                if (sources.value == value) {
                    return sources
                }
            }

            throw IllegalArgumentException("No matching constant for [$value]")
        }
    }
}