package kr.co.lion.unipiece.db.remote

import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import kr.co.lion.unipiece.model.PieceAddInfoData
import java.util.UUID

class PieceAddInfoDataSource {
    private val db = Firebase.firestore
    private val storageRef = Firebase.storage.reference

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
                "addPieceDate" to pieceAddInfoData.addPieceDate,
                "authorIdx" to pieceAddInfoData.authorIdx
            )

            // 작품 정보 저장
            db.collection("PieceAddInfo")
                .document(pieceAddInfoId)
                .set(pieceAddInfoDataMap)
                .await()

            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getAuthorIdx(userIdx: Int): Int {
        return try {
            val querySnapshot = db.collection("AuthorInfo")
                .whereEqualTo("userIdx", userIdx)
                .get()
                .await()

            val authorIdx = querySnapshot.documents.firstOrNull()?.get("authorIdx") as? Long ?: 0

            authorIdx.toInt()

        } catch (e: Exception) {
            Log.e("firebase", "Failed to get authorIdx: ${Log.getStackTraceString(e)}")
            0
        }
    }

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

    fun uploadImage(imageUri: Uri): String {
        val imageFileName = "${UUID.randomUUID()}.jpg"
        val imageRef = storageRef.child("addPieceInfo/$imageFileName")

        imageRef.putFile(imageUri)

        return imageFileName
    }

    suspend fun getPieceAddInfoImage(addPieceImg: String): Uri? {
        val path = "addPieceInfo/$addPieceImg"

        return try {
            val storageRef = Firebase.storage.reference.child(path)
            val imageUri = storageRef.downloadUrl.await()

            imageUri
        } catch (e: Exception) {
            null
        }
    }

    suspend fun isAuthor(userIdx: Int): Boolean {
        return try {
            val querySnapshot = db.collection("AuthorInfo")
                .whereEqualTo("userIdx", userIdx)
                .get()
                .await()

            !querySnapshot.isEmpty
        } catch (e: Exception) {
            false
        }
    }

}