package kr.co.lion.unipiece.ui.payment.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.model.DeliveryData
import kr.co.lion.unipiece.repository.DeliveryRepository

class DeliveryViewModel : ViewModel() {

    private val deliveryRepository = DeliveryRepository()

    // 유저 Idx 찾기
    val userIdx = UniPieceApplication.prefs.getUserIdx("userIdx", 0)

    // userIdx로 가져온 배송지
    private val _userIdxDeliveryDataList = MutableLiveData<List<DeliveryData>>()
    val userIdxDeliveryDataList: LiveData<List<DeliveryData>> = _userIdxDeliveryDataList

    // deliveryIdx로 가져온 배송지
    private val _deliveryIdxDeliveryDataList = MutableLiveData<List<DeliveryData>>()
    val deliveryIdxDeliveryDataList: LiveData<List<DeliveryData>> = _deliveryIdxDeliveryDataList

    // 신규 배송지 등록하기
    private val _insertDeliveryData = MutableLiveData<List<DeliveryData>>()
    val insertDeliveryData: LiveData<List<DeliveryData>> = _insertDeliveryData

    // 배송지 삭제하기
    private val _deleteDeliveryData = MutableLiveData<Int>()
    val deleteDeliveryData: LiveData<Int> = _deleteDeliveryData

    // 기본 배송지
    private val _getBasicDeliveryData = MutableLiveData<List<DeliveryData>>()
    val getBasicDeliveryData: LiveData<List<DeliveryData>> = _getBasicDeliveryData

    // 신규 데이터 등록시 로딩
    private val _insertDataLoading = MutableLiveData<Boolean?>(null)
    val insertDataLoading : LiveData<Boolean?> = _insertDataLoading

    // 데이터 삭제시 로딩
    private val _deleteDataLoading = MutableLiveData<Boolean?>(null)
    val deleteDataLoading : LiveData<Boolean?> = _deleteDataLoading

    // deliveryIdx로 데이터 불러올 시 로딩
    private val _getDeliveryIdxDataLoading = MutableLiveData<Boolean?>(null)
    val getDeliveryIdxDataLoading : LiveData<Boolean?> = _getDeliveryIdxDataLoading


    init {
        viewModelScope.launch {
            getDeliveryDataByUserIdx(userIdx)
            getBasicDeliveryData(userIdx)
        }
    }

    fun setInsertData(){
        _insertDataLoading.value = null
    }
    fun setDeleteData(){
        _deleteDataLoading.value = null
    }
    fun setGetDeliverIdxData(){
        _getDeliveryIdxDataLoading.value = null
    }


    // userIdx로 가져온 배송지 정보 담기
    fun getDeliveryDataByUserIdx(userIdx: Int) = viewModelScope.launch {
        try {
            val response = deliveryRepository.getDeliveryDataByUserIdx(userIdx)

            _userIdxDeliveryDataList.value = response
            Log.d("테스트 vm1","${_userIdxDeliveryDataList.value.toString()}")
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error vmGetDeliveryDataByUserIdx : ${e.message}")
        }

    }

    // deliveryIdx로 가져온 배송지 정보 담기
    fun getDeliveryDataByDeliveryIdx(deliveryIdx: Int) = viewModelScope.launch {
        try {
            val response = deliveryRepository.getDeliveryDataByDeliveryIdx(deliveryIdx)

            _deliveryIdxDeliveryDataList.value = response
            _getDeliveryIdxDataLoading.value = true
            Log.d("테스트 vm2","${_deliveryIdxDeliveryDataList.value.toString()}")
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error vmGetDeliveryDataByDeliveryIdx : ${e.message}")
        }

    }

    // 신규 배송지 등록 및 수정하기 분기
    fun insertDeliveryData(deliveryDataList: DeliveryData) = viewModelScope.launch {
        try {
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
                deliveryRepository.insertDeliveryData(sqDeliveryData)
                _insertDataLoading.value = true
            } else {
                deliveryRepository.updateDeliveryData(deliveryDataList)
                _insertDataLoading.value = true
            }
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error vmInsertDeliveryData : ${e.message}")
        }

    }



    // 삭제할 데이터 담기
    fun deleteDeliveryData(deliveryIdx: Int) = viewModelScope.launch {
        try {
            val response = deliveryRepository.deleteDeliveryData(deliveryIdx)
            _deleteDeliveryData.value = response
            _deleteDataLoading.value = true
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error vmDeleteDeliveryData : ${e.message}")
        }

    }

    // 가져온 기본 배송지 데이터 담기
    fun getBasicDeliveryData(userIdx: Int) = viewModelScope.launch {
        try {
            val response = deliveryRepository.getBasicDeliveryData(userIdx)
            _getBasicDeliveryData.value = response
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error vmGetBasicDeliveryData : ${e.message}")
        }
    }


}
