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

    private val _popPieceInfoList = MutableLiveData<List<PieceInfoData>>()
    val popPieceInfoList : LiveData<List<PieceInfoData>> = _popPieceInfoList

    private val _newPieceInfoList = MutableLiveData<List<PieceInfoData>>()
    val newPieceInfoList : LiveData<List<PieceInfoData>> = _newPieceInfoList

    init {
        viewModelScope.launch {
            getPopPieceInfo()
            getNewPieceInfo()
        }
    }

    private suspend fun getPopPieceInfo(){
        val response = pieceInfoRepository.getPopPieceInfo()
        val pieceInfoList = mutableListOf<PieceInfoData>()

        response.forEach { pieceInfoData ->
            val pieceImgUrl = getPieceImg(pieceInfoData.pieceIdx.toString(), pieceInfoData.pieceImg)?.toString()
            pieceInfoData.pieceImg = pieceImgUrl ?: pieceInfoData.pieceImg
            pieceInfoList.add(pieceInfoData)
        }

        _popPieceInfoList.value = pieceInfoList
    }

    private suspend fun getNewPieceInfo(){
        val response = pieceInfoRepository.getNewPieceInfo()
        val pieceInfoList = mutableListOf<PieceInfoData>()

        response.forEach { pieceInfoData ->
            val pieceImgUrl = getPieceImg(pieceInfoData.pieceIdx.toString(), pieceInfoData.pieceImg)?.toString()
            pieceInfoData.pieceImg = pieceImgUrl ?: pieceInfoData.pieceImg
            pieceInfoList.add(pieceInfoData)
        }

        _newPieceInfoList.value = pieceInfoList
    }

    private suspend fun getPieceImg(pieceIdx: String, pieceImg: String): URI? {
        return pieceInfoRepository.getPieceInfoImg(pieceIdx, pieceImg)
    }
}