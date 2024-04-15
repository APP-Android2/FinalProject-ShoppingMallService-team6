package kr.co.lion.unipiece.ui.author.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.model.AuthorReviewData
import kr.co.lion.unipiece.repository.AuthorReviewRepository

class AuthorReviewViewModel : ViewModel() {
    private val authorReviewRepository = AuthorReviewRepository()

    private val _authorReviewList = MutableLiveData<List<AuthorReviewData>>()
    val authorReviewList: LiveData<List<AuthorReviewData>> = _authorReviewList

    // 리뷰 등록
    suspend fun insertReviewData(authorReviewData: AuthorReviewData){
        val job1 = viewModelScope.launch {
            authorReviewRepository.insertReviewData(authorReviewData)
        }
        job1.join()
    }

    // 리뷰 불러오기
    suspend fun getReviewList(authorIdx:Int){
        val job1 = viewModelScope.launch {
            _authorReviewList.value = authorReviewRepository.getAuthorReviewDataByIdx(authorIdx)
        }
        job1.join()
    }

    // 리뷰 삭제
    suspend fun deleteReview(reviewIdx:Int){
        val job1 = viewModelScope.launch {
            authorReviewRepository.deleteReview(reviewIdx)
        }
    }
}