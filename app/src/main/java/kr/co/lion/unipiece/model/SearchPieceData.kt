package kr.co.lion.unipiece.model

import kr.co.lion.unipiece.R

data class SearchPieceData(
    var pieceImg: Int = R.drawable.test_piece_img,
    var authorName: String = "홍길동",
    var pieceName: String = "작품 이름",
    var piecePrice: String = "100,000원"
)
