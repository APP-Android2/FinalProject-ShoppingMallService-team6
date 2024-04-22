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



    // 장바구니 데이터 로딩
    private val _getCartDataByUserIdxLoading = MutableLiveData<Boolean?>(null)
    val getCartDataByUserIdxLoading : LiveData<Boolean?> = _getCartDataByUserIdxLoading

    fun setDataLoading() {
        _getCartDataByUserIdxLoading.value = null
    }

    init {
        viewModelScope.launch {
            getCartDataByUserIdx(userIdx)
        }
    }



    // 가져온 userIdx로 장바구니 데이터 불러오기
    fun getCartDataByUserIdx(userIdx: Int) = viewModelScope.launch {
        try {
            val response = cartRepository.getCartDataByUserIdx(userIdx)

            _getCartDataByUserIdxList.value = response
            _getCartDataByUserIdxLoading.value = true
            Log.d("테스트 vm2","${_getCartDataByUserIdxList.value.toString()}")
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error vmGetDeliveryDataByDeliveryIdx : ${e.message}")
        }
    }

}
