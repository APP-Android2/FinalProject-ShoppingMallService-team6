package kr.co.lion.unipiece.repository

import kr.co.lion.unipiece.db.remote.CartDataSource
import kr.co.lion.unipiece.model.CartData

class CartRepository {
    private val cartDataSource = CartDataSource()

    // 장바구니 시퀀스값을 가져온다.
    suspend fun getCartSequence() = cartDataSource.getCartSequence()

    // 장바구니 시퀀스 값을 업데이트 한다.
    suspend fun updateCartSequence(cartSequence: Int) = cartDataSource.updateCartSequence(cartSequence)

    // 작품 번호를 통해 장바구니에 담을 작품 정보를 가져와 반환한다.
    suspend fun getPieceDataByIdx(pieceIdx:Int) = cartDataSource.getPieceDataByIdx(pieceIdx)

    // 유저 번호를 통해 유저 정보를 가져와 반환한다.
    suspend fun getUserDataByIdx(userIdx:Int) = cartDataSource.getUserDataByIdx(userIdx)
}