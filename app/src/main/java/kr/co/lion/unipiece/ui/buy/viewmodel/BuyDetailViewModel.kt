package kr.co.lion.unipiece.ui.buy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.model.PieceInfoData
import kr.co.lion.unipiece.repository.PieceInfoRepository

class BuyDetailViewModel(private val pieceIdx: Int): ViewModel() {

    private val pieceInfoRepository = PieceInfoRepository()

    private val _pieceInfo = MutableLiveData<PieceInfoData?>()
    val pieceInfoList : LiveData<PieceInfoData?> = _pieceInfo

    init {
        viewModelScope.launch {
            getIdxPieceInfo(pieceIdx)
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

    private suspend fun getPieceImg(pieceIdx: String, pieceImg: String): String? {
        return pieceInfoRepository.getPieceInfoImg(pieceIdx, pieceImg)
    }
}