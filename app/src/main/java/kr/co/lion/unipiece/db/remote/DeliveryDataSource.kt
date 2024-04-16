package kr.co.lion.unipiece.db.remote

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kr.co.lion.unipiece.model.DeliveryData

class DeliveryDataSource {

    private val deliveryStore = Firebase.firestore.collection("Delivery")

    //    // 배송지 시퀀스값을 가져온다.
//    suspend fun getDeliverySequence(): Int {
//
//        var deliverySequence = -1
//
//        val job1 = CoroutineScope(Dispatchers.IO).launch {
//            // 컬렉션에 접근할 수 있는 객체를 가져온다.
//            val collectionReference = db.collection("Sequence")
//            // 사용자 번호 시퀀스값을 가지고 있는 문서에 접근할 수 있는 객체를 가져온다.
//            val documentReference = collectionReference.document("DeliverySequence")
//            // 문서내에 있는 데이터를 가져올 수 있는 객체를 가져온다.
//            val documentSnapShot = documentReference.get().await()
//
//            deliverySequence = documentSnapShot.getLong("value")?.toInt() ?: -1
//        }
//        job1.join()
//
//        return deliverySequence
//    }
//
//    // 배송지 시퀀스 값을 업데이트 한다.
//    suspend fun updateDeliverySequence(authorSequence: Int) {
//        val job1 = CoroutineScope(Dispatchers.IO).launch {
//            // 컬렉션에 접근할 수 있는 객체를 가져온다.
//            val collectionReference = db.collection("Sequence")
//            // 작가 번호 시퀀스값을 가지고 있는 문서에 접근할 수 있는 객체를 가져온다.
//            val documentReference = collectionReference.document("DeliverySequence")
//            // 저장할 데이터를 담을 HashMap을 만들어준다.
//            val map = mutableMapOf<String, Long>()
//            // "value"라는 이름의 필드가 있다면 값이 덮어씌워지고 필드가 없다면 필드가 새로 생성된다.
//            map["value"] = authorSequence.toLong()
//            // 저장한다.
//            documentReference.set(map)
//        }
//        job1.join()
//    }
//
    // 신규 배송지 등록
    suspend fun insertDeliveryData(deliveryData: DeliveryData): Boolean {
        return try {
            val deliveryId = deliveryStore.document().id

            val deliveryDataMap = hashMapOf(
                "deliveryName" to deliveryData.deliveryName,
                "deliveryAddress" to deliveryData.deliveryAddress,
                "deliveryPhone" to deliveryData.deliveryPhone,
                "deliveryNickName" to deliveryData.deliveryNickName,
                "deliveryMemo" to deliveryData.deliveryMemo,
                "basicDelivery" to deliveryData.basicDelivery,
                "deliveryIdx" to deliveryData.deliveryIdx,
                "userIdx" to deliveryData.userIdx,
            )

            // 작품 정보 저장
            deliveryStore
                .document(deliveryId)
                .set(deliveryDataMap)
                .await()

            true
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error insertDeliveryData : ${e.message}")
            return false
        }
    }

    // UserIdx 를 통해 배송지 정보를 가져와 반환한다
    suspend fun getDeliveryDataByIdx(userIdx: Int): List<DeliveryData> {

        return try {
            val query = deliveryStore.whereEqualTo("userIdx", userIdx)
            val querySnapshot = query.get().await()
            querySnapshot.map { it.toObject(DeliveryData::class.java) }

        } catch (e: Exception) {
            Log.e("Firebase Error", "Error getDeliveryDataByIdx : ${e.message}")
            emptyList()
        }
    }

//        var userDeliveryData: DeliveryData? = null
//
//        val job1 = CoroutineScope(Dispatchers.IO).launch {
//            // Delivery 컬렉션 접근 객체를 가져온다.
//            val collectionReference = db.collection("Delivery")
//            // userIdx 필드가 매개변수로 들어오는 userIdx와 같은 문서들을 가져온다.
//            val querySnapshot = collectionReference.whereEqualTo("userIdx", userIdx).get().await()
//            // 가져온 문서객체들이 들어 있는 리스트에서 첫 번째 객체를 추출한다.
//            // 회원 번호가 동일한 사용자는 없기 때문에 무조건 하나만 나오기 때문이다
//            userDeliveryData = querySnapshot.documents[0].toObject(DeliveryData::class.java)
//        }
//        job1.join()
//        return userDeliveryData
}

//    // 모든 배송지 정보를 가져온다.
//    suspend fun getDeliveryAll(): MutableList<DeliveryData> {
//        // 사용자 정보를 담을 리스트
//        val deliveryList = mutableListOf<DeliveryData>()
//
//        val job1 = CoroutineScope(Dispatchers.IO).launch {
//            // 모든 사용자 정보를 가져온다.
//            val querySnapshot = db.collection("Delivery").get().await()
//            // 가져온 문서의 수 만큼 반복한다.
//            querySnapshot.forEach {
//                // UserModel 객체에 담는다.
//                val deliveryData = it.toObject(DeliveryData::class.java)
//                // 리스트에 담는다.
//                deliveryList.add(deliveryData)
//            }
//        }
//        job1.join()
//
//        return deliveryList
//    }
//
//    // 배송지 정보를 수정하는 메서드
//    suspend fun updateDeliveryData(deliveryData: DeliveryData) {
//        val job1 = CoroutineScope(Dispatchers.IO).launch {
//            // 컬렉션에 접근할 수 있는 객체를 가져온다.
//            val collectionReference = db.collection("Delivery")
//
//            // 컬렉션이 가지고 있는 문서들 중에 수정할 작가 정보를 가져온다.
//            val query =
//                collectionReference.whereEqualTo("deliveryIdx", deliveryData.deliveryIdx).get()
//                    .await()
//
//            // 저장할 데이터를 담을 HashMap을 만들어준다.
//            val map = mutableMapOf<String, Any?>()
//            // 받는 이
//            map["deliveryName"] = deliveryData.deliveryName
//            // 연락처
//            map["deliveryPhone"] = deliveryData.deliveryPhone
//            // 배송지 명(별명)
//            map["deliveryNicName"] = deliveryData.deliveryNickName
//            // 주소
//            map["deliveryAddress"] = deliveryData.deliveryAddress
//            // 배송 메모
//            map["deliveryMemo"] = deliveryData.deliveryMemo
//            // 기본배송지 여부
//            map["basicDelivery"] = deliveryData.basicDelivery
//
//            // 저장한다.
//            // 가져온 문서 중 첫 번째 문서에 접근하여 데이터를 수정한다.
//            query.documents[0].reference.update(map)
//        }
//        job1.join()
//    }
//}