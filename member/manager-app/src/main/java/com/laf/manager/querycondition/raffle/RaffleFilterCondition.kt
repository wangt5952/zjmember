package com.laf.manager.querycondition.raffle

class RaffleFilterCondition {

    var title = ""
    var mobile = ""
    var username = ""
    var actionTimeStart = 0L
    var actionTimeEnd = 0L
    var isWin = -1
    var mallId = 0
    var size = 10
    var page = 1
    val offset: Int
        get() {
            return (this.page - 1) * this.size
        }
}


//data class RaffleFilterCondition(
//
//        var title: String,
//        var mobile: String,
//        var username: String,
//        var actionTimeStart: Long,
//        var actionTimeEnd: Long,
//        var isWin: Int,
//        var mallId: Int,
//        var size: Int,
//        var page: Int,
//        val offset: Int
//) {
////        get() {
////            return (this.page - 1) * this.size
////        }
//
////    constructor(title: String,,
////                mobile: String,,
////                username: String,,
////                actionTimeStart: Long,,
////                actionTimeEnd: Long,,
////                isWin: Int,,
////                mallId: Int,,
////                size: Int,,
////                page: Int,
////    ) : this(title, mobile, username, actionTimeStart, actionTimeEnd, isWin, mallId, size, page)
//
//    constructor() : this("", "", "", 0L, 0L, -1, 0, 10, 1, 1)
//}