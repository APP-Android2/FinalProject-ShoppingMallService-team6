package kr.co.lion.unipiece.ui.mypage.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.model.AuthorInfoData
import kr.co.lion.unipiece.repository.AuthorFollowRepository
import kr.co.lion.unipiece.repository.AuthorInfoRepository

class FollowViewModel : ViewModel() {

    private val authorFollowRepository = AuthorFollowRepository()
    private val authorInfoRepository = AuthorInfoRepository()

    // 유저 Idx 찾기
    val userIdx = UniPieceApplication.prefs.getUserIdx("userIdx", 0)

    // userIdx로 가져온 작가 리스트
    private val _userIdxAuthorInfoDataList = MutableLiveData<List<AuthorInfoData>>()
    val userIdxAuthorInfoDataList: LiveData<List<AuthorInfoData>> = _userIdxAuthorInfoDataList

    // userIdx로 팔로우한 작가 리스트 가져오기 로딩
    private val _userIdxAuthorInfoDataLoading = MutableLiveData<Boolean?>(null)
    val userIdxAuthorInfoDataLoading : LiveData<Boolean?> = _userIdxAuthorInfoDataLoading

    // 팔로우 취소 로딩
    private val _cancelFollowingLoading = MutableLiveData<Boolean?>(null)
    val cancelFollowingLoading : LiveData<Boolean?> = _cancelFollowingLoading

    init {
        viewModelScope.launch {
            getFollowDataByUserIdx(userIdx)
        }
    }

    fun userIdxAuthorInfoDataLoading() {
        _userIdxAuthorInfoDataLoading.value = null
    }

    fun cancelFollowingLoading(){
        _cancelFollowingLoading.value = null
    }


    // userIdx로 내가 팔로우한 작가들의 정보를 들고온다.
    fun getFollowDataByUserIdx(userIdx: Int) = viewModelScope.launch {
        try {
            val authorIdxs = authorFollowRepository.getAuthorIdxsByUserIdx(userIdx)
            val response = authorInfoRepository.getAuthorInfoByAuthorIdxs(authorIdxs)
            val updateAuthorInfoList = updateImageAuthorInfo(response)
            _userIdxAuthorInfoDataList.value = updateAuthorInfoList
            _userIdxAuthorInfoDataLoading.value = true
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error vmGetFollowDataByUserIdx : ${e.message}")
        }

    }


    // 팔로우 취소
    suspend fun cancelFollowing(userIdx: Int, authorIdx: Int) {
        val job1 = viewModelScope.launch {
            authorFollowRepository.cancelFollowing(userIdx, authorIdx)
            _cancelFollowingLoading.value = true
        }
        job1.join()
    }

    suspend fun updateImageAuthorInfo(authorInfoList: List<AuthorInfoData>): List<AuthorInfoData> {
        return authorInfoList.map { authorInfoData ->
            val authorImgUrl = getPieceImg(authorInfoData.authorIdx.toString(), authorInfoData.authorImg)
            authorInfoData.copy(authorImg = authorImgUrl ?: authorInfoData.authorImg)
        }
    }

    private suspend fun getPieceImg(authorIdx: String, authorImg: String): String? {
        return authorInfoRepository.getAuthorInfoImg(authorIdx, authorImg)
    }
}