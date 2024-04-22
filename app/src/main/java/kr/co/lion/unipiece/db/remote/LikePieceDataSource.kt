package kr.co.lion.unipiece.db.remote

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kr.co.lion.unipiece.model.LikePieceData

class LikePieceDataSource {

    private val db = Firebase.firestore.collection("LikePiece")

    // 관심 작품 추가
    suspend fun addLikePiece(pieceIdx: Int, userIdx: Int){
        try {
            val addLikeData = LikePieceData(pieceIdx, userIdx, Timestamp.now())
            db.add(addLikeData).await()
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error getPopPieceInfo: ${e.message}")
        }
    }

    // 관심 작품 취소
    suspend fun cancelLikePiece(pieceIdx: Int, userIdx: Int){
        try {
            val query = db.whereEqualTo("pieceIdx", pieceIdx)
                .whereEqualTo("userIdx", userIdx)

            val querySnapshot = query.get().await()
            querySnapshot.documents.first()?.reference?.delete()

        } catch (e: Exception) {
            Log.e("Firebase Error", "Error getPopPieceInfo: ${e.message}")
        }
    }

    // 작품 별 관심 수 카운트
    suspend fun countLikePiece(pieceIdx: Int) : Int {
        return try {
            val query = db.whereEqualTo("pieceIdx", pieceIdx)

            val querySnapshot = query.get().await()
            querySnapshot.count()

        } catch (e: Exception) {
            Log.e("Firebase Error", "Error getPopPieceInfo: ${e.message}")
            -1
        }
    }

    // 작품에 관심을 눌렀는지 안 눌렀는지
    suspend fun isLikePiece(pieceIdx: Int, userIdx: Int): Boolean{
        return try{
            val query = db.whereEqualTo("pieceIdx", pieceIdx)
                .whereEqualTo("userIdx", userIdx)

            val querySnapshot = query.get().await()

            !querySnapshot.isEmpty
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error getPopPieceInfo: ${e.message}")
            false
        }
    }

    // userIdx로 관심 목록 가져오기
    suspend fun getUserLikedPiece(userIdx: Int): List<LikePieceData> {
        return try {
            val querySnapshot = db.whereEqualTo("userIdx", userIdx)
                .orderBy("likePieceData", Query.Direction.DESCENDING)
                .get()
                .await()

            Log.e("LikePieceDataSource", "getUserLikedPieces: ${ querySnapshot.toObjects(LikePieceData::class.java)}")

            querySnapshot.toObjects(LikePieceData::class.java)
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error getUserLikedPieces: ${e.message}")
            emptyList()
        }
    }
}