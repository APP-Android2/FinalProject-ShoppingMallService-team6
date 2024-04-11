package kr.co.lion.unipiece.model

import kr.co.lion.unipiece.R

data class SearchAuthorData(
    var authorImage: Int = R.drawable.mypage_icon,
    var authorName: String = "김길동",
    var followCount: String = "124 팔로워"
)
