package kr.co.lion.unipiece.model

import com.google.firebase.Timestamp

data class PromoteInfoData (
    var promoteName:String = "",
    var promoteDate:String = "",
    var promoteContent:String = "",
    var promoteTime:Timestamp = Timestamp.now(),
    var promoteImg:String = "",
    var promoteLink:String = "",
    var homeIdx:Int = 0,
    var promoteMoney:String = "",
    var promotePlace:String = ""
)