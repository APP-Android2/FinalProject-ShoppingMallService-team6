package kr.co.lion.unipiece.repository

import kr.co.lion.unipiece.db.remote.CartDataSource
import kr.co.lion.unipiece.model.PieceInfoData

class CartRepository {
    private val cartDataSource = CartDataSource()

    // 작품 번호를 통해 장바구니에 담을 작품 정보를 가져와 반환한다.
    suspend fun getPieceDataByIdx(pieceIdx : Int):MutableList<PieceInfoData> =
        cartDataSource.getPieceDataByIdx(pieceIdx)
}