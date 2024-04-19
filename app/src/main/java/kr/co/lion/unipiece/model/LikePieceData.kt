package kr.co.lion.unipiece.model

import com.google.firebase.Timestamp

data class LikePieceData(
    var pieceIdx: Int = -1,
    var userIdx: Int = -1,
    var likePieceData: Timestamp = Timestamp.now()
)
