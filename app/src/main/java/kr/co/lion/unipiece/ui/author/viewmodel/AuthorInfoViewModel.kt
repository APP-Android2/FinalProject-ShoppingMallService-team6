package kr.co.lion.unipiece.ui.author.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.model.AuthorInfoData
import kr.co.lion.unipiece.model.PieceInfoData
import kr.co.lion.unipiece.repository.AuthorInfoRepository
import kr.co.lion.unipiece.repository.PieceInfoRepository

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

    // 작가 본인 여부
    private val _authorIsMe = MutableLiveData<Boolean>()
    val authorIsMe:LiveData<Boolean> = _authorIsMe

    // 작가 작품 리스트
    private val _authorPieces = MutableLiveData<List<PieceInfoData>>()
    val authorPieces = _authorPieces

    //작가 정보 리스트
    private val _authorInfoDataList = MutableLiveData<List<AuthorInfoData>>()
    var authorInfoDataList: LiveData<List<AuthorInfoData>> = _authorInfoDataList

    private val authorInfoRepository = AuthorInfoRepository()

    // 작가 정보를 불러오기
    suspend fun getAuthorInfoData(authorIdx: Int) {
        val job1 = viewModelScope.launch {
            // 작가 정보 객체 셋팅
            _authorInfoData.value = authorInfoRepository.getAuthorInfoDataByIdx(authorIdx)
        }
        job1.join()
    }

    // 본인이 해당 작가인지 여부 확인
    fun checkAuthor(userIdx: Int){
        _authorIsMe.value = authorInfoData.value?.userIdx == userIdx
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
    suspend fun getAuthorPieces(authorIdx:Int){
        val pieceInfoRepository = PieceInfoRepository()
        val response = pieceInfoRepository.getAuthorPieceInfo(authorIdx)
        val pieceInfoList = mutableListOf<PieceInfoData>()

        response.forEach { pieceInfoData ->
            val pieceImgUrl = pieceInfoRepository.getPieceInfoImg(pieceInfoData.pieceIdx.toString(), pieceInfoData.pieceImg)
            val finalPieceInfoData = pieceInfoData.copy(pieceImg = pieceImgUrl ?: pieceInfoData.pieceImg)
            pieceInfoList.add(finalPieceInfoData)
        }

        _authorPieces.value = pieceInfoList
    }

    // 작가의 이미지 URL 가져오기
    suspend fun getAuthorInfoImg(authorImg:String):String?{
        return authorInfoRepository.getAuthorInfoImg(authorImg)
    }


    //작가 리스트 가져오기
    suspend fun getAuthorInfoAll(){
        val authorInfo = authorInfoRepository.getAuthorInfoAll()
        _authorInfoDataList.value = authorInfo
    }

}