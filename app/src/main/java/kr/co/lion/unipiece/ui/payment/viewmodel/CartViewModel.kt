package kr.co.lion.unipiece.ui.payment.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.co.lion.unipiece.model.CartData
import kr.co.lion.unipiece.model.PieceInfoData
import kr.co.lion.unipiece.repository.CartRepository
import kr.co.lion.unipiece.repository.PieceInfoRepository
import java.sql.Timestamp

class CartViewModel : ViewModel() {

    private val cartRepository = CartRepository()
    private val pieceInfoRepository = PieceInfoRepository()

    // 장바구니 작품 정보
    private val _cartPieceInfoList = MutableLiveData<PieceInfoData>()
    val cartPieceInfoList: LiveData<PieceInfoData> = _cartPieceInfoList

    // 작가 정보를 불러오기
    suspend fun getPieceInfoData(pieceIdx: Int) {
        val job1 = viewModelScope.launch {
            // 작품 정보 객체 셋팅

        }
        job1.join()
    }

}
