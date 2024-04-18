package kr.co.lion.unipiece.ui.login.viewModel

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

    //회원 정보를 가져온다 (userIdx)
    suspend fun getUserDataByIdx(userIdx:Int):UserInfoData?{
        return userInfoRepository.getUserDataByIdx(userIdx)
    }

    //회원 정보를 가져온다 (userId)
    suspend fun getUserDataByUserId(userId: String):UserInfoData?{
        return userInfoRepository.getUserDataByUserId(userId)
    }


    //아이디가 중복되는지 검사한다
    suspend fun checkUserId(userId:String):Boolean{
        return userInfoRepository.checkUserId(userId)
    }

}