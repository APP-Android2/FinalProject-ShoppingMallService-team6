package kr.co.lion.unipiece.ui.author.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.model.AuthorInfoData
import kr.co.lion.unipiece.repository.AuthorInfoRepository

class ModifyAuthorInfoViewModel: ViewModel() {
    // 작가 정보
    private val _authorInfoData = MutableLiveData<AuthorInfoData>()
    val authorInfoData: LiveData<AuthorInfoData> = _authorInfoData

    private val authorInfoRepository = AuthorInfoRepository()

    // 작가 정보를 불러오기
    suspend fun getAuthorInfoData(authorIdx: Int) {
        val job1 = viewModelScope.launch {
            // 작가 정보 객체 셋팅
            _authorInfoData.value = authorInfoRepository.getAuthorInfoDataByIdx(authorIdx)
        }
        job1.join()
    }

    // 작가의 이미지 URL 가져오기
    suspend fun getAuthorInfoImg(authorImg:String):String?{
        return authorInfoRepository.getAuthorInfoImg(authorImg)
    }

    // 작가 이미지 셋팅


    // 작가 정보 수정
    suspend fun updateAuthorInfo(){
        authorInfoRepository.updateAuthorInfoData(authorInfoData.value!!)
    }
}