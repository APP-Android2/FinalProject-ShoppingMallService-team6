package kr.co.lion.unipiece.ui.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.co.lion.unipiece.model.NewsInfoData
import kr.co.lion.unipiece.repository.NewsInfoRepository

class NewsInfoViewModel : ViewModel(){

    private val newsInfoRepository = NewsInfoRepository()

    private var _newsInfo = MutableLiveData<List<String>>()
    var newsInfo : LiveData<List<String>> = _newsInfo


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



    suspend fun getNewsInfoByImage(newsImg:String):NewsInfoData?{
        return newsInfoRepository.getNewsInfoByImage(newsImg)
    }

}