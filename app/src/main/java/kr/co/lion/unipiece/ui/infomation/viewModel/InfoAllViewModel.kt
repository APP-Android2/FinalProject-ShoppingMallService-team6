package kr.co.lion.unipiece.ui.infomation.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.model.PromoteInfoData
import kr.co.lion.unipiece.repository.PromoteInfoRepository

class InfoAllViewModel : ViewModel() {

    private val promoteInfoRepository = PromoteInfoRepository()

    private val _promoteInfoList = MutableLiveData<List<PromoteInfoData>>()
    var promoteInfoList: LiveData<List<PromoteInfoData>> = _promoteInfoList

    init {
        viewModelScope.launch {
            gettingDataByDate()
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



    //이미지 Url을 받아온다
    private suspend fun getPromoteInfoImg(image:String): String? {
        return promoteInfoRepository.getPromoteInfoImg(image)
    }
}