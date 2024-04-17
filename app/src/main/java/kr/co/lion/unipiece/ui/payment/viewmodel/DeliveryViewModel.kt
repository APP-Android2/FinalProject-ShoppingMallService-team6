package kr.co.lion.unipiece.ui.payment.viewmodel

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
    private val _insertDeliveryDataResult = MutableLiveData<List<DeliveryData>>()
    val insertDeliveryDataResult: LiveData<List<DeliveryData>> = _insertDeliveryDataResult

    val userIdx = UniPieceApplication.prefs.getUserIdx("userIdx",0)

    init{
        viewModelScope.launch {
            getDeliveryDataByIdx(userIdx)
        }
    }

    // 배송지 정보 불러오기
    suspend fun getDeliveryDataByIdx(userIdx: Int) {
        try {
            val response = deliveryRepository.getDeliveryDataByIdx(userIdx)

            _deliveryDataList.value = response
        }catch (e:Exception){
            Log.e("Firebase Error", "Error insertDeliveryData : ${e.message}")
            return
        }

    }

    // 신규 배송지 등록 및 수정하기
    suspend fun insertDeliveryData(deliveryData: DeliveryData) {
        try {
            deliveryRepository.insertDeliveryData(deliveryData)
        }catch (e:Exception){
            Log.e("Firebase Error", "Error insertDeliveryData : ${e.message}")
            return
        }

    }
    
}
