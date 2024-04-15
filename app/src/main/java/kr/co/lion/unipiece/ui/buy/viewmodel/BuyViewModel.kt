package kr.co.lion.unipiece.ui.buy.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.model.PieceInfoData
import kr.co.lion.unipiece.repository.PieceInfoRepository
import java.net.URI

class BuyViewModel(): ViewModel() {

    private val pieceInfoRepository = PieceInfoRepository()

    private var _popPieceInfoList = MutableLiveData<List<PieceInfoData>>()
    val popPieceInfoList : LiveData<List<PieceInfoData>> = _popPieceInfoList

    init {
        viewModelScope.launch {
            getPopPieceInfo()
        }
    }

    private suspend fun getPopPieceInfo(){
        val response = pieceInfoRepository.getPopPieceInfo()
        val pieceInfoList = mutableListOf<PieceInfoData>()

        response.forEach { pieceInfoData ->
            val pieceImgUrl = getPopPieceImg(pieceInfoData.pieceIdx.toString(), pieceInfoData.pieceImg)?.toString()
            pieceInfoData.pieceImg = pieceImgUrl ?: pieceInfoData.pieceImg
            Log.d("Firebase response", pieceInfoData.toString())
            pieceInfoList.add(pieceInfoData)
        }

        _popPieceInfoList.value = pieceInfoList
    }

    private suspend fun getPopPieceImg(pieceIdx: String, pieceImg: String): URI? {
        return pieceInfoRepository.getPopPieceInfoImg(pieceIdx, pieceImg)
    }
}