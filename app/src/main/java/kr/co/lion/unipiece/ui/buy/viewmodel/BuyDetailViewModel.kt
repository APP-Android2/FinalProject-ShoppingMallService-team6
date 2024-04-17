package kr.co.lion.unipiece.ui.buy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.model.AuthorInfoData
import kr.co.lion.unipiece.model.AuthorReviewData
import kr.co.lion.unipiece.model.PieceInfoData
import kr.co.lion.unipiece.repository.AuthorInfoRepository
import kr.co.lion.unipiece.repository.AuthorReviewRepository
import kr.co.lion.unipiece.repository.PieceInfoRepository

class BuyDetailViewModel(private val pieceIdx: Int, private val authorIdx: Int): ViewModel() {

    private val pieceInfoRepository = PieceInfoRepository()

    private val authorInfoRepository = AuthorInfoRepository()

    private val authorReviewRepository = AuthorReviewRepository()

    private val _pieceInfo = MutableLiveData<PieceInfoData?>()
    val pieceInfo : LiveData<PieceInfoData?> = _pieceInfo

    private val _authorInfo = MutableLiveData<AuthorInfoData?>()
    val authorInfo : LiveData<AuthorInfoData?> = _authorInfo

    private val _authorReviewList = MutableLiveData<List<AuthorReviewData>>()
    val authorReviewList : LiveData<List<AuthorReviewData>> = _authorReviewList

    init {
        viewModelScope.launch {
            getIdxPieceInfo(pieceIdx)
            getIdxAuthorInfo(authorIdx)
            getAuthorReviewDataByIdx(authorIdx)
        }
    }

    suspend fun getIdxPieceInfo(pieceIdx: Int) {
        val response = pieceInfoRepository.getIdxPieceInfo(pieceIdx)

        response?.pieceImg?.let {
            val pieceImgUrl = getPieceImg(response.pieceIdx.toString(), it)
            response.pieceImg = pieceImgUrl ?: it
        }

        _pieceInfo.value = response
    }

    suspend fun getIdxAuthorInfo(authorIdx: Int) {
        val response = authorInfoRepository.getAuthorInfoDataByIdx(authorIdx)

        response?.authorImg?.let {
            val authorImgUrl = getAuthorImg(response.authorIdx)
            response.authorImg = authorImgUrl ?: it
        }

        _authorInfo.value = response
    }

    suspend fun getAuthorReviewDataByIdx(authorIdx: Int){
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