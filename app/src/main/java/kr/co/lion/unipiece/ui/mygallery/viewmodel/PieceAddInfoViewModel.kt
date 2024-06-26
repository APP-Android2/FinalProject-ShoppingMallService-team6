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
import kr.co.lion.unipiece.repository.AuthorAddRepository
import kr.co.lion.unipiece.repository.AuthorInfoRepository
import kr.co.lion.unipiece.repository.PieceAddInfoRepository

class PieceAddInfoViewModel : ViewModel() {
    private val pieceAddInfoRepository = PieceAddInfoRepository()
    private val authorInfoRepository = AuthorInfoRepository()
    private val authorAddRepository = AuthorAddRepository()

    private val _addPieceInfoResult = MutableLiveData<Boolean>()
    val addPieceInfoResult: LiveData<Boolean> = _addPieceInfoResult

    private val _updatePieceInfoResult = MutableLiveData<Boolean>()
    val updatePieceInfoResult: LiveData<Boolean> = _updatePieceInfoResult

    private val _authorIdx = MutableLiveData<Int>()
    val authorIdx : LiveData<Int> = _authorIdx

    private val _authorName = MutableLiveData<String>()
    val authorName : LiveData<String> = _authorName

    private val _imageFileName = MutableLiveData<String>()
    val imageFileName : LiveData<String> = _imageFileName

    private val _pieceAddInfoList = MutableLiveData<List<PieceAddInfoData>>()
    val pieceAddInfoList : LiveData<List<PieceAddInfoData>> = _pieceAddInfoList

    private val _pieceAddInfo = MutableLiveData<PieceAddInfoData>()
    val pieceAddInfo : LiveData<PieceAddInfoData> = _pieceAddInfo

    private val _uploadImageResult = MutableLiveData<String?>()
    val uploadImageResult: LiveData<String?> = _uploadImageResult

    private val _isAuthor = MutableLiveData<Boolean>()
    val isAuthor: LiveData<Boolean> = _isAuthor

    private val _isAuthorAdd = MutableLiveData<Boolean>()
    val isAuthorAdd: LiveData<Boolean> = _isAuthorAdd

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            val userIdx = getUserIdxFromSharedPreferences()
            val isAuthor = authorInfoRepository.isAuthor(userIdx)
            _isAuthor.value = isAuthor

            if (isAuthor) {
                getAuthorIdx(userIdx)
            } else {
                _isAuthorAdd.value = authorAddRepository.isAuthorAdd(userIdx)
            }
        }
    }

    private fun getAuthorIdx(userIdx: Int) {
        viewModelScope.launch {
            try {
                val authorIdx = authorInfoRepository.getAuthorIdxByUserIdx(userIdx)
                _authorIdx.value = authorIdx

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

            } catch (throwable: Throwable) {
                Log.e("PieceAddInfoViewModel", "Failed to get authorName: $throwable")
            }
        }
    }

    suspend fun getPieceAddInfo() {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val authorIdx = _authorIdx.value ?: 0
                val pieceAddInfoList = pieceAddInfoRepository.getPieceAddInfo(authorIdx)

                // 이미지를 제외한 데이터 먼저 UI에 반영
                updateUIWithData(pieceAddInfoList)

                _isLoading.value = false

                // 각 이미지를 비동기적으로 가져오고, 가져온 이미지를 데이터에 추가하여 UI 업데이트
                pieceAddInfoList.forEach { pieceAddInfo ->
                    val imageName = pieceAddInfo.addPieceImg
                    viewModelScope.launch {
                        val imageUrl = getPieceAddInfoImage(authorIdx, imageName)
                        imageUrl?.let {
                            pieceAddInfo.addPieceImg = it.toString()
                            updateUIWithData(pieceAddInfoList)
                        }
                    }
                }

            } catch (throwable: Throwable) {
                _isLoading.value = false
            }
        }
    }

    suspend fun getPieceAddInfoByAddPieceIdx(authorIdx: Int, addPieceIdx: Int) {
        viewModelScope.launch {
            try {
                val pieceAddInfo = pieceAddInfoRepository.getPieceAddInfoByAddPieceIdx(addPieceIdx)

                if (pieceAddInfo != null) {
                    _imageFileName.value = pieceAddInfo.addPieceImg
                    // 이미지를 제외한 데이터 먼저 UI에 반영
                    updateUIWithOneData(pieceAddInfo)

                    // 이미지를 비동기적으로 가져오기
                    val imageName = pieceAddInfo.addPieceImg
                    viewModelScope.launch {
                        val imageUrl = getPieceAddInfoImage(authorIdx, imageName)
                        imageUrl?.let {
                            pieceAddInfo.addPieceImg = it.toString()
                            updateUIWithOneData(pieceAddInfo)
                        }
                    }
                }
            } catch (throwable: Throwable) {
                Log.e("PieceAddInfoViewModel", "Failed to get pieceAddInfo: $throwable")
            }
        }
    }

    private fun updateUIWithData(pieceAddInfoList: List<PieceAddInfoData>) {
        _pieceAddInfoList.value = pieceAddInfoList
    }

    private fun updateUIWithOneData(pieceAddInfoData: PieceAddInfoData) {
        _pieceAddInfo.value = pieceAddInfoData
    }

    fun addPieceInfo(pieceAddInfoData: PieceAddInfoData) {
        viewModelScope.launch {
            try {
                pieceAddInfoData.addPieceIdx = getAddPieceIdx()

                _addPieceInfoResult.value = pieceAddInfoRepository.addPieceInfo(pieceAddInfoData)
            } catch (throwable: Throwable) {
                _addPieceInfoResult.value = false
            }
        }
    }

    fun updatePieceAddInfo(pieceAddInfoData: PieceAddInfoData) {
        viewModelScope.launch {
            try {
                _updatePieceInfoResult.value = pieceAddInfoRepository.updatePieceAddInfo(pieceAddInfoData)
            } catch (throwable: Throwable) {
                _updatePieceInfoResult.value = false
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
                _uploadImageResult.value = null
            }
        }
    }

    fun updateImage(authorIdx: Int, imageUri: Uri, imageFileName: String) {
        viewModelScope.launch {
            try {
                pieceAddInfoRepository.updateImage(authorIdx, imageUri, imageFileName)
            } catch (throwable: Throwable) {
                Log.e("PieceAddInfoViewModel", "Image upload failed: $throwable")
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
}