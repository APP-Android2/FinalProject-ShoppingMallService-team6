package kr.co.lion.unipiece.model

import com.google.firebase.Timestamp

data class NewsInfoData (
    var newsName:String = "",
    var newsDate:String = "",
    var newsContent:String = "",
    var newsTime:Timestamp = Timestamp.now(),
    var newsImg:String = "",
    var homeIdx:Int = 0,
    var newsSale:String = "",

)