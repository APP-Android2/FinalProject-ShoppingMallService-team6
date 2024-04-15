package kr.co.lion.unipiece.db.remote

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
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

    fun uploadImage(imageUri: Uri): String {
        val imageFileName = "${UUID.randomUUID()}.jpg"
        val imageRef = storageRef.child("addPieceInfo/$imageFileName")

        imageRef.putFile(imageUri)

        return imageFileName
    }
}