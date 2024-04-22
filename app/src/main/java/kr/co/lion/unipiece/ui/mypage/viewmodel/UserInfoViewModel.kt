package kr.co.lion.unipiece.ui.mypage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.model.UserInfoData
import kr.co.lion.unipiece.repository.UserInfoRepository

class UserInfoViewModel: ViewModel() {
    // 회원 정보
    private val _userInfoData = MutableLiveData<UserInfoData>()
    val userInfoData:LiveData<UserInfoData> = _userInfoData

    private val userInfoRepository = UserInfoRepository()

    // 회원 정보를 불러오기
    suspend fun getUserDataByIdx(userIdx:Int){
        val job1 = viewModelScope.launch {
            _userInfoData.value = userInfoRepository.getUserDataByIdx(userIdx)
        }
        job1.join()
    }
}