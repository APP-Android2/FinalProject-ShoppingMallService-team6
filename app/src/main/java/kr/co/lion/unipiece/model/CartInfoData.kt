package kr.co.lion.unipiece.model

data class CartInfoData(
    var pieceInfo: PieceInfoData = PieceInfoData(),
    var isChecked: Boolean = false
)