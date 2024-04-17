package kr.co.lion.unipiece.db.remote

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kr.co.lion.unipiece.model.DeliveryData

class DeliveryDataSource {

    private val deliveryStore = Firebase.firestore.collection("Delivery")

    // 신규 배송지 등록 및 수정
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

            // 신규 배송지 등록에서 저장한다면 (배송지 시퀀스 + 1 세팅)
            if (deliveryData.deliveryIdx == 0) {
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
            } else {
                // 배송지 수정에서 저장한다면 (해당 배송지 인덱스를 가진 문서에 접근해서 수정)

                val query = deliveryStore.whereEqualTo("deliveryIdx", deliveryData.deliveryIdx)
                Log.d("test1","$query")
                val querySnapshot = query.get().await()
                Log.d("test2","$querySnapshot")
                // 저장할 데이터를 담을 HashMap을 만들어준다.
                val map = mutableMapOf<String, Any?>()
                map["deliveryName"] = deliveryData.deliveryName
                map["deliveryAddress"] = deliveryData.deliveryAddress
                map["deliveryAddressDetail"] = deliveryData.deliveryAddressDetail
                map["deliveryPhone"] = deliveryData.deliveryPhone
                map["deliveryNickName"] = deliveryData.deliveryNickName
                map["deliveryMemo"] = deliveryData.deliveryMemo
                map["basicDelivery"] = deliveryData.basicDelivery
                map["userIdx"] = deliveryData.userIdx
                map["deliveryIdx"] = deliveryData.deliveryIdx

                // 해당 문서를 업데이트한다.
                querySnapshot.documents[0].reference.update(map).await()
            }


        } catch (e: Exception) {
            Log.e("Firebase Error", "Error insertDeliveryData : ${e.message}")
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
}
