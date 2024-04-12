package kr.co.lion.unipiece.ui.author

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.co.lion.unipiece.R

class AuthorInfoViewModel: ViewModel() {
    // 작가 이름
    val authorName = MutableLiveData<String>()
    // 팔로워 수
    val authorFollower = MutableLiveData<String>()
    // 작가 기본 정보
    val authorBasic = MutableLiveData<String>()
    // 작가 소개
    val authorInfo = MutableLiveData<String>()
    // 작가 이미지
    val authorImage = MutableLiveData<String>()

    // 팔로우 버튼 배경이미지
    val buttonAuthorFollowBackground = MutableLiveData<Int>()
    // 팔로우 버튼 텍스트 색
    val buttonAuthorFollowTextColor = MutableLiveData<Int>()
    // 팔로우 버튼 텍스트
    val buttonAuthorFollowText = MutableLiveData<String>()

    // 팔로우 버튼 상태 변경
    fun changeFollowState(state:Boolean){
        if(state){
            buttonAuthorFollowBackground.value = R.drawable.button_radius
            buttonAuthorFollowTextColor.value = Color.WHITE
            buttonAuthorFollowText.value = "팔로잉"
        }else{
            buttonAuthorFollowBackground.value = R.drawable.button_radius2
            buttonAuthorFollowTextColor.value = R.color.first
            buttonAuthorFollowText.value = "팔로우"
        }

    }
}