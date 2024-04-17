package kr.co.lion.unipiece.ui.mygallery.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.model.PieceAddInfoData
import kr.co.lion.unipiece.repository.AuthorInfoRepository
import kr.co.lion.unipiece.repository.PieceAddInfoRepository

class PieceAddInfoViewModel : ViewModel() {
    private val pieceAddInfoRepository = PieceAddInfoRepository()
    private val authorInfoRepository = AuthorInfoRepository()

    private val _addPieceInfoResult = MutableLiveData<Boolean>()
    val addPieceInfoResult: LiveData<Boolean> = _addPieceInfoResult

    private val _authorIdx = MutableLiveData<Int>()
    val authorIdx : LiveData<Int> = _authorIdx

    private val _authorName = MutableLiveData<String>()
    val authorName : LiveData<String> = _authorName

    private val _pieceAddInfoList = MutableLiveData<List<PieceAddInfoData>>()
    val pieceAddInfoList : LiveData<List<PieceAddInfoData>> = _pieceAddInfoList

    private val _uploadImageResult = MutableLiveData<String?>()
    val uploadImageResult: LiveData<String?> = _uploadImageResult

    private val _isAuthor = MutableLiveData<Boolean>()
    val isAuthor: LiveData<Boolean> = _isAuthor

    init {
        getAuthorIdx()
    }

    private fun getAuthorIdx() {
        viewModelScope.launch {
            try {
                val userIdx = getUserIdxFromSharedPreferences()
                val authorIdx = authorInfoRepository.getAuthorIdxByUserIdx(userIdx)
                _authorIdx.value = authorIdx
                Log.e("PieceAddInfoViewModel", "userIdx : $userIdx")
                Log.e("PieceAddInfoViewModel", "authorIdx : $authorIdx")

                getPieceAddInfo()
                getAuthorName()
            } catch (throwable: Throwable) {
                Log.e("PieceAddInfoViewModel", "Failed to get authorIdx: $throwable")
            }
        }
    }

    suspend fun getAuthorName() {
        viewModelScope.launch {
            try {
                val authorIdx = _authorIdx.value ?: 0
                val authorInfo = authorInfoRepository.getAuthorInfoDataByIdx(authorIdx)
                _authorName.value = authorInfo?.authorName
                Log.e("PieceAddInfoViewModel", "authorIdx : $authorIdx")
                Log.e("PieceAddInfoViewModel", "_authorName : ${_authorName.value}")

            } catch (throwable: Throwable) {
                Log.e("PieceAddInfoViewModel", "Failed to get authorName: $throwable")
            }
        }
    }

    private fun getPieceAddInfo() {
        viewModelScope.launch {
            try {
                val authorIdx = _authorIdx.value ?: 0
                val pieceAddInfoList = pieceAddInfoRepository.getPieceAddInfo(authorIdx)

                pieceAddInfoList.forEach { pieceAddInfo ->
                    val imageName = pieceAddInfo.addPieceImg
                    val imageUrl = getPieceAddInfoImage(authorIdx, imageName)

                    imageUrl?.let {
                        pieceAddInfo.addPieceImg = it.toString()
                    }
                }

                _pieceAddInfoList.value = pieceAddInfoList

                Log.e("PieceAddInfoViewModel", "pieceAddInfoList : $pieceAddInfoList")
            } catch (throwable: Throwable) {
                Log.e("PieceAddInfoViewModel", "Failed to get pieceAddInfo: $throwable")
            }
        }
    }

    fun addPieceInfo(pieceAddInfoData: PieceAddInfoData) {
        viewModelScope.launch {
            try {
                pieceAddInfoData.addPieceIdx = getAddPieceIdx()

                pieceAddInfoRepository.addPieceInfo(pieceAddInfoData)
                _addPieceInfoResult.value = true
            } catch (throwable: Throwable) {
                _addPieceInfoResult.value = false
            }
        }
    }

    private suspend fun getAddPieceIdx(): Int {
        return try {
            val addPieceSequence = pieceAddInfoRepository.getPieceAddSequence()
            pieceAddInfoRepository.updatePieceAddSequence(addPieceSequence + 1)

            addPieceSequence + 1
        } catch (throwable: Throwable) {
            0
        }
    }

    fun uploadImage(authorIdx: Int, imageUri: Uri) {
        viewModelScope.launch {
            try {
                val fileName = pieceAddInfoRepository.uploadImage(authorIdx, imageUri)
                _uploadImageResult.value = fileName
            } catch (throwable: Throwable) {
                Log.e("PieceAddInfoViewModel", "Image upload failed: $throwable")
                _uploadImageResult.value = null
            }
        }
    }

    private suspend fun getPieceAddInfoImage(authorIdx: Int, addPieceImg: String): Uri? {
        return pieceAddInfoRepository.getPieceAddInfoImage(authorIdx, addPieceImg)
    }

    private fun getUserIdxFromSharedPreferences(): Int {
        val sharedPrefs = UniPieceApplication.prefs
        return sharedPrefs.getUserIdx("userIdx", 0)
    }

    private fun isAuthor(userIdx: Int) {
        viewModelScope.launch {
            try {
                val isAuthor = authorInfoRepository.isAuthor(userIdx)
                _isAuthor.value = isAuthor
            } catch (throwable: Throwable) {
                Log.e("PieceAddInfoViewModel", "Failed to check author status: $throwable")
                _isAuthor.value = false
            }
        }
    }

}