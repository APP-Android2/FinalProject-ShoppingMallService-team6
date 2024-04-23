package kr.co.lion.unipiece.ui.mygallery.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.model.PieceBuyInfoData
import kr.co.lion.unipiece.model.PieceInfoData
import kr.co.lion.unipiece.repository.PieceBuyInfoRepository
import kr.co.lion.unipiece.repository.PieceInfoRepository

class PurchasedPieceViewModel : ViewModel() {
    private val pieceBuyInfoRepository = PieceBuyInfoRepository()
    private val pieceInfoRepository = PieceInfoRepository()

    private val _pieceBuyInfoList = MutableLiveData<List<Pair<PieceBuyInfoData, PieceInfoData?>>>()
    val pieceBuyInfoList : LiveData<List<Pair<PieceBuyInfoData, PieceInfoData?>>> = _pieceBuyInfoList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            val userIdx = getUserIdxFromSharedPreferences()

            getPieceBuyInfo(userIdx)
        }
    }

    suspend fun getPieceBuyInfo(userIdx: Int) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val pieceBuyInfoList = pieceBuyInfoRepository.getPieceBuyInfo(userIdx)
                val buyInfoWithPieceInfo = mutableListOf<Pair<PieceBuyInfoData, PieceInfoData?>>()

                for (pieceBuyInfo in pieceBuyInfoList) {
                    val pieceIdx = pieceBuyInfo.pieceIdx
                    val pieceInfo = pieceInfoRepository.getIdxPieceInfo(pieceIdx)
                    buyInfoWithPieceInfo.add(Pair(pieceBuyInfo, pieceInfo))
                }

                _pieceBuyInfoList.value = buyInfoWithPieceInfo

                _isLoading.value = false

                updateUIWithImage(buyInfoWithPieceInfo)
            } catch (e: Exception) {
                Log.e("PurchasedPieceViewModel", "Error loading piece buy info: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun updateUIWithImage(buyInfoWithPieceInfo: List<Pair<PieceBuyInfoData, PieceInfoData?>>) {
        val updatedBuyInfoWithPieceInfo = mutableListOf<Pair<PieceBuyInfoData, PieceInfoData?>>()

        for ((pieceBuyInfo, pieceInfo) in buyInfoWithPieceInfo) {
            val pieceImgUrl = pieceInfo?.pieceImg

            if (pieceImgUrl != null) {
                val pieceIdx = pieceInfo.pieceIdx.toString()
                val imgUrl = getPieceAddInfoImage(pieceIdx, pieceImgUrl)
                if (imgUrl != null) {
                    val updatedPieceInfo = pieceInfo.copy(pieceImg = imgUrl)
                    updatedBuyInfoWithPieceInfo.add(Pair(pieceBuyInfo, updatedPieceInfo))
                } else {
                    updatedBuyInfoWithPieceInfo.add(Pair(pieceBuyInfo, pieceInfo))
                }
            } else {
                updatedBuyInfoWithPieceInfo.add(Pair(pieceBuyInfo, pieceInfo))
            }
        }

        _pieceBuyInfoList.postValue(updatedBuyInfoWithPieceInfo)
    }

    private suspend fun getPieceAddInfoImage(pieceIdx: String, pieceImg: String): String? {
        return pieceInfoRepository.getPieceInfoImg(pieceIdx, pieceImg)
    }

    private fun getUserIdxFromSharedPreferences(): Int {
        val sharedPrefs = UniPieceApplication.prefs
        return sharedPrefs.getUserIdx("userIdx", 0)
    }
}