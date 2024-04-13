package kr.co.lion.unipiece.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.co.lion.unipiece.model.UserInfoData
import kr.co.lion.unipiece.repository.UserInfoRepository

class LoginViewModel : ViewModel() {
    private val _userInfo = MutableLiveData<List<UserInfoData>>()
    val userInfo: LiveData<List<UserInfoData>> = _userInfo

    private val userInfoRepository = UserInfoRepository()




    //회원 정보를 저장한다
    fun insertUserData(
        userName:String, nickName:String, phoneNumber: String, userId:String, userPwd:String,
        userState: Boolean, callback:(Boolean) -> Unit
    ){
        viewModelScope.launch {

            val userSequence = userInfoRepository.getUserSequence()

            userInfoRepository.updateUserSequence(userSequence + 1)

            val userIdx = userSequence + 1

            val userInfoData = UserInfoData(userName, nickName, phoneNumber, userId, userPwd, userIdx, userState)
            // 비동기 작업을 메인 스레드에서 실행하여 callback에 값을 전달
            val success = withContext(Dispatchers.IO) {
                try {
                    userInfoRepository.insertUserData(userInfoData)
                    true // 성공적으로 데이터가 삽입됨
                } catch (e: Exception) {
                    Log.e("InsertUserData", "Error inserting user data: ${e.message}")
                    false // 데이터 삽입 중 오류 발생
                }
            }

            // callback에 성공 여부를 전달
            callback(success)
        }

    }

    //회원 정보를 가져온다
    fun getUserDataByIdx(userIdx:Int){
        viewModelScope.launch {
            userInfoRepository.getUserDataByIdx(userIdx)
        }
    }


    //아이디가 중복되는지 검사한다
//    fun checkUserId(userId:String):Boolean{
//        viewModelScope.launch{
//            userInfoRepository.checkUserId(userId)
//        }
//
//    }

}