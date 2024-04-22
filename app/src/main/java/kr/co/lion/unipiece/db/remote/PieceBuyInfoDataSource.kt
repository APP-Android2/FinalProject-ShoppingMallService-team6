package kr.co.lion.unipiece.db.remote

import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import kr.co.lion.unipiece.model.PieceBuyInfoData
import java.util.UUID

class PieceBuyInfoDataSource {
    private val db = Firebase.firestore.collection("PieceBuyInfo")
    private val storageRef = Firebase.storage.reference

    // 구매한 작품 정보 가져오기
    suspend fun getPieceBuyInfo(userIdx: Int): List<PieceBuyInfoData> {
        return try {
            val querySnapshot = db.whereEqualTo("userIdx", userIdx)
                .orderBy("pieceBuyDate", Query.Direction.DESCENDING)
                .get()
                .await()

            querySnapshot.toObjects(PieceBuyInfoData::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // 주문 취소
    suspend fun updatePieceBuyCancel(pieceBuyInfoData: PieceBuyInfoData): Boolean {
        return try {
            val pieceBuyIdx = pieceBuyInfoData.pieceBuyIdx

            val updatedData = mapOf(
                "pieceBuyState" to pieceBuyInfoData.pieceBuyState,
                "pieceBuyCancel" to pieceBuyInfoData.pieceBuyCancel,
            )

            val querySnapshot = db.whereEqualTo("pieceBuyIdx", pieceBuyIdx)
                .get()
                .await()

            querySnapshot.documents[0].reference.update(updatedData).await()

            true
        } catch (e: Exception) {
            Log.d("PieceAddInfoDataSource", "pieceBuyInfoData.pieceBuyIdx: ${pieceBuyInfoData.pieceBuyIdx}")
            Log.e("PieceAddInfoDataSource", "Failed to update PieceAddInfo", e)
            false
        }
    }

    // 반품 신청
    suspend fun updatePieceBuyRefund(pieceBuyInfoData: PieceBuyInfoData): Boolean {
        return try {
            val pieceBuyIdx = pieceBuyInfoData.pieceBuyIdx

            val updatedData = mapOf(
                "pieceBuyState" to pieceBuyInfoData.pieceBuyState,
                "pieceBuyRefund" to pieceBuyInfoData.pieceBuyRefund,
                "pieceBuyDetailRefund" to pieceBuyInfoData.pieceBuyDetailRefund,
                "pieceBuyImg" to pieceBuyInfoData.pieceBuyImg,
            )

            val querySnapshot = db.whereEqualTo("pieceBuyIdx", pieceBuyIdx)
                .get()
                .await()

            querySnapshot.documents[0].reference.update(updatedData).await()

            true
        } catch (e: Exception) {
            Log.d("PieceAddInfoDataSource", "pieceBuyInfoData.pieceBuyIdx: ${pieceBuyInfoData.pieceBuyIdx}")
            Log.e("PieceAddInfoDataSource", "Failed to update PieceAddInfo", e)
            false
        }
    }

    // 반품 신청 이미지 저장
    fun uploadRefundImage(userIdx: Int, imageUri: Uri): String {
        val imageFileName = "${UUID.randomUUID()}.jpg"
        val imageRef = storageRef.child("pieceBuyInfo/${userIdx}/${imageFileName}")

        imageRef.putFile(imageUri)

        return imageFileName
    }
}