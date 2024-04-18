package kr.co.lion.unipiece.db.remote

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

    // 인기순으로 전체 작품 정보 불러오기
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

    // 인기순으로 대학별 작품 정보 불러오기
    suspend fun getPopPieceSort(category: String): List<PieceInfoData> {
        return try{
            val query = pieceInfoStore.whereEqualTo("pieceSaleState", true)
                .whereEqualTo("pieceSort", category)
                .orderBy("pieceLike", Query.Direction.DESCENDING)

            val querySnapShot = query.get().await()
            querySnapShot.map { it.toObject(PieceInfoData::class.java) }

        } catch (e: Exception) {
            Log.e("Firebase Error", "Error getPopPieceInfo: ${e.message}")
            emptyList()
        }

    }

    // 인기순으로 상세 카테고리별 정보 불러오기
    suspend fun getPopPieceDetailSort(detailCategory: String): List<PieceInfoData> {
        return try{
            val query = pieceInfoStore.whereEqualTo("pieceSaleState", true)
                .whereEqualTo("pieceDetailSort", detailCategory)
                .orderBy("pieceLike", Query.Direction.DESCENDING)

            val querySnapShot = query.get().await()
            querySnapShot.map { it.toObject(PieceInfoData::class.java) }

        } catch (e: Exception) {
            Log.e("Firebase Error", "Error getPopPieceInfo: ${e.message}")
            emptyList()
        }

    }

    // 신규순으로 전체 작품 정보 불러오기
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

    // 신규순으로 대학별 작품 정보 불러오기
    suspend fun getNewPieceSort(category: String): List<PieceInfoData> {
        return try{
            val query = pieceInfoStore.whereEqualTo("pieceSaleState", true)
                .whereEqualTo("pieceSort", category)
                .orderBy("pieceDate", Query.Direction.DESCENDING)

            val querySnapShot = query.get().await()
            querySnapShot.map { it.toObject(PieceInfoData::class.java) }

        } catch (e: Exception) {
            Log.e("Firebase Error", "Error getPopPieceInfo: ${e.message}")
            emptyList()
        }

    }

    // 신규순으로 상세 카테고리별 정보 불러오기
    suspend fun getNewPieceDetailSort(detailCategory: String): List<PieceInfoData> {
        return try{
            val query = pieceInfoStore.whereEqualTo("pieceSaleState", true)
                .whereEqualTo("pieceDetailSort", detailCategory)
                .orderBy("pieceDate", Query.Direction.DESCENDING)

            val querySnapShot = query.get().await()
            querySnapShot.map { it.toObject(PieceInfoData::class.java) }

        } catch (e: Exception) {
            Log.e("Firebase Error", "Error getPopPieceInfo: ${e.message}")
            emptyList()
        }

    }

    // 작품 이미지 url 받아오기
    suspend fun getPieceInfoImg(pieceIdx: String, pieceImg: String): String? {
        val path = "PieceInfo/$pieceIdx/$pieceImg"
        return try {
            storage.child(path).downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error getPieceInfoImg: ${e.message} ${path}")
            null
        }
    }

    // 작가별 작품 불러오기
    suspend fun getAuthorPieceInfo(authorIdx: Int): List<PieceInfoData> {
        return try{
            val query = pieceInfoStore.whereEqualTo("pieceSaleState", true)
                .whereEqualTo("authorIdx", authorIdx)
                .orderBy("pieceDate", Query.Direction.DESCENDING)

            val querySnapShot = query.get().await()
            querySnapShot.map { it.toObject(PieceInfoData::class.java) }

        } catch (e: Exception) {
            Log.e("Firebase Error", "Error getPopPieceInfo: ${e.message}")
            emptyList()
        }
    }

    // 작품 아이디로 작품 정보 불러오기
    suspend fun getIdxPieceInfo(pieceIdx: Int): PieceInfoData? {
        return try{
            val query = pieceInfoStore.whereEqualTo("pieceIdx", pieceIdx)

            val querySnapShot = query.get().await()
            querySnapShot.documents.first()?.toObject(PieceInfoData::class.java)
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error getPopPieceInfo: ${e.message}")
            null
        }
    }

}