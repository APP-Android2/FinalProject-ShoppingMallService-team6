package kr.co.lion.unipiece.ui.mypage.viewmodel

import androidx.lifecycle.ViewModel
import kr.co.lion.unipiece.repository.AuthorInfoRepository
import kr.co.lion.unipiece.repository.UserInfoRepository

class MyPageViewModel: ViewModel() {

    private val userInfoRepository = UserInfoRepository()
    private val authorInfoRepository = AuthorInfoRepository()

    suspend fun getUserNickname(userIdx: Int):String?{
        return userInfoRepository.getUserDataByIdx(userIdx)?.nickName
    }

    suspend fun isAuthor(userIdx: Int):Boolean{
        return authorInfoRepository.isAuthor(userIdx)
    }
}