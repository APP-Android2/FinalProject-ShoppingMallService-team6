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
    constructor() : this(0, 0, "", "무명작가", "OO대학 OO학과", "작가 소개하는 내용", "", "128", "")
}