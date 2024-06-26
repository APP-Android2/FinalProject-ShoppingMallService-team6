package kr.co.lion.unipiece.ui.buy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.model.AuthorInfoData
import kr.co.lion.unipiece.model.AuthorReviewData
import kr.co.lion.unipiece.model.CartData
import kr.co.lion.unipiece.model.PieceInfoData
import kr.co.lion.unipiece.repository.AuthorInfoRepository
import kr.co.lion.unipiece.repository.AuthorReviewRepository
import kr.co.lion.unipiece.repository.CartRepository
import kr.co.lion.unipiece.repository.LikePieceRepository
import kr.co.lion.unipiece.repository.PieceInfoRepository

class BuyDetailViewModel(private val pieceIdx: Int, private val authorIdx: Int, private val userIdx: Int): ViewModel() {

    private val pieceInfoRepository = PieceInfoRepository()

    private val authorInfoRepository = AuthorInfoRepository()

    private val authorReviewRepository = AuthorReviewRepository()

    private val likePieceInfoRepository = LikePieceRepository()

    private val cartRepository = CartRepository()

    private val _pieceInfo = MutableLiveData<PieceInfoData?>()
    val pieceInfo : LiveData<PieceInfoData?> = _pieceInfo

    private val _authorInfo = MutableLiveData<AuthorInfoData?>()
    val authorInfo : LiveData<AuthorInfoData?> = _authorInfo

    private val _authorReviewList = MutableLiveData<List<AuthorReviewData>>()
    val authorReviewList : LiveData<List<AuthorReviewData>> = _authorReviewList

    private val _pieceInfoReceived = MutableLiveData<Boolean>()
    private val _authorInfoReceived = MutableLiveData<Boolean>()
    private val _authorReviewReceived = MutableLiveData<Boolean>()

    private val _allDataReceived = MediatorLiveData<Boolean>()
    val allDataReceived: LiveData<Boolean> = _allDataReceived

    private val _likePiece = MutableLiveData<Boolean>()
    val likePiece : LiveData<Boolean> = _likePiece

    private val _likePieceCount = MutableLiveData<Int>()
    val likePieceCount : LiveData<Int> = _likePieceCount

    private val _cartPiece = MutableLiveData<Boolean>()
    val cartPiece : LiveData<Boolean> = _cartPiece

    init {
        viewModelScope.launch {
            _allDataReceived.addSource(_pieceInfoReceived) { checkAllDataReceived() }
            _allDataReceived.addSource(_authorInfoReceived) { checkAllDataReceived() }
            _allDataReceived.addSource(_authorReviewReceived) { checkAllDataReceived() }
        }
    }

    suspend fun getCartPiece(){
        val response = cartRepository.isCartPiece(pieceIdx, userIdx)

        _cartPiece.value = response
    }
    suspend fun insertCartData(cartData: CartData){
        cartRepository.insertCartData(cartData)
    }

    suspend fun cancelCartPiece(){
        cartRepository.cancelCartPiece(pieceIdx, userIdx)
    }

    suspend fun updateLike(){
        val countLike = likePieceInfoRepository.countLikePiece(pieceIdx)
        pieceInfoRepository.updatePieceLike(pieceIdx, countLike)

        _likePieceCount.value = countLike
    }

    suspend fun getLikePiece() {
        val response = likePieceInfoRepository.isLikePiece(pieceIdx, userIdx)

        _likePiece.value = response
    }

    suspend fun addLikePiece(){
        likePieceInfoRepository.addLikePiece(pieceIdx, userIdx)
    }

    suspend fun cancelLikePiece(){
        likePieceInfoRepository.cancelLikePiece(pieceIdx, userIdx)
    }

    private fun checkAllDataReceived() {
        val getAllDataReceived = _pieceInfoReceived.value == true &&
                _authorInfoReceived.value == true &&
                _authorReviewReceived.value == true
        _allDataReceived.value = getAllDataReceived
    }

    fun setPieceInfoReceived(received: Boolean) {
        _pieceInfoReceived.value = received
    }

    fun setAuthorInfoReceived(received: Boolean) {
        _authorInfoReceived.value = received
    }

    fun setAuthorReviewReceived(received: Boolean) {
        _authorReviewReceived.value = received
    }

    suspend fun getIdxPieceInfo() {
        val response = pieceInfoRepository.getIdxPieceInfo(pieceIdx)

        val updatedResponse = response?.let {
            val pieceImgUrl = getPieceImg(it.pieceIdx.toString(), it.pieceImg)
            it.copy(pieceImg = pieceImgUrl ?: it.pieceImg)
        }

        _pieceInfo.value = updatedResponse
    }

    suspend fun getIdxAuthorInfo() {
        val response = authorInfoRepository.getAuthorInfoDataByIdx(authorIdx)

        val updatedResponse = response?.let {
            val authorImgUrl = getAuthorImg(it.authorIdx)
            it.copy(authorImg = authorImgUrl ?: it.authorImg)
        }

        _authorInfo.value = updatedResponse
    }

    suspend fun getAuthorReviewDataByIdx(){
        val response = authorReviewRepository.getAuthorReviewDataByIdx(authorIdx)
        _authorReviewList.value = response
    }

    private suspend fun getPieceImg(pieceIdx: String, pieceImg: String): String? {
        return pieceInfoRepository.getPieceInfoImg(pieceIdx, pieceImg)
    }

    private suspend fun getAuthorImg(authorIdx: Int): String? {
        return authorInfoRepository.getAuthorInfoImg(authorIdx)
    }
}