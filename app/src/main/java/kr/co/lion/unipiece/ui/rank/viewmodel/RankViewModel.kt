package kr.co.lion.unipiece.ui.rank.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.model.PieceInfoData
import kr.co.lion.unipiece.repository.PieceInfoRepository

class RankViewModel(): ViewModel() {

    private val pieceInfoRepository = PieceInfoRepository()

    private val _pieceRankList = MutableLiveData<List<PieceInfoData>>()
    val pieceRankList : LiveData<List<PieceInfoData>> = _pieceRankList

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading

    fun setLoading(check: Boolean){
        _loading.value = check
    }

    suspend fun getPopPieceInfo(){
        val response = pieceInfoRepository.getPopPieceInfo()
        val updatedPieceInfoList = updateImagePieceInfo(response)

        _pieceRankList.value = updatedPieceInfoList
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