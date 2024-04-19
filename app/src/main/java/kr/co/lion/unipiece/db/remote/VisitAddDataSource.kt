package kr.co.lion.unipiece.db.remote

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await
import kr.co.lion.unipiece.model.VisitAddData

class VisitAddDataSource {

    private val collectionReference = Firebase.firestore.collection("VisitAdd")

    // 전시실 방문 신청서를 저장한다.
    suspend fun insertVisitAddData(visitAddData: VisitAddData):Boolean{
        return try{
            collectionReference.add(visitAddData).await()
            true
        }catch (e:Exception){
            Log.e("Firebase Error", "Error insertVisitAddData: ${e.message}")
            false
        }
    }

    // 전시실 방문 신청 번호를 통해 전시실 방문 신청 정보를 가져와 반환한다
    suspend fun getVisitAddDataByIdx(visitIdx:Int) : VisitAddData? {
        val visitAddData:VisitAddData?
        return try {
            val querySnapshot = collectionReference.whereEqualTo("visitIdx", visitIdx).get().await()
            visitAddData = querySnapshot.documents.firstOrNull()?.toObject(VisitAddData::class.java)
            visitAddData
        }catch (e:Exception){
            Log.e("Firebase Error", "Error getVisitAddDataByIdx: ${e.message}")
            null
        }
    }

    // 해당 유저의 전시실 방문 신청 목록을 모두 가져와 반환한다.
    suspend fun getVisitAddList(userIdx: Int): List<VisitAddData> {
        return try {
            // 전시실 방문 신청 목록을 가져온다.
            val querySnapshot = collectionReference.whereEqualTo("userIdx", userIdx).get().await()
            // 전시실 방문 신청 목록을 담은 리스트
            querySnapshot.toObjects<VisitAddData>()
        }catch (e:Exception){
            Log.e("Firebase Error", "Error getVisitAddList: ${e.message}")
            emptyList()
        }
    }

    // 전시실 방문 신청 정보를 수정하는 메서드
    suspend fun updateVisitAddData(visitAddData: VisitAddData):Boolean{
        val map = visitAddData.instanceToMap()
        return try {
            // 컬렉션이 가지고 있는 문서들 중에 수정할 작가 정보를 가져온다.
            val query = collectionReference.whereEqualTo("visitIdx", visitAddData.visitIdx).get().await()
            // 저장한다.
            query.documents.first().reference.update(map)
            true
        }catch (e:Exception){
            Log.e("Firebase Error", "Error updateVisitAddData: ${e.message}")
            false
        }
    }

    // 전시실 방문 신청 승인 상태 변경
    suspend fun updateVisitState(state: String, visitIdx: Int):Boolean{
        val map = mapOf(
            "visitState" to state
        )
        return try{
            // 컬렉션이 가지고 있는 문서들 중에 수정할 작가 정보를 가져온다.
            val query = collectionReference.whereEqualTo("visitIdx", visitIdx).get().await()
            // 변경한 상태값을 저장한다.
            query.documents.first().reference.update(map)
            true
        }catch (e:Exception){
            Log.e("Firebase Error", "Error updateVisitState: ${e.message}")
            false
        }
    }
}