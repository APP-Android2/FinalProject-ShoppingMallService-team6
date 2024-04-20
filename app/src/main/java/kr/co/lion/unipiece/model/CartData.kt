package kr.co.lion.unipiece.model

import com.google.firebase.Timestamp


data class CartData(
    val userIdx: Int = -1,
    val pieceIdx: Int = -1,
    val cartPieceDate: Timestamp = Timestamp.now(),

)