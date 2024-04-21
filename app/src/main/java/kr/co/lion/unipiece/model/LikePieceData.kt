package kr.co.lion.unipiece.model

import com.google.firebase.Timestamp

data class LikePieceData(
    val pieceIdx: Int = -1,
    val userIdx: Int = -1,
    val likePieceData: Timestamp = Timestamp.now()
)
