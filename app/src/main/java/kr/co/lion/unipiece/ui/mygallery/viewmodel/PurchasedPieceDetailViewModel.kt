package kr.co.lion.unipiece.ui.mygallery.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.model.PieceBuyInfoData
import kr.co.lion.unipiece.model.PieceInfoData
import kr.co.lion.unipiece.repository.PieceBuyInfoRepository
import kr.co.lion.unipiece.repository.PieceInfoRepository

class PurchasedPieceDetailViewModel : ViewModel() {
    private val pieceBuyInfoRepository = PieceBuyInfoRepository()
    private val pieceInfoRepository = PieceInfoRepository()

    private val _pieceBuyInfoData = MutableLiveData<Pair<PieceBuyInfoData?, PieceInfoData?>>()
    val pieceBuyInfoData : LiveData<Pair<PieceBuyInfoData?, PieceInfoData?>> = _pieceBuyInfoData

    private val _pieceBuyState = MutableLiveData<String>()
    val pieceBuyState: LiveData<String> = _pieceBuyState

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    suspend fun getPieceBuyInfoByPieceBuyIdx(pieceIdx: Int, pieceBuyIdx: Int) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val pieceBuyInfoData = pieceBuyInfoRepository.getPieceBuyInfoByPieceBuyIdx(pieceBuyIdx)
                val pieceInfoData = pieceInfoRepository.getIdxPieceInfo(pieceIdx)
                val resultData: Pair<PieceBuyInfoData?, PieceInfoData?> = Pair(pieceBuyInfoData, pieceInfoData)

                _pieceBuyInfoData.value = resultData

                _pieceBuyState.value = resultData.first?.pieceBuyState

                _isLoading.value = false

                updateUIWithImage(resultData)
            } catch (e: Exception) {
                Log.e("PurchasedPieceDetailViewModel", "Error loading piece buy info: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun updateUIWithImage(buyInfoWithPieceInfo: Pair<PieceBuyInfoData?, PieceInfoData?>) {
        val pieceBuyInfo = buyInfoWithPieceInfo.first
        val pieceInfoData = buyInfoWithPieceInfo.second

        val pieceImgUrl = pieceInfoData?.pieceImg

        if (pieceImgUrl != null) {
            val pieceIdx = pieceInfoData.pieceIdx.toString()
            val imgUrl = getPieceAddInfoImage(pieceIdx, pieceImgUrl)
            if (imgUrl != null) {
                val updatedPieceInfo = pieceInfoData.copy(pieceImg = imgUrl)
                val updatedData: Pair<PieceBuyInfoData?, PieceInfoData?> = Pair(pieceBuyInfo, updatedPieceInfo)
                _pieceBuyInfoData.postValue(updatedData)
            } else {
                _pieceBuyInfoData.postValue(buyInfoWithPieceInfo)
            }
        } else {
            _pieceBuyInfoData.postValue(buyInfoWithPieceInfo)
        }
    }

    private suspend fun getPieceAddInfoImage(pieceIdx: String, pieceImg: String): String? {
        return pieceInfoRepository.getPieceInfoImg(pieceIdx, pieceImg)
    }
}