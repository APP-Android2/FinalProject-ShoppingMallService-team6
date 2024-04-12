package kr.co.lion.unipiece.model

data class AuthorInfoData(
    var userIdx: Int,
    var authorIdx: Int,
    var authorImg: String,
    var authorName: String,
    var authorBasic: String,
    var authorInfo: String,
    var authorSale: String,
    var authorFollower: String,
    var authorDate: String,
){
    constructor() : this(0, 0, "", "", "", "", "", "", "")
}