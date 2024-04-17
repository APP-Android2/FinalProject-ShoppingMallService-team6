package kr.co.lion.unipiece.ui.buy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.model.PieceInfoData
import kr.co.lion.unipiece.repository.PieceInfoRepository

class BuyViewModel(): ViewModel() {

    private val pieceInfoRepository = PieceInfoRepository()

    private val _popPieceInfoList = MutableLiveData<List<PieceInfoData>>()
    val popPieceInfoList : LiveData<List<PieceInfoData>> = _popPieceInfoList

    private val _newPieceInfoList = MutableLiveData<List<PieceInfoData>>()
    val newPieceInfoList : LiveData<List<PieceInfoData>> = _newPieceInfoList

    private val _loadingData: MutableLiveData<Unit> = MutableLiveData()
    val loadingData: LiveData<Unit> = _loadingData

    init {
        viewModelScope.launch {
            getPopPieceInfo()
            getNewPieceInfo()
        }
    }

    fun loading(){
        _loadingData.value = Unit
    }

    suspend fun getPopPieceInfo(){
        val response = pieceInfoRepository.getPopPieceInfo()
        val pieceInfoList = mutableListOf<PieceInfoData>()

        response.forEach { pieceInfoData ->
            val pieceImgUrl = getPieceImg(pieceInfoData.pieceIdx.toString(), pieceInfoData.pieceImg)
            pieceInfoData.pieceImg = pieceImgUrl ?: pieceInfoData.pieceImg
            pieceInfoList.add(pieceInfoData)
        }

        _popPieceInfoList.value = pieceInfoList
    }

    suspend fun getNewPieceInfo(){
        val response = pieceInfoRepository.getNewPieceInfo()
        val pieceInfoList = mutableListOf<PieceInfoData>()

        response.forEach { pieceInfoData ->
            val pieceImgUrl = getPieceImg(pieceInfoData.pieceIdx.toString(), pieceInfoData.pieceImg)
            pieceInfoData.pieceImg = pieceImgUrl ?: pieceInfoData.pieceImg
            pieceInfoList.add(pieceInfoData)
        }

        _newPieceInfoList.value = pieceInfoList
    }

    suspend fun getPopPieceSort(category: String){
        val response = pieceInfoRepository.getPopPieceSort(category)
        val pieceInfoList = mutableListOf<PieceInfoData>()

        response.forEach { pieceInfoData ->
            val pieceImgUrl = getPieceImg(pieceInfoData.pieceIdx.toString(), pieceInfoData.pieceImg)
            pieceInfoData.pieceImg = pieceImgUrl ?: pieceInfoData.pieceImg
            pieceInfoList.add(pieceInfoData)
        }

        _popPieceInfoList.value = pieceInfoList
    }

    suspend fun getPopPieceDetailSort(detailCategory: String){
        val response = pieceInfoRepository.getPopPieceDetailSort(detailCategory)
        val pieceInfoList = mutableListOf<PieceInfoData>()

        response.forEach { pieceInfoData ->
            val pieceImgUrl = getPieceImg(pieceInfoData.pieceIdx.toString(), pieceInfoData.pieceImg)
            pieceInfoData.pieceImg = pieceImgUrl ?: pieceInfoData.pieceImg
            pieceInfoList.add(pieceInfoData)
        }

        _popPieceInfoList.value = pieceInfoList
    }

    suspend fun getNewPieceSort(category: String){
        val response = pieceInfoRepository.getNewPieceSort(category)
        val pieceInfoList = mutableListOf<PieceInfoData>()

        response.forEach { pieceInfoData ->
            val pieceImgUrl = getPieceImg(pieceInfoData.pieceIdx.toString(), pieceInfoData.pieceImg)
            pieceInfoData.pieceImg = pieceImgUrl ?: pieceInfoData.pieceImg
            pieceInfoList.add(pieceInfoData)
        }

        _newPieceInfoList.value = pieceInfoList
    }

    suspend fun getNewPieceDetailSort(detailCategory: String){
        val response = pieceInfoRepository.getNewPieceDetailSort(detailCategory)
        val pieceInfoList = mutableListOf<PieceInfoData>()

        response.forEach { pieceInfoData ->
            val pieceImgUrl = getPieceImg(pieceInfoData.pieceIdx.toString(), pieceInfoData.pieceImg)
            pieceInfoData.pieceImg = pieceImgUrl ?: pieceInfoData.pieceImg
            pieceInfoList.add(pieceInfoData)
        }

        _newPieceInfoList.value = pieceInfoList
    }

    private suspend fun getPieceImg(pieceIdx: String, pieceImg: String): String? {
        return pieceInfoRepository.getPieceInfoImg(pieceIdx, pieceImg)
    }
}