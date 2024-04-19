package kr.co.lion.unipiece.db.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
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

    //날짜 순으로 모든 정보 받아오기(TimeStamp)
    suspend fun getNewsDataByDate(): List<NewsInfoData>{
        return try {
            //private val newsInfo = Firebase.firestore.collection("NewsInfo")
            val query = newsInfo.orderBy("newsTime", Query.Direction.DESCENDING)
            val querySnapshot = query.get().await()
            querySnapshot.map { it.toObject(NewsInfoData::class.java) }
        }catch (e:Exception){
            emptyList()
        }
    }

    //이미지 이름을 이미지 url로 바꾸기
    suspend fun getNewsInfoImg(image:String): String?{
        val path = "NewsInfo/$image"
        return try {
            //private val storage = Firebase.storage.reference
            storage.child(path).downloadUrl.await().toString()
        }catch (e:Exception){
            null
        }
    }

}