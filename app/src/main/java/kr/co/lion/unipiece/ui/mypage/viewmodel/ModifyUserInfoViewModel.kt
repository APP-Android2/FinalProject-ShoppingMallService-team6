package kr.co.lion.unipiece.ui.mypage.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.model.UserInfoData
import kr.co.lion.unipiece.repository.UserInfoRepository

class ModifyUserInfoViewModel: ViewModel() {

    // 회원 정보
    private val _userInfoData = MutableLiveData<UserInfoData>()
    val userInfoData: LiveData<UserInfoData> = _userInfoData

    // 수정 완료 여부
    val checkComplete = MutableLiveData<Boolean>()

    // 회원 탈퇴 완료 여부
    val deleteComplete = MutableLiveData<Boolean>()

    private val userInfoRepository = UserInfoRepository()

    // 회원 정보를 불러오기
    suspend fun getUserDataByIdx(userIdx: Int) = viewModelScope.launch {
        val job1 = launch {
            _userInfoData.value = userInfoRepository.getUserDataByIdx(userIdx)
            _userInfoData.value?.userPwd = ""
        }
        job1.join()
    }

    // 회원 정보 업데이트
    suspend fun updateUserInfo() {
        _userInfoData.value?.let { checkComplete.value = userInfoRepository.updateUserData(it) }
    }

    // 회원 탈퇴

    suspend fun deleteUser() {
        _userInfoData.value?.let { deleteComplete.value = userInfoRepository.deleteUser(it.userIdx) }
    }

}