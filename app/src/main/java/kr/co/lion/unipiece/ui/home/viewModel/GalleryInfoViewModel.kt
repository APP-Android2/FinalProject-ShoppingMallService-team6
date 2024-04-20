package kr.co.lion.unipiece.ui.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.model.GalleryInfoData
import kr.co.lion.unipiece.repository.GalleryInfoRepository

class GalleryInfoViewModel : ViewModel() {

    private val galleryInfoRepository = GalleryInfoRepository()

    private val _galleryInfoList = MutableLiveData<List<String>>()
    var galleryInfoList: LiveData<List<String>> = _galleryInfoList


    fun getGalleryImg(){
        viewModelScope.launch {
            try {
                val imageUrl = galleryInfoRepository.getGalleryImg()
                _galleryInfoList.value = imageUrl
            }catch (e:Exception){
                "에러 : ${e}"
            }
        }
    }

    //이미지의 이름으로 데이터 가져오기
    suspend fun getGalleryInfoByImg(galleryImg:String): GalleryInfoData?{
        return galleryInfoRepository.getGalleryByImage(galleryImg)
    }



}