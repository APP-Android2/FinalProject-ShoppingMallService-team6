package kr.co.lion.unipiece.ui.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.co.lion.unipiece.model.AuthorInfoData
import kr.co.lion.unipiece.model.GalleryInfoData
import kr.co.lion.unipiece.model.NewsInfoData
import kr.co.lion.unipiece.model.PromoteInfoData
import kr.co.lion.unipiece.repository.AuthorInfoRepository
import kr.co.lion.unipiece.repository.GalleryInfoRepository
import kr.co.lion.unipiece.repository.NewsInfoRepository
import kr.co.lion.unipiece.repository.PromoteInfoRepository

class HomeViewModel : ViewModel() {

    private val authorInfoRepository = AuthorInfoRepository()

    private val galleryInfoRepository = GalleryInfoRepository()

    private val promoteInfoRepository = PromoteInfoRepository()

    private val newsInfoRepository = NewsInfoRepository()

    //작가 정보 리스트
    private val _authorInfoDataList = MutableLiveData<List<AuthorInfoData>>()
    var authorInfoDataList: LiveData<List<AuthorInfoData>> = _authorInfoDataList

    private val _galleryInfoList = MutableLiveData<List<String>>()
    var galleryInfoList: LiveData<List<String>> = _galleryInfoList

    private var _promoteInfo = MutableLiveData<List<String>>()
    val promoteInfo:LiveData<List<String>> = _promoteInfo

    private var _newsInfo = MutableLiveData<List<String>>()
    var newsInfo : LiveData<List<String>> = _newsInfo

    //작가 리스트 가져오기
    suspend fun getAuthorInfoAll(){
        val authorInfo = authorInfoRepository.getAuthorInfoAll()
        _authorInfoDataList.value = authorInfo
    }


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


    //시간순으로 데이터 가져오기
    suspend fun getGalleryDataByDate():List<GalleryInfoData>{
        return galleryInfoRepository.getGalleryDataByDate()
    }


    //이미지의 이름으로 데이터 가져오기
    suspend fun getGalleryInfoByImg(galleryImg:String): GalleryInfoData?{
        return galleryInfoRepository.getGalleryByImage(galleryImg)
    }


    fun getPromoteImages(){
        viewModelScope.launch {
            try {
                val imageUrls = promoteInfoRepository.getPromoteImage()
                _promoteInfo.value = imageUrls
            }catch (e : Exception){
                "에러 : ${e}"
            }
        }
    }

    suspend fun getPromoteDataByDate():List<PromoteInfoData>{
        return promoteInfoRepository.gettingDataByDate()
    }

    suspend fun getPromoteInfoByImage(promoteImg:String): PromoteInfoData?{
        return promoteInfoRepository.gettingPromoteInfoByImage(promoteImg)
    }

    fun getNewsImages(){
        viewModelScope.launch {
            try {
                val images = withContext(Dispatchers.IO){
                    newsInfoRepository.getNewsImage()
                }
                _newsInfo.value = images
            }catch (e: Exception){
                "에러 : ${e}"
            }
        }
    }



    suspend fun getNewsInfoByImage(newsImg:String): NewsInfoData?{
        return newsInfoRepository.getNewsInfoByImage(newsImg)
    }


}