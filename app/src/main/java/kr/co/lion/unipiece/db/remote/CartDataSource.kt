package kr.co.lion.unipiece.db.remote

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kr.co.lion.unipiece.model.AuthorInfoData
import kr.co.lion.unipiece.model.CartData
import kr.co.lion.unipiece.model.DeliveryData
import kr.co.lion.unipiece.model.PieceInfoData

class CartDataSource {

    private val db = Firebase.firestore.collection("Cart")
    private val pieceInfoDb = Firebase.firestore.collection("PieceInfo")

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

    // userIdx에 해당하는 장바구니 데이터로 작품정보를 들고온다.
    suspend fun getCartDataByUserIdx(userIdx: Int): List<PieceInfoData> {
        return try {
            // Cart 컬렉션에서 userIdx로 장바구니 데이터 가져오기
            val cartQuery = db.whereEqualTo("userIdx", userIdx)
            val cartQuerySnapshot = cartQuery.get().await()
            // 장바구니 데이터에서 pieceIdx 추출하기
            val pieceIdxs = cartQuerySnapshot.map { it.toObject(CartData::class.java).pieceIdx }.distinct()

            // 작품 정보를 담을 리스트 초기화
            val pieceInfoList = mutableListOf<PieceInfoData>()

            // PieceInfo 컬렉션에서 각 pieceIdx에 해당하는 작품 정보 가져오기
            for (pieceIdx in pieceIdxs) {
                val pieceInfoQuery = pieceInfoDb.whereEqualTo("pieceIdx", pieceIdx)
                val pieceInfoQuerySnapshot = pieceInfoQuery.get().await()
                pieceInfoList.addAll(pieceInfoQuerySnapshot.map { it.toObject(PieceInfoData::class.java) })
            }

            return pieceInfoList
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error in getCartDataByUserIdx: ${e.message}")
            emptyList()
        }
    }

    // authorIdx에 해당하는 작품정보 데이터를 들고온다.

    // 선택한 장바구니  장바구니에 담긴 데이터를 삭제한다.
    suspend fun deleteCartDataByUserIdx(userIdx:Int): Int{
        return try {
            val query = db.whereEqualTo("userIdx", userIdx)

            val querySnapshot = query.get().await()

            val result = querySnapshot.documents[0].reference.delete().toString().toInt()
            result
        } catch (e:Exception){
            Log.e("Firebase Error", "Error dbGetCartPieceByUserIdx: ${e.message}")
            0
        }
    }
}
