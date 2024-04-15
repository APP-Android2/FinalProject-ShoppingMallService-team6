package kr.co.lion.unipiece.ui.payment.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.co.lion.unipiece.model.CartData
import java.sql.Timestamp

class CartViewModel : ViewModel() {

    // 유저 인덱스
    val userIdx = MutableLiveData<Int>()
    // 작품 인덱스
    val pieceIdx = MutableLiveData<Int>()
    // 등록일자
    val cartPieceDate = MutableLiveData<Timestamp>()

}