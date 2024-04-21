package kr.co.lion.unipiece.db.remote

import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.co.lion.unipiece.model.PieceAddInfoData
import kr.co.lion.unipiece.model.PieceInfoData
import java.util.UUID

class PieceAddInfoDataSource {
    private val db = Firebase.firestore
    private val storageRef = Firebase.storage.reference

    // 작품 등록 정보 저장
    suspend fun addPieceInfo(pieceAddInfoData: PieceAddInfoData): Boolean {
        return try {
            val pieceAddInfoId = db.collection("PieceAddInfo").document().id

            val pieceAddInfoDataMap = hashMapOf(
                "addAuthorName" to pieceAddInfoData.addAuthorName,
                "addPieceName" to pieceAddInfoData.addPieceName,
                "addPieceSort" to pieceAddInfoData.addPieceSort,
                "addPieceDetailSort" to pieceAddInfoData.addPieceDetailSort,
                "addMakeYear" to pieceAddInfoData.addMakeYear,
                "addPieceSize" to pieceAddInfoData.addPieceSize,
                "addPieceMaterial" to pieceAddInfoData.addPieceMaterial,
                "addPieceInfo" to pieceAddInfoData.addPieceInfo,
                "addPieceImg" to pieceAddInfoData.addPieceImg,
                "addPiecePrice" to pieceAddInfoData.addPiecePrice,
                "addPieceState" to pieceAddInfoData.addPieceState,
                "addPieceMessage" to pieceAddInfoData.addPieceMessage,
                "addPieceDate" to pieceAddInfoData.addPieceDate,
                "authorIdx" to pieceAddInfoData.authorIdx,
                "pieceIdx" to pieceAddInfoData.pieceIdx,
                "addPieceIdx" to pieceAddInfoData.addPieceIdx
            )

            db.collection("PieceAddInfo")
                .document(pieceAddInfoId)
                .set(pieceAddInfoDataMap)
                .await()

            true
        } catch (e: Exception) {
            false
        }
    }

    // authorIdx로 작품 등록 정보 가져오기
    suspend fun getPieceAddInfo(authorIdx: Int) : List<PieceAddInfoData> {
        return try {
            val querySnapshot = db.collection("PieceAddInfo")
                .whereEqualTo("authorIdx", authorIdx)
                .orderBy("addPieceDate", Query.Direction.DESCENDING)
                .get()
                .await()

            querySnapshot.toObjects(PieceAddInfoData::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // addPieceIdx로 작품 등록 정보 가져오기
    suspend fun getPieceAddInfoByAddPieceIdx(addPieceIdx: Int): PieceAddInfoData? {
        return try {
            val querySnapshot = db.collection("PieceAddInfo")
                .whereEqualTo("addPieceIdx", addPieceIdx)
                .get()
                .await()

            querySnapshot.documents.first()?.toObject(PieceAddInfoData::class.java)
        } catch (e: Exception) {
            null
        }
    }

    // 작품 등록 정보 업데이트
    suspend fun updatePieceAddInfo(pieceAddInfoData: PieceAddInfoData): Boolean {
        return try {
            val addPieceIdx = pieceAddInfoData.addPieceIdx

            val updatedData = mapOf(
                "addPieceName" to pieceAddInfoData.addPieceName,
                "addMakeYear" to pieceAddInfoData.addMakeYear,
                "addPieceSize" to pieceAddInfoData.addPieceSize,
                "addPieceMaterial" to pieceAddInfoData.addPieceMaterial,
                "addPieceInfo" to pieceAddInfoData.addPieceInfo,
                "addPieceImg" to pieceAddInfoData.addPieceImg,
                "addPiecePrice" to pieceAddInfoData.addPiecePrice,
            )

            val querySnapshot = db.collection("PieceAddInfo")
                .whereEqualTo("addPieceIdx", addPieceIdx)
                .get()
                .await()

            querySnapshot.documents[0].reference.update(updatedData).await()

            true
        } catch (e: Exception) {
            Log.d("PieceAddInfoDataSource", "pieceAddInfoData.addPieceIdx: ${pieceAddInfoData.addPieceIdx}")
            Log.e("PieceAddInfoDataSource", "Failed to update PieceAddInfo", e)
            false
        }
    }


    // 작품 등록 정보 시퀀스 가져오기
    suspend fun getPieceAddSequence(): Int {
        return try {
            val sequenceSnapshot = db.collection("Sequence")
                .document("PieceAddSequence")
                .get()
                .await()

            sequenceSnapshot.getLong("value")?.toInt() ?: -1
        } catch (e: Exception) {
            -1
        }
    }

    // 작품 등록 정보 시퀀스 업데이트
    suspend fun updatePieceAddSequence(pieceAddSequence: Int) {
        try {
            val pieceAddSequenceDocument = db.collection("Sequence")
                .document("PieceAddSequence")

            val map = mutableMapOf<String, Long>()
            map["value"] = pieceAddSequence.toLong()

            pieceAddSequenceDocument.set(map).await()
        } catch (e: Exception) {
            Log.e("firebase", "Failed to update pieceAddSequence", e)
        }
    }

    // 작품 등록 정보 이미지 저장
    fun uploadImage(authorIdx: Int, imageUri: Uri): String {
        val imageFileName = "${UUID.randomUUID()}.jpg"
        val imageRef = storageRef.child("addPieceInfo/${authorIdx}/${imageFileName}")

        imageRef.putFile(imageUri)

        return imageFileName
    }

    // 작품 등록 정보 이미지 업데이트
    fun updateImage(authorIdx: Int, imageUri: Uri, imageFileName: String){
        val imageRef = storageRef.child("addPieceInfo/${authorIdx}/${imageFileName}")

        imageRef.putFile(imageUri)
    }

    // 작품 등록 정보 이미지 가져오기
    suspend fun getPieceAddInfoImage(authorIdx: Int, addPieceImg: String): Uri? {
        val path = "addPieceInfo/${authorIdx}/${addPieceImg}"

        return try {
            val storageRef = Firebase.storage.reference.child(path)
            val imageUri = storageRef.downloadUrl.await()

            imageUri
        } catch (e: Exception) {
            null
        }
    }
}