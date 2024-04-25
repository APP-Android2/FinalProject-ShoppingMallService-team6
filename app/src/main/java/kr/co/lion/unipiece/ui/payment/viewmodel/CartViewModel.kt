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
import kr.co.lion.unipiece.model.CartData
import kr.co.lion.unipiece.model.CartInfoData
import kr.co.lion.unipiece.model.DeliveryData
import kr.co.lion.unipiece.model.PieceInfoData
import kr.co.lion.unipiece.repository.CartRepository
import kr.co.lion.unipiece.repository.PieceInfoRepository
import java.sql.Timestamp

class CartViewModel : ViewModel() {

    private val cartRepository = CartRepository()
    private val pieceInfoRepository = PieceInfoRepository()

    // 유저 Idx 찾기
    val userIdx = UniPieceApplication.prefs.getUserIdx("userIdx", 0)

    // userIdx로 가져온 장바구니에 담긴 작품 정보
    private val _getCartDataByUserIdxList = MutableLiveData<List<PieceInfoData>>()
    val getCartDataByUserIdxList: LiveData<List<PieceInfoData>> = _getCartDataByUserIdxList

    private val _cartInfoData = MutableLiveData<List<CartInfoData>>()
    val cartInfoData: LiveData<List<CartInfoData>> = _cartInfoData

    /*// 장바구니 데이터 로딩
    private val _getCartDataByUserIdxLoading = MutableLiveData<Boolean?>(null)
    val getCartDataByUserIdxLoading : LiveData<Boolean?> = _getCartDataByUserIdxLoading

    // 데이터 삭제시 로딩
    private val _deleteDataLoading = MutableLiveData<Boolean?>(null)
    val deleteDataLoading : LiveData<Boolean?> = _deleteDataLoading*/

    private val _loading = MutableLiveData<Boolean>(true)
    val loading: LiveData<Boolean> = _loading


    /*fun setDataLoading() {
        _getCartDataByUserIdxLoading.value = null
    }

    fun setDeleteData(){
        _deleteDataLoading.value = null
    }*/

    init {
        viewModelScope.launch {
            getCartDataByUserIdx(userIdx)
        }
    }

    fun setLoading(loading: Boolean) = viewModelScope.launch {
        _loading.value = loading
    }


    // 가져온 userIdx로 장바구니 데이터 불러오기
    fun getCartDataByUserIdx(userIdx: Int) = viewModelScope.launch {
        try {
            val response = cartRepository.getCartDataByUserIdx(userIdx)
            val updatedPieceInfoList = updateImagePieceInfo(response)
            val list = mutableListOf<CartInfoData>()

            updatedPieceInfoList.map {
                list.add(CartInfoData(it, false))
            }

            /*_getCartDataByUserIdxList.value = updatedPieceInfoList
            _getCartDataByUserIdxLoading.value = true*/

            _cartInfoData.value = list
            _loading.value = false

            Log.d("테스트 vm2","${_getCartDataByUserIdxList.value.toString()}")
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error vmGetDeliveryDataByDeliveryIdx : ${e.message}")
        }
    }

    suspend fun updateImagePieceInfo(pieceInfoList: List<PieceInfoData>): List<PieceInfoData> {
        return pieceInfoList.map { pieceInfoData ->
            val pieceImgUrl = getPieceImg(pieceInfoData.pieceIdx.toString(), pieceInfoData.pieceImg)
            pieceInfoData.copy(pieceImg = pieceImgUrl ?: pieceInfoData.pieceImg)
        }
    }

    private suspend fun getPieceImg(pieceIdx: String, pieceImg: String): String? {
        return pieceInfoRepository.getPieceInfoImg(pieceIdx, pieceImg)
    }

    // 특정 사용자의 특정 작품을 장바구니에서 삭제하는 함수
    fun deleteCartDataByUserIdx(userIdx: Int, pieceIdx: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = try {
                cartRepository.deleteCartDataByUserIdx(userIdx, pieceIdx)
                _loading.value = false
                // _deleteDataLoading.value = true
                true
                } catch (e: Exception) {
                    Log.e("Firebase Error", "Error vmDeleteCartDataByUserIdx : ${e.message}")
                    false
                }

            callback(success)
        }
    }

}
