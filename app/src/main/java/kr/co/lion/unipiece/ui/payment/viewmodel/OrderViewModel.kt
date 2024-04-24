package kr.co.lion.unipiece.ui.payment.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.model.PieceInfoData
import kr.co.lion.unipiece.repository.PieceInfoRepository

class OrderViewModel : ViewModel() {

    private val pieceInfoRepository = PieceInfoRepository()

    val userIdx = UniPieceApplication.prefs.getUserIdx("userIdx", 0)

    // pieceIdxList로 가져온 작품 리스트
    private val _pieceIdxPieceInfoDataList = MutableLiveData<List<PieceInfoData>>()
    val pieceIdxPieceInfoDataList: LiveData<List<PieceInfoData>> = _pieceIdxPieceInfoDataList


    // 장바구니에서 넘어온 리스트데이터로 작품정보들을 가져온다.
    suspend fun getIdxPieceInfo(pieceIdxList: ArrayList<Int>) {
        viewModelScope.launch {
            try {
                val pieceInfoList = mutableListOf<PieceInfoData>()
                // 여기서는 예시로 간단한 반복문을 사용했습니다. 실제로는 Room 또는 Retrofit 등을 사용하여 데이터를 가져와야 합니다.
                pieceIdxList.forEach { pieceIdx ->
                    val pieceInfo = pieceInfoRepository.getIdxPieceInfo(pieceIdx)
                    pieceInfo?.let { pieceInfoList.add(it) }
                }
                val updatedPieceInfoList = updateImagePieceInfo(pieceInfoList)
                _pieceIdxPieceInfoDataList.postValue(updatedPieceInfoList)
            } catch (e: Exception) {
                Log.e("Firebase Error", "Error vmGetIdxPieceInfo : ${e.message}")
            }
        }
    }

    // 작품상세에서 넘어온 pieceIdx로 작품 정보를 가져온다.
    suspend fun getIdxPieceInfoOne(pieceIdx:Int) {
        viewModelScope.launch {
            try {
                val pieceInfo = pieceInfoRepository.getIdxPieceInfo(pieceIdx)
                pieceInfo?.let {
                    // 현재 LiveData에 저장된 리스트를 가져오고, 새로운 정보를 추가한 뒤 LiveData를 업데이트합니다.
                    val currentList = _pieceIdxPieceInfoDataList.value?.toMutableList() ?: mutableListOf()
                    currentList.add(it)
                    val updatedPieceInfoList = updateImagePieceInfo(currentList)
                    _pieceIdxPieceInfoDataList.postValue(updatedPieceInfoList)
                }
            } catch (e: Exception) {
                Log.e("Firebase Error", "Error vmGetIdxPieceInfoOne : ${e.message}")
            }
        }
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