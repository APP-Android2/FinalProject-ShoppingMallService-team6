package kr.co.lion.unipiece.ui.author.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.model.AuthorInfoData
import kr.co.lion.unipiece.repository.AuthorInfoRepository
import java.util.Date
import kotlin.math.abs

class ModifyAuthorInfoViewModel: ViewModel() {

    // 작가 정보
    private val _authorInfoData = MutableLiveData<AuthorInfoData>()
    val authorInfoData: LiveData<AuthorInfoData> = _authorInfoData

    private val authorInfoRepository = AuthorInfoRepository()


    // 작가 정보 갱신 여부 확인
    fun checkAuthorDate(): Boolean {
        val currentTime = Date()
        val oneYearInMillis: Long = 365 * 24 * 60 * 60 * 1000

        val authorDate = authorInfoData.value?.authorDate?.toDate()

        if (authorDate != null) {
            val timeDiff = abs(currentTime.time - authorDate.time)
            return timeDiff >= oneYearInMillis
        }

        return false
    }

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

    // 작가 이미지 업로드
    suspend fun uploadImage(authorIdx: Int, imageUri: Uri): Boolean{
        return authorInfoRepository.uploadImage(authorIdx, imageUri)
    }


    // 작가 정보 수정
    suspend fun updateAuthorInfo(){
        authorInfoRepository.updateAuthorInfoData(authorInfoData.value!!)
    }
}