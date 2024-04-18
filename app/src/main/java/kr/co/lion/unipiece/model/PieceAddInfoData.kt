package kr.co.lion.unipiece.model

import com.google.firebase.Timestamp

data class PieceAddInfoData(
    var addAuthorName: String = "",
    var addPieceName: String = "",
    var addPieceSort: String = "",
    var addPieceDetailSort: String = "",
    var addMakeYear: String = "",
    var addPieceSize: String = "",
    var addPieceMaterial: String = "",
    var addPieceInfo: String = "",
    var addPieceImg: String = "",
    var addPiecePrice: Int = 0,
    var addPieceState: String = "",
    var addPieceMessage: String = "",
    var addPieceDate: Timestamp = Timestamp.now(),
    var authorIdx: Int = 0,
    var pieceIdx: Int = 0,
    var addPieceIdx: Int = 0
)