package kr.co.lion.unipiece.db.remote

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.co.lion.unipiece.model.DeliveryData

class DeliveryDataSource {

    private val deliveryStore = Firebase.firestore.collection("Delivery")
    private val sequenceStore = Firebase.firestore.collection("Sequence")

    // 배송지 번호 시퀀스값을 가져온다.
    suspend fun getDeliverySequence(): Int {
        try {

            var deliverySequence = -1

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 사용자 번호 시퀀스값을 가지고 있는 문서에 접근할 수 있는 객체를 가져온다.
                val documentReference = sequenceStore.document("DeliverySequence")
                // 문서내에 있는 데이터를 가져올 수 있는 객체를 가져온다.
                val documentSnapShot = documentReference.get().await()

                deliverySequence = documentSnapShot.getLong("value")?.toInt() ?: -1
            }
            job1.join()

            return deliverySequence
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error dbGetDeliverySequence : ${e.message}")
            return 0
        }
    }

    // 배송지 시퀀스 값을 업데이트 한다.
    suspend fun updateDeliverySequence(deliverySequence: Int) {
        try {

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 배송지 시퀀스 값을 가지고 있는 문서에 접근할 수 있는 객체를 가져온다.
                val documentReference = sequenceStore.document("DeliverySequence")
                // 저장할 데이터를 담을 HashMap을 만들어준다.
                val map = mutableMapOf<String, Long>()
                // "value"라는 이름의 필드가 있다면 값이 덮어씌워지고 필드가 없다면 필드가 새로 생성된다.
                map["value"] = deliverySequence.toLong()
                // 저장한다.
                documentReference.set(map)
            }
            job1.join()
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error dbUpdateDeliverySequence : ${e.message}")
        }
    }

    // 기존 배송지를 수정한다.
    suspend fun updateDeliveryData(deliveryData: DeliveryData) {

        try {

            // 기본 배송지로 설정하는 경우
            if (deliveryData.basicDelivery) {
                // 모든 기존 배송지의 basicDelivery를 false로 설정
                val allDeliveryDocuments = deliveryStore.get().await()
                allDeliveryDocuments.forEach { document ->
                    if (document.getBoolean("basicDelivery") == true) {
                        document.reference.update("basicDelivery", false)
                    }
                }
            }
            // deliveryIdx를 사용하여 해당 배송지 정보를 찾는다.
            val query = deliveryStore.whereEqualTo("deliveryIdx", deliveryData.deliveryIdx)
            val querySnapshot = query.get().await()

            // 해당하는 모든 문서에 대하여 업데이트를 진행한다.
            // 일반적으로 deliveryIdx는 유일한 값이므로, 하나의 문서만 업데이트 될 것이다.
            // 그러나 혹시 모르는 상황에 대비해 forEach를 사용한다.
            querySnapshot.forEach { document ->
                val deliveryDataMap = hashMapOf(
                    "deliveryName" to deliveryData.deliveryName,
                    "deliveryAddress" to deliveryData.deliveryAddress,
                    "deliveryAddressDetail" to deliveryData.deliveryAddressDetail,
                    "deliveryPhone" to deliveryData.deliveryPhone,
                    "deliveryNickName" to deliveryData.deliveryNickName,
                    "deliveryMemo" to deliveryData.deliveryMemo,
                    "basicDelivery" to deliveryData.basicDelivery,
                    "deliveryIdx" to deliveryData.deliveryIdx,
                    "userIdx" to deliveryData.userIdx
                )
                document.reference.update(deliveryDataMap as Map<String, Any>).await()
            }
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error dbUpdateDeliveryData : ${e.message}")
        }
    }

    // 신규 배송지를 등록한다.
    suspend fun insertDeliveryData(deliveryData: DeliveryData) {
        try {
            val deliveryId = deliveryStore.document().id

            // 기본 배송지로 설정하는 경우
            if (deliveryData.basicDelivery) {
                // 모든 기존 배송지의 basicDelivery를 false로 설정
                val allDeliveryDocuments = deliveryStore.get().await()
                allDeliveryDocuments.forEach { document ->
                    if (document.getBoolean("basicDelivery") == true) {
                        document.reference.update("basicDelivery", false)
                    }
                }
            }
            val deliveryDataMap = hashMapOf(
                "deliveryName" to deliveryData.deliveryName,
                "deliveryAddress" to deliveryData.deliveryAddress,
                "deliveryAddressDetail" to deliveryData.deliveryAddressDetail,
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
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error dbInsertDeliveryData : ${e.message}")
        }
    }

    // UserIdx 를 통해 배송지 정보를 가져와 반환한다. (배송지 관리 접근 시)
    suspend fun getDeliveryDataByUserIdx(userIdx: Int): List<DeliveryData> {

        return try {
            val query = deliveryStore.whereEqualTo("userIdx", userIdx)
            val querySnapshot = query.get().await()
            querySnapshot.map { it.toObject(DeliveryData::class.java) }

        } catch (e: Exception) {
            Log.e("Firebase Error", "Error dbGetDeliveryDataByIdx : ${e.message}")
            emptyList()
        }
    }

    // deliveryIdx 를 통해 해당 배송지 정보를 삭제한다.
    suspend fun deleteDeliveryData(deliveryIdx: Int): Int {
        return try {
            val query = deliveryStore.whereEqualTo("deliveryIdx", deliveryIdx)
            val querySnapshot = query.get().await()

            val result = querySnapshot.documents[0].reference.delete().toString().toInt()
            result
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error dbDeleteDeliveryData : ${e.message}")
            0
        }
    }

    // userIdx 를 통해 해당 유저의 배송지 정보중에서 기본 배송지를 가져온다.
    suspend fun getBasicDeliveryData(userIdx: Int) : List<DeliveryData>{
        return try {
            val query = deliveryStore.whereEqualTo("userIdx", userIdx).whereEqualTo("basicDelivery",true)
            val querySnapshot = query.get().await()

            querySnapshot.map { it.toObject(DeliveryData::class.java) }
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error dbGetBasicDeliveryData : ${e.message}")
            emptyList()
        }
    }

    // deliveryIdx를 통해 해당 배송지 정보를 가져온다.
    suspend fun getDeliveryDataByDeliveryIdx(deliveryIdx: Int): List<DeliveryData> {

        return try {
            val query = deliveryStore.whereEqualTo("deliveryIdx", deliveryIdx)
            val querySnapshot = query.get().await()
            querySnapshot.map { it.toObject(DeliveryData::class.java) }

        } catch (e: Exception) {
            Log.e("Firebase Error", "Error dbGetDeliveryDataByDeliveryIdx : ${e.message}")
            emptyList()
        }

    }
}
