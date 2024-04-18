package kr.co.lion.unipiece.ui.payment.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.model.DeliveryData
import kr.co.lion.unipiece.model.PieceAddInfoData
import kr.co.lion.unipiece.model.UserInfoData
import kr.co.lion.unipiece.repository.DeliveryRepository

class DeliveryViewModel : ViewModel() {

    private val deliveryRepository = DeliveryRepository()

    // 배송지 정보 가져오기
    private val _deliveryDataList = MutableLiveData<List<DeliveryData>>()
    val deliveryDataList: LiveData<List<DeliveryData>> = _deliveryDataList

    // 신규 배송지 등록하기
    private val _insertDeliveryData = MutableLiveData<List<DeliveryData>>()
    val insertDeliveryData: LiveData<List<DeliveryData>> = _insertDeliveryData

    val userIdx = UniPieceApplication.prefs.getUserIdx("userIdx", 0)

    init {
        viewModelScope.launch {
            getDeliveryDataByIdx(userIdx)
        }
    }

    // 배송지 정보 불러오기
    suspend fun getDeliveryDataByIdx(userIdx: Int) {
        try {
            val response = deliveryRepository.getDeliveryDataByIdx(userIdx)

            _deliveryDataList.value = response
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error insertDeliveryData : ${e.message}")
            return
        }

    }

    // 신규 배송지 등록 및 수정하기
    suspend fun insertDeliveryData(
        deliveryDataList: DeliveryData
    ) {
        try {
            Log.d("deliveryDataList1","${deliveryDataList}")
            // 신규 배송지 등록일 경우
            if (deliveryDataList.deliveryIdx == 0) {
                // 배송지 시퀀스 값 가져오기
                val deliverySequence = deliveryRepository.getDeliverySequence()
                // 배송지 시퀀스 값 업데이트
                deliveryRepository.updateDeliverySequence(deliverySequence + 1)
                // 배송지 시퀀스 값으로 deliveryIdx 세팅
                val deliveryIdx = deliverySequence + 1
                // 신규 배송지 등록 데이터
                val sqDeliveryData = DeliveryData(
                    deliveryDataList.deliveryName,
                    deliveryDataList.deliveryPhone,
                    deliveryDataList.deliveryNickName,
                    deliveryDataList.deliveryAddress,
                    deliveryDataList.deliveryAddressDetail,
                    deliveryDataList.deliveryMemo,
                    deliveryDataList.basicDelivery,
                    deliveryDataList.userIdx,
                    deliveryIdx
                )
                Log.d("deliveryData2","${sqDeliveryData}")
                deliveryRepository.insertDeliveryData(sqDeliveryData)
            } else {
                Log.d("deliveryData3","${deliveryDataList}")
                deliveryRepository.updateDeliveryData(deliveryDataList)
            }
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error insertDeliveryData : ${e.message}")
        }

    }
}
