package kr.co.lion.unipiece.model

import java.sql.Timestamp


data class CartData(
    var userIdx: Int = 0,
    var pieceIdx: Int = 0,
    var cartPieceDate: Timestamp

)