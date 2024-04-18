package kr.co.lion.unipiece.db.remote

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kr.co.lion.unipiece.model.CartData

class CartDataSource {

    private val db = Firebase.firestore.collection("Cart")

    // 장바구니 안에 작품 넣기
    suspend fun insertCartData(cartData: CartData) {
        db.add(cartData).await()
    }

    // 장바구니 안에 작품이 있는지 없는지 판단
    suspend fun isCartPiece(pieceIdx: Int, userIdx: Int): Boolean{
        return try{
            val query = db.whereEqualTo("pieceIdx", pieceIdx)
                .whereEqualTo("userIdx", userIdx)

            val querySnapshot = query.get().await()

            !querySnapshot.isEmpty
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error isCartPiece: ${e.message}")
            false
        }
    }

    // 장바구니 취소 코드

    suspend fun cancelCartPiece(pieceIdx: Int, userIdx: Int){
        try {
            val query = db.whereEqualTo("pieceIdx", pieceIdx)
                .whereEqualTo("userIdx", userIdx)

            val querySnapshot = query.get().await()
            querySnapshot.documents.first()?.reference?.delete()

        } catch (e: Exception) {
            Log.e("Firebase Error", "Error getPopPieceInfo: ${e.message}")
        }
    }

}
