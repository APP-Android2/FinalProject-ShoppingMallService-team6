package kr.co.lion.unipiece.db.remote

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.co.lion.unipiece.model.PromoteInfoData

class PromoteInfoDataSource {

    private val promoteInfo = Firebase.firestore.collection("PromoteInfo")
    private val storage = Firebase.storage.reference


    //이미지 전체의 데이터를 받아오는 메서드
    suspend fun getPromoteImage(): List<String>{
        val db = storage.child("PromoteInfo/")
        val listResult = db.listAll().await()
        val imageUrls = listResult.items.map { it.downloadUrl.await().toString() }
        return imageUrls
    }

    //이미지 이름으로 해당 정보를 가져오는 메서드
    suspend fun gettingPromoteInfoByImage(promoteImg:String) : PromoteInfoData? {

        var promoteInfoData:PromoteInfoData? = null

        val job1 = CoroutineScope(Dispatchers.IO).launch {
            val collectionReference = promoteInfo
            val querySnapshot = collectionReference.whereEqualTo("promoteImg", promoteImg).get().await()
            if (querySnapshot.isEmpty == false){
                promoteInfoData = querySnapshot.documents[0].toObject(PromoteInfoData::class.java)
            }
        }
        job1.join()

        return promoteInfoData
    }


    //날짜순으로 모든 정보 받아오기
    suspend fun gettingDataByDate(): List<PromoteInfoData>{
        return try {
            val query = promoteInfo.orderBy("promoteTime", Query.Direction.DESCENDING)
            val querySnapshot = query.get().await()
            querySnapshot.map { it.toObject(PromoteInfoData::class.java) }
        }catch (e:Exception){
            emptyList()
        }
    }

    //이미지URL 받아오기
    suspend fun getPromoteInfoImg(image:String): String?{
        val path = "PromoteInfo/$image"
        return try {
            storage.child(path).downloadUrl.await().toString()
        }catch (e:Exception){
            null
        }
    }
}