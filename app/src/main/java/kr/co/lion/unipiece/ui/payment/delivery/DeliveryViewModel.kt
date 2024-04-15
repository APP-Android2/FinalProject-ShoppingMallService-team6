package kr.co.lion.unipiece.ui.payment.delivery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.model.DeliveryData
import kr.co.lion.unipiece.repository.DeliveryRepository

class DeliveryViewModel : ViewModel() {
    // 배송지 정보
    private val _deliveryData = MutableLiveData<DeliveryData>()
    val deliveryData: LiveData<DeliveryData> = _deliveryData

    private val deliveryRepository = DeliveryRepository()

    // 배송지 정보 불러오기
    suspend fun getDeliveryData(userIdx: Int){
        val job1 = viewModelScope.launch{
            _deliveryData.value = deliveryRepository.getDeliveryDataByIdx(userIdx)
        }
        job1.join()
    }

    // 신규 배송지 등록하기
    suspend fun insertDeliveryData(deliveryData: DeliveryData){
        val job1 = viewModelScope.launch{
            deliveryRepository.insertDeliveryData(deliveryData)
            // 등록 후, 필요하다면 UI 업데이트를 위한 추가 작업 수행
            // 예: 새로운 배송지 정보를 _deliveryData에 설정
            _deliveryData.value = deliveryData
        }
        job1.join()
    }
}