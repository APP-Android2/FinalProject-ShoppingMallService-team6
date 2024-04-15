package kr.co.lion.unipiece.model

import com.google.firebase.Timestamp

data class AuthorReviewData(
    val reviewIdx: Int,
    val userIdx: Int,
    val authorIdx: Int,
    val reviewContent: String,
    val reviewTime: Timestamp,
){
    constructor() : this(0, 0, 0, "멋진 리뷰", Timestamp.now())
}