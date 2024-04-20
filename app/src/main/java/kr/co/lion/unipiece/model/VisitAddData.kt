package kr.co.lion.unipiece.model

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat

data class VisitAddData(
    var visitorName:String = "",
    var visitorPhone:String = "",
    var visitorNumber:String = "",
    var visitorDate: Timestamp = Timestamp.now(),
    var visitState:String = "",
    val userIdx:Int = -1,
    val visitIdx:Int = -1
){
    // 전시실 방문 신청 수정에 사용할 Map으로 변환
    fun instanceToMap():Map<String,Any?>{
        return mapOf(
            "visitorName" to visitorName,
            "visitorPhone" to visitorPhone,
            "visitorNumber" to visitorNumber,
            "visitorDate" to visitorDate
        )
    }

    // Timestamp를 String으로 변환
    fun timestampToString():String{
        val dateFormat = SimpleDateFormat("yyyy.MM.dd")
        return dateFormat.format(visitorDate.toDate()).toString()
    }
}
