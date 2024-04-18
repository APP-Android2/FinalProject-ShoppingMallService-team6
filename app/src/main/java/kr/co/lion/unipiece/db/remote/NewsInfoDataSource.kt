package kr.co.lion.unipiece.db.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await

class NewsInfoDataSource {

    private val newsInfo = Firebase.firestore.collection("NewsInfo")
    private val storage = Firebase.storage.reference

    //이미지 전체의 데이터를 받아오는 메서드
    suspend fun getNewsImage():List<String>{
        val db = storage.child("NewsInfo/")
        val listResult = db.listAll().await()
        val imageUrl = listResult.items.map { it.downloadUrl.await().toString() }
        return imageUrl
    }

}