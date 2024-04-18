package kr.co.lion.unipiece.db.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.co.lion.unipiece.model.NewsInfoData

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

    //이미지의 이름으로 데이터를 받아오는 메서드
    suspend fun getNewsInfoByImage(newsImg:String) : NewsInfoData? {
        var newsInfoData:NewsInfoData? = null

        val job1 = CoroutineScope(Dispatchers.IO).launch {
            val collectionReference = newsInfo
            val querySnapshot = collectionReference.whereEqualTo("newsImg", newsImg).get().await()
            if (querySnapshot.isEmpty == false){
                newsInfoData = querySnapshot.documents[0].toObject(NewsInfoData::class.java)
            }
        }
        job1.join()
        return newsInfoData
    }

}