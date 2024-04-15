package kr.co.lion.unipiece.db.remote

import java.net.URI
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import kr.co.lion.unipiece.model.PieceInfoData

class PieceInfoDataSource {

    private val pieceInfoStore = Firebase.firestore.collection("PieceInfo")
    private val storage = Firebase.storage.reference

    suspend fun getPopPieceInfo(): List<PieceInfoData> {
        return try{
            val query = pieceInfoStore.whereEqualTo("pieceSaleState", true)
                .orderBy("pieceLike", Query.Direction.DESCENDING)

            val querySnapShot = query.get().await()
            querySnapShot.map { it.toObject(PieceInfoData::class.java) }

        } catch (e: Exception) {
            Log.e("Firebase Error", "Error getPopPieceInfo: ${e.message}")
            emptyList()
        }

    }

    suspend fun getNewPieceInfo(): List<PieceInfoData> {
        return try{
            val query = pieceInfoStore.whereEqualTo("pieceSaleState", true)
                .orderBy("pieceDate", Query.Direction.DESCENDING)

            val querySnapShot = query.get().await()
            querySnapShot.map { it.toObject(PieceInfoData::class.java) }

        } catch (e: Exception) {
            Log.e("Firebase Error", "Error getPopPieceInfo: ${e.message}")
            emptyList()
        }

    }

    suspend fun getPieceInfoImg(pieceIdx: String, pieceImg: String): URI? {
        val path = "PieceInfo/$pieceIdx/$pieceImg"
        return try {
            val response = storage.child(path).downloadUrl.await().toString()
            URI.create(response)
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error getPieceInfoImg: ${e.message} ${path}")
            null
        }
    }
}