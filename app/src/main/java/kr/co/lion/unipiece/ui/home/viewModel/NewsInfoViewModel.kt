package kr.co.lion.unipiece.ui.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.repository.NewsInfoRepository

class NewsInfoViewModel : ViewModel(){

    private val newsInfoRepository = NewsInfoRepository()

    private var _newsInfo = MutableLiveData<List<String>>()
    var newsInfo : LiveData<List<String>> = _newsInfo


    fun getNewsImages(){
        viewModelScope.launch {
            try {
                val imageUrl = newsInfoRepository.getNewsImage()
                _newsInfo.value = imageUrl
            }catch (e: Exception){
                "에러 : ${e}"
            }
        }
    }

}