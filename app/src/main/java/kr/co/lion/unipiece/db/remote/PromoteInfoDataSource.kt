package kr.co.lion.unipiece.db.remote

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await

class PromoteInfoDataSource {

    private val pieceInfoStore = Firebase.firestore.collection("PieceInfo")
    private val storage = Firebase.storage.reference


    //이미지 전체의 데이터를 받아오는 메서드
    suspend fun getPromoteImage(): List<String>{
        val db = Firebase.storage.reference.child("PromoteInfo/")
        val listResult = db.listAll().await()
        val imageUrls = listResult.items.map { it.downloadUrl.await().toString() }
        return imageUrls
    }
}