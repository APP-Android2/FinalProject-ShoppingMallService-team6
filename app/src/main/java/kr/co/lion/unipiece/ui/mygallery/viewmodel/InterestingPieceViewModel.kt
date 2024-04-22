package kr.co.lion.unipiece.ui.mygallery.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.model.LikePieceData
import kr.co.lion.unipiece.model.PieceInfoData
import kr.co.lion.unipiece.repository.LikePieceRepository
import kr.co.lion.unipiece.repository.PieceInfoRepository

class InterestingPieceViewModel : ViewModel() {
    private val likePieceInfoRepository = LikePieceRepository()
    private val pieceInfoRepository = PieceInfoRepository()

    private val _likePieceInfoList = MutableLiveData<List<PieceInfoData>>()
    val likePieceInfoList : LiveData<List<PieceInfoData>> = _likePieceInfoList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            val userIdx = getUserIdxFromSharedPreferences()

            getUserLikedPieceInfo(userIdx)
        }
    }

    private suspend fun getUserLikedPieceInfo(userIdx: Int) {
        _isLoading.value = true

        val userLikedPieceList = likePieceInfoRepository.getUserLikedPiece(userIdx)

        val likedPieceInfoList = mutableListOf<PieceInfoData?>()
        for (likePieceList in userLikedPieceList) {
            val pieceIdxInfo = pieceInfoRepository.getIdxPieceInfo(likePieceList.pieceIdx)
            likedPieceInfoList.add(pieceIdxInfo)
        }

        _likePieceInfoList.postValue(likedPieceInfoList.filterNotNull())

        _isLoading.value = false

        // 이미지 로드
        likedPieceInfoList.filterNotNull().forEachIndexed { index, pieceInfo ->
            viewModelScope.launch {
                val pieceImgUrl = getPieceAddInfoImage(pieceInfo.pieceIdx.toString(), pieceInfo.pieceImg)
                // 이미지가 성공적으로 가져와지면 UI에 추가
                pieceImgUrl?.let { imgUrl ->
                    val updatedPieceInfoWithImage = pieceInfo.copy(pieceImg = imgUrl)
                    likedPieceInfoList[index] = updatedPieceInfoWithImage
                    // UI에 데이터 및 이미지 업데이트
                    updateUIWithData(likedPieceInfoList.filterNotNull())
                    Log.d("test1234", updatedPieceInfoWithImage.pieceImg)
                }
            }
        }
    }

    private fun updateUIWithData(updatedData: List<PieceInfoData>) {
        _likePieceInfoList.postValue(updatedData)
    }


    fun cancelLikePiece(pieceIdx: Int, userIdx: Int) {
        viewModelScope.launch {
            // 리사이클러뷰에서 해당 아이템 삭제
            val updatedList = _likePieceInfoList.value.orEmpty().toMutableList()
            val index = updatedList.indexOfFirst { it.pieceIdx == pieceIdx }
            if (index != -1) {
                updatedList.removeAt(index)
                _likePieceInfoList.postValue(updatedList)
            }

            likePieceInfoRepository.cancelLikePiece(pieceIdx, userIdx)
        }
    }


    private suspend fun getPieceAddInfoImage(pieceIdx: String, pieceImg: String): String? {
        return pieceInfoRepository.getPieceInfoImg(pieceIdx, pieceImg)
    }

    private fun getUserIdxFromSharedPreferences(): Int {
        val sharedPrefs = UniPieceApplication.prefs
        return sharedPrefs.getUserIdx("userIdx", 0)
    }
}