package kr.co.lion.unipiece.ui.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.model.PromoteInfoData
import kr.co.lion.unipiece.repository.PromoteInfoRepository

class PromoteInfoViewModel : ViewModel() {

    private val promoteInfoRepository = PromoteInfoRepository()

    private var _promoteInfo = MutableLiveData<List<String>>()
    val promoteInfo:LiveData<List<String>> get() = _promoteInfo


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

    suspend fun getPromoteInfoByImage(promoteImg:String):PromoteInfoData?{
        return promoteInfoRepository.gettingPromoteInfoByImage(promoteImg)
    }

}