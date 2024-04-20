package kr.co.lion.unipiece.ui.buy.viewmodel

import android.util.Log
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

    private val _popLoading: MutableLiveData<Boolean> = MutableLiveData()
    val popLoading: LiveData<Boolean> = _popLoading

    private val _newLoading: MutableLiveData<Boolean> = MutableLiveData()
    val newLoading: LiveData<Boolean> = _newLoading

    init {
        viewModelScope.launch {
            getPopPieceInfo()
            getNewPieceInfo()
        }
    }

    fun popLoading(check: Boolean){
        _popLoading.value = check
    }

    fun newLoading(check: Boolean){
        _newLoading.value = check
    }

    suspend fun getPopPieceInfo(){
        val response = pieceInfoRepository.getPopPieceInfo()
        val updatedPieceInfoList = updateImagePieceInfo(response)

        _popPieceInfoList.value = updatedPieceInfoList
    }

    suspend fun getNewPieceInfo(){
        val response = pieceInfoRepository.getNewPieceInfo()
        val updatedPieceInfoList = updateImagePieceInfo(response)

        _newPieceInfoList.value = updatedPieceInfoList
    }

    suspend fun getPopPieceSort(category: String){
        val response = pieceInfoRepository.getPopPieceSort(category)
        val updatedPieceInfoList = updateImagePieceInfo(response)

        _popPieceInfoList.value = updatedPieceInfoList
    }

    suspend fun getPopPieceDetailSort(detailCategory: String){
        val response = pieceInfoRepository.getPopPieceDetailSort(detailCategory)
        val updatedPieceInfoList = updateImagePieceInfo(response)

        _popPieceInfoList.value = updatedPieceInfoList
    }

    suspend fun getNewPieceSort(category: String){
        val response = pieceInfoRepository.getNewPieceSort(category)
        val updatedPieceInfoList = updateImagePieceInfo(response)

        _newPieceInfoList.value = updatedPieceInfoList
    }

    suspend fun getNewPieceDetailSort(detailCategory: String){
        val response = pieceInfoRepository.getNewPieceDetailSort(detailCategory)
        val updatedPieceInfoList = updateImagePieceInfo(response)

        _newPieceInfoList.value = updatedPieceInfoList
    }

    suspend fun updateImagePieceInfo(pieceInfoList: List<PieceInfoData>): List<PieceInfoData> {
        return pieceInfoList.map { pieceInfoData ->
            val pieceImgUrl = getPieceImg(pieceInfoData.pieceIdx.toString(), pieceInfoData.pieceImg)
            pieceInfoData.copy(pieceImg = pieceImgUrl ?: pieceInfoData.pieceImg)
        }
    }


    private suspend fun getPieceImg(pieceIdx: String, pieceImg: String): String? {
        return pieceInfoRepository.getPieceInfoImg(pieceIdx, pieceImg)
    }
}