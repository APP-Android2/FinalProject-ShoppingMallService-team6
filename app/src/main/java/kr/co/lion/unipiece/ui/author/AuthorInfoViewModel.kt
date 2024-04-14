package kr.co.lion.unipiece.ui.author

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.model.AuthorInfoData
import kr.co.lion.unipiece.repository.AuthorInfoRepository

class AuthorInfoViewModel: ViewModel() {
    // 작가 정보
    private val _authorInfoData = MutableLiveData<AuthorInfoData>()
    val authorInfoData:LiveData<AuthorInfoData> = _authorInfoData

    // 작가 팔로우 수
    private val _authorFollow = MutableLiveData<String>()
    val authorFollow:LiveData<String> = _authorFollow

    // 팔로우 여부
    private val _checkFollow = MutableLiveData<Boolean>()
    val checkFollow:LiveData<Boolean> = _checkFollow

    private val authorInfoRepository = AuthorInfoRepository()

    // 작가 정보를 불러오기
    suspend fun getAuthorInfoData(authorIdx: Int) {
        val job1 = viewModelScope.launch {
            // 작가 정보 객체 셋팅
            _authorInfoData.value = authorInfoRepository.getAuthorInfoDataByIdx(authorIdx)
        }
        job1.join()
    }

    // 팔로우 수 불러오기
    fun getFollowCount(authorIdx: Int){
        viewModelScope.launch {
            val followCount = authorInfoRepository.getAuthorFollow(authorIdx)
            _authorFollow.value = "${followCount} 팔로워"
        }
    }

    // 팔로우 여부 확인
    fun checkFollow(userIdx:Int, authorIdx:Int){
        viewModelScope.launch {
            _checkFollow.value = authorInfoRepository.checkFollow(userIdx, authorIdx)
        }
    }

    // 팔로우 하기
    suspend fun followAuthor(userIdx:Int, authorIdx:Int){
        val job1 = viewModelScope.launch {
            authorInfoRepository.addAuthorFollow(userIdx, authorIdx)
        }
        job1.join()
    }
    // 팔로우 취소
    suspend fun cancelFollowing(userIdx:Int, authorIdx:Int){
        val job1 = viewModelScope.launch {
            authorInfoRepository.cancelFollowing(userIdx, authorIdx)
        }
        job1.join()
    }

    // 작가의 작품 리스트 불러오기
    fun getPiecesList(authorIdx:Int){

    }
}