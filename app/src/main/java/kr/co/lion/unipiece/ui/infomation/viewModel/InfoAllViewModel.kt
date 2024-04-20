package kr.co.lion.unipiece.ui.infomation.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.model.GalleryInfoData
import kr.co.lion.unipiece.model.NewsInfoData
import kr.co.lion.unipiece.model.PromoteInfoData
import kr.co.lion.unipiece.repository.GalleryInfoRepository
import kr.co.lion.unipiece.repository.NewsInfoRepository
import kr.co.lion.unipiece.repository.PromoteInfoRepository

class InfoAllViewModel : ViewModel() {

    private val promoteInfoRepository = PromoteInfoRepository()

    private val newsInfoRepository = NewsInfoRepository()

    private val galleryInfoRepository = GalleryInfoRepository()

    private val _promoteInfoList = MutableLiveData<List<PromoteInfoData>>()
    var promoteInfoList: LiveData<List<PromoteInfoData>> = _promoteInfoList


    private val _newsInfoList = MutableLiveData<List<NewsInfoData>>()
    var newsInfoList: LiveData<List<NewsInfoData>> = _newsInfoList

    private val _galleryInfoList = MutableLiveData<List<GalleryInfoData>>()
    var galleryInfoList: LiveData<List<GalleryInfoData>> = _galleryInfoList



    init {
        viewModelScope.launch {
            gettingDataByDate()
            getNewsDataByDate()
            getGalleryDataByDate()
        }
    }

    suspend fun gettingDataByDate(){
        val promoteInfo = promoteInfoRepository.gettingDataByDate()
        val promoteInfoList = mutableListOf<PromoteInfoData>()

        promoteInfo.forEach { promoteInfoData ->
            val promoteImg = getPromoteInfoImg(promoteInfoData.promoteImg)
            promoteInfoData.promoteImg = promoteImg ?: promoteInfoData.promoteImg
            promoteInfoList.add(promoteInfoData)
        }

        _promoteInfoList.value = promoteInfoList
    }

    //소식의 정보를 받아온다
    suspend fun getNewsDataByDate(){
        val newsInfo = newsInfoRepository.getNewsDataByDate()
        val newsInfoList = mutableListOf<NewsInfoData>()

        newsInfo.forEach { newsInfoData ->
            val newsImg = getNewsInfoImg(newsInfoData.newsImg)
            newsInfoData.newsImg = newsImg ?: newsInfoData.newsImg
            newsInfoList.add(newsInfoData)
        }
        _newsInfoList.value = newsInfoList
    }

    //전시실 작품의 정보 리스트를 받아온다
    suspend fun getGalleryDataByDate(){
        val galleryInfo = galleryInfoRepository.getGalleryDataByDate()
        val galleryInfoList = mutableListOf<GalleryInfoData>()

        galleryInfo.forEach {galleryInfoData ->
            val galleryImg = getGalleryInfoImg(galleryInfoData.galleryInfoImg)
            galleryInfoData.galleryInfoImg = galleryImg ?: galleryInfoData.galleryInfoImg
            galleryInfoList.add(galleryInfoData)
        }
         _galleryInfoList.value = galleryInfoList
    }



    //이미지 Url을 받아온다 (promote)
    private suspend fun getPromoteInfoImg(image:String): String? {
        return promoteInfoRepository.getPromoteInfoImg(image)
    }

    //이미지 Url을 받아온다 (News)
    private suspend fun getNewsInfoImg(image: String): String? {
        return newsInfoRepository.getNewsInfoImg(image)
    }

    //이미지 Url을 받아온다
    suspend fun getGalleryInfoImg(image:String): String?{
        return galleryInfoRepository.getGalleryInfoImg(image)
    }
}