package kr.co.lion.unipiece.ui.mygallery.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.model.PieceAddInfoData
import kr.co.lion.unipiece.repository.PieceAddInfoRepository

class PieceAddInfoViewModel : ViewModel() {
    private val pieceAddInfoRepository = PieceAddInfoRepository()

    private val _addPieceInfoResult = MutableLiveData<Boolean>()
    val addPieceInfoResult: LiveData<Boolean> = _addPieceInfoResult

    private val _uploadImageResult = MutableLiveData<String?>()
    val uploadImageResult: LiveData<String?> = _uploadImageResult

    fun addPieceInfo(pieceAddInfoData: PieceAddInfoData) {
        viewModelScope.launch {
            try {
                pieceAddInfoRepository.addPieceInfo(pieceAddInfoData)
                _addPieceInfoResult.value = true
            } catch (throwable: Throwable) {
                _addPieceInfoResult.value = false
            }
        }
    }

    fun uploadImage(imageUri: Uri) {
        viewModelScope.launch {
            try {
                val fileName = pieceAddInfoRepository.uploadImage(imageUri)
                _uploadImageResult.value = fileName
            } catch (throwable: Throwable) {
                Log.e("PieceAddInfoViewModel", "Image upload failed: $throwable")
                _uploadImageResult.value = null
            }
        }
    }
}