package kr.co.lion.unipiece.model

import com.google.firebase.Timestamp

data class VisitAddData(
    var visitorName:String = "",
    var visitorPhone:String = "",
    var visitorNumber:String = "",
    var visitorDate: Timestamp = Timestamp.now(),
    var visitState:String = "",
    val userIdx:Int = -1
)
