package kr.co.lion.unipiece.model

import com.google.firebase.Timestamp

data class AuthorAddData (
    var userIdx:Int = 0,
    var authorFile:String = "",
    var authorName:String = "",
    var authorMajor:String = "",
    var authorUni:String = "",
    var authorInfo:String = "",
    var authorImg:String? = "",
    var authorNew:Boolean = false,
    var authorIdx:Int = 0,
    var authorUniState:String = "",
    var authorRegisterTime:Timestamp = Timestamp.now()
)