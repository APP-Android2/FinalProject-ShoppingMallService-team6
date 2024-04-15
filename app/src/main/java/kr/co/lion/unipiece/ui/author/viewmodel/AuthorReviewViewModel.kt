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

    // 댓글 내용
    val authorReviewContent = MutableLiveData<String>()

    // 리뷰 시퀀스 값 가져오기
    suspend fun getReviewSequence(): Int {
        var sequence = 0
        val job1 = viewModelScope.launch {
            sequence = authorReviewRepository.getReviewSequence()
        }
        job1.join()
        return sequence
    }

    // 리뷰 시퀀스 값 업데이트
    suspend fun updateReviewSequence(reviewSequence: Int){
        val job1 = viewModelScope.launch {
            authorReviewRepository.updateReviewSequence(reviewSequence+1)
        }
        job1.join()
    }
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