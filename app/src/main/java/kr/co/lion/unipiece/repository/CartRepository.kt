package kr.co.lion.unipiece.repository

import kr.co.lion.unipiece.db.remote.CartDataSource
import kr.co.lion.unipiece.model.CartData

class CartRepository {
    private val cartDataSource = CartDataSource()

    suspend fun  insertCartData(cartData: CartData) = cartDataSource.insertCartData(cartData)

    suspend fun isCartPiece(pieceIdx: Int, userIdx: Int) = cartDataSource.isCartPiece(pieceIdx, userIdx)

    suspend fun cancelCartPiece(pieceIdx: Int, userIdx: Int) = cartDataSource.cancelCartPiece(pieceIdx, userIdx)

    suspend fun getCartDataByUserIdx(userIdx: Int) = cartDataSource.getCartDataByUserIdx(userIdx)

}
