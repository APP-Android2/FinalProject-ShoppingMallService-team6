package kr.co.lion.unipiece.model

import com.google.firebase.Timestamp


data class CartData(
    var userIdx: Int,
    var pieceIdx: Int,
    var cartPieceDate: Timestamp,

){
    constructor() : this(0, 0, Timestamp.now())
}