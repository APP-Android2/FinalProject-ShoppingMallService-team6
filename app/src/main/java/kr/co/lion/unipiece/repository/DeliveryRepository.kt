package kr.co.lion.unipiece.repository

import kr.co.lion.unipiece.db.remote.DeliveryDataSource
import kr.co.lion.unipiece.model.DeliveryData

class DeliveryRepository {
    private val deliveryDataSource = DeliveryDataSource()

    // 배송지 정보를 저장한다.
    suspend fun insertDeliveryData(deliveryData: DeliveryData) = deliveryDataSource.insertDeliveryData(deliveryData)

    // 유저 번호를 통해 배송지 정보를 가져와 반환한다
    suspend fun getDeliveryDataByIdx(userIdx:Int) = deliveryDataSource.getDeliveryDataByIdx(userIdx)

}
