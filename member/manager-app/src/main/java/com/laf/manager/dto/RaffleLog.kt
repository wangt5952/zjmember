package com.laf.manager.dto

import java.util.*

data class RaffleLog(
        //i.title,l.member_name,l.member_mobile,i.required_points,l.trr_id,l.action_time,r.reward_name,r.reward_type
//        var r_id: Int,
        var title: String,
        var member_name: String,
        var member_mobile: String,
        //        var win: Boolean,
        var action_time: Date,
        var required_points: Int,
        var trr_id: Int,
        var reward_name: String?,
        var reward_type: Int

) {
}
