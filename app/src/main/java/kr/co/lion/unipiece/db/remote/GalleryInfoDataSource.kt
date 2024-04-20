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
import kr.co.lion.unipiece.model.GalleryInfoData

class GalleryInfoDataSource {

    private val galleryInfo = Firebase.firestore.collection("GalleryInfo")
    private val storage = Firebase.storage.reference


    //이미지 전체의 데이터를 가져오는 메서드
    suspend fun getGalleryImg():List<String>{
        val db = storage.child("GalleryInfo")
        val listResult = db.listAll().await()
        val imageUrl = listResult.items.map { it.downloadUrl.await().toString() }
        return imageUrl
    }

    //이미지의 이름으로 데이터를 받아오는 메서드
    suspend fun getGalleryByImage(galleryImg:String) : GalleryInfoData?{
        var galleryInfoData:GalleryInfoData? = null

        val job1 = CoroutineScope(Dispatchers.IO).launch {
            val collectionReference = galleryInfo
            val querySnapshot = collectionReference.whereEqualTo("galleryInfoImg", galleryImg).get().await()
            if (!querySnapshot.isEmpty){
                galleryInfoData = querySnapshot.documents[0].toObject(GalleryInfoData::class.java)
            }
        }
        job1.join()
        return galleryInfoData
    }

    //날짜 순으로 모든 정보를 받아오기(TimeStamp)
    suspend fun getGalleryDataByDate():List<GalleryInfoData>{
        return try {
            val query = galleryInfo.orderBy("galleryTime", Query.Direction.DESCENDING)
            val querySnapshot = query.get().await()
            querySnapshot.map { it.toObject(GalleryInfoData::class.java) }
        }catch (e:Exception){
            emptyList()
        }
    }

    //이미지 이름을 Url로 변경하기
    suspend fun getGalleryInfoImg(image:String): String?{
        val path = "GalleryInfo/$image"
        return try {
            storage.child(path).downloadUrl.await().toString()
        }catch (e:Exception){
            null
        }
    }
}