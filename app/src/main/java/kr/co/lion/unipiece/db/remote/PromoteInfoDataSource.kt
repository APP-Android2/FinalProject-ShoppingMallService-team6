package kr.co.lion.unipiece.db.remote

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
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
}