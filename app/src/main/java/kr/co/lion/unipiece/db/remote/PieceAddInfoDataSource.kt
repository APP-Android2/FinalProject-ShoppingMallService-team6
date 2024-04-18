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


    fun uploadImage(authorIdx: Int, imageUri: Uri): String {
        val imageFileName = "${UUID.randomUUID()}.jpg"
        val imageRef = storageRef.child("addPieceInfo/${authorIdx}/${imageFileName}")

        imageRef.putFile(imageUri)

        return imageFileName
    }

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