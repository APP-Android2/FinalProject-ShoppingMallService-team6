package kr.co.lion.unipiece.db.remote

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kr.co.lion.unipiece.model.DeliveryData

class DeliveryDataSource {

    private val deliveryStore = Firebase.firestore.collection("Delivery")

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
}
