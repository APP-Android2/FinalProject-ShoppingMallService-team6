package kr.co.lion.unipiece.model

import com.google.firebase.Timestamp

data class PieceInfoData(
    var authorName: String = "",
    var pieceName: String = "",
    var makeYear: String = "",
    var pieceSize: String = "",
    var pieceMaterial: String = "",
    var pieceLike: Int = 0,
    var pieceImg: String = "",
    var pieceDate: Timestamp = Timestamp.now(),
    var piecePrice: Int = 0,
    var pieceIdx: Int = -1,
    var authorIdx: Int = -1,
    var pieceSaleState: Boolean = false,
    var pieceSort: String = "",
    var pieceDetailSort: String = "",
    var pieceInfo: String = "",

)
