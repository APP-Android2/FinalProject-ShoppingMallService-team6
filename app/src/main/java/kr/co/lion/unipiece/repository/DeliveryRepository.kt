package kr.co.lion.unipiece.repository

import kr.co.lion.unipiece.db.remote.DeliveryDataSource
import kr.co.lion.unipiece.model.DeliveryData

class DeliveryRepository {
    private val deliveryDataSource = DeliveryDataSource()

    // 배송지 시퀀스 값을 가져온다.
    suspend fun getDeliverySequence() = deliveryDataSource.getDeliverySequence()

    // 배송지 시퀀스 값을 업데이트한다.
    suspend fun updateDeliverySequence(deliverySequence: Int) = deliveryDataSource.updateDeliverySequence(deliverySequence)

    // 배송지 정보를 업데이트한다.
    suspend fun updateDeliveryData(deliveryData: DeliveryData) = deliveryDataSource.updateDeliveryData(deliveryData)

    // 배송지 정보를 저장한다.
    suspend fun insertDeliveryData(deliveryData: DeliveryData) = deliveryDataSource.insertDeliveryData(deliveryData)

    // 유저 번호를 통해 배송지 정보를 가져와 반환한다.
    suspend fun getDeliveryDataByIdx(userIdx:Int) = deliveryDataSource.getDeliveryDataByIdx(userIdx)

    // 배송지 번호를 통해 해당하는 배송지 정보를 삭제한다.
    suspend fun deleteDeliveryData(deliveryIdx:Int) = deliveryDataSource.deleteDeliveryData(deliveryIdx)

    // 유저 번호를 통해 해당 유저의 배송지정보 중에서 기본 배송지 정보를 가져온다.
    suspend fun getBasicDeliveryData(userIdx:Int) = deliveryDataSource.getBasicDeliveryData(userIdx)

}
