package kr.co.lion.unipiece.model

import com.google.firebase.Timestamp

data class PieceInfoData(
    val authorName: String = "",
    val pieceName: String = "",
    val makeYear: String = "",
    val pieceSize: String = "",
    val pieceMaterial: String = "",
    val pieceLike: Int = 0,
    val pieceImg: String = "",
    val pieceDate: Timestamp = Timestamp.now(),
    val piecePrice: Int = 0,
    val pieceIdx: Int = -1,
    val authorIdx: Int = -1,
    val pieceSaleState: Boolean = false,
    val pieceSort: String = "",
    val pieceDetailSort: String = "",
    val pieceInfo: String = "",

)


