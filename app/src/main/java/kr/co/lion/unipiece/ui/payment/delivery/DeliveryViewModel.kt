package kr.co.lion.unipiece.ui.payment.delivery

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.model.DeliveryData
import kr.co.lion.unipiece.model.PieceAddInfoData
import kr.co.lion.unipiece.repository.DeliveryRepository

class DeliveryViewModel : ViewModel() {

    private val deliveryRepository = DeliveryRepository()

    // 배송지 정보 가져오기
    private val _deliveryDataList = MutableLiveData<List<DeliveryData>>()
    val deliveryDataList: LiveData<List<DeliveryData>> = _deliveryDataList

    // 신규 배송지 등록하기
    private val _insertDeliveryDataResult = MutableLiveData<Boolean>()
    val insertDeliveryDataResult: LiveData<Boolean> = _insertDeliveryDataResult

    val userIdx = UniPieceApplication.prefs.getUserIdx("userIdx",0)

    init{
        viewModelScope.launch {
            getDeliveryDataByIdx(userIdx)
        }
    }

    // 배송지 정보 불러오기
    suspend fun getDeliveryDataByIdx(userIdx: Int) {

        val response = deliveryRepository.getDeliveryDataByIdx(userIdx)
        Log.d("test1234","${response}")
        _deliveryDataList.value = response
    }

    // 신규 배송지 등록하기
    suspend fun insertDeliveryData(deliveryData: DeliveryData) {
        try {
            deliveryRepository.insertDeliveryData(deliveryData)
            _insertDeliveryDataResult.value = true
        }catch (e:Exception){
            _insertDeliveryDataResult.value = false
        }

    }

//    // 신규 배송지 등록하기
//    suspend fun insertDeliveryData(deliveryData: DeliveryData){
//        val job1 = viewModelScope.launch{
//            deliveryRepository.insertDeliveryData(deliveryData)
//            // 등록 후, 필요하다면 UI 업데이트를 위한 추가 작업 수행
//            // 예: 새로운 배송지 정보를 _deliveryData에 설정
//            _deliveryData.value = deliveryData
//        }
//        job1.join()
//    }
}