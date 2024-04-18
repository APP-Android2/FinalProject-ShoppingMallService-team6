package kr.co.lion.unipiece.repository

import kr.co.lion.unipiece.db.remote.AuthorReviewDataSource
import kr.co.lion.unipiece.model.AuthorReviewData

class AuthorReviewRepository {
    private val authorReviewDataSource = AuthorReviewDataSource()

    // 리뷰 시퀀스값을 가져온다.
    suspend fun getReviewSequence() =
        authorReviewDataSource.getReviewSequence()

    // 리뷰 시퀀스 값을 업데이트 한다.
    suspend fun updateReviewSequence(reviewSequence: Int) =
        authorReviewDataSource.updateReviewSequence(reviewSequence)

    // 리뷰를 저장한다.
    suspend fun insertReviewData(authorReviewData: AuthorReviewData) =
        authorReviewDataSource.insertReviewData(authorReviewData)

    // 작가idx를 통해 리뷰를 가져와 반환한다
    suspend fun getAuthorReviewDataByIdx(authorIdx:Int) : List<AuthorReviewData> =
        authorReviewDataSource.getAuthorReviewDataByIdx(authorIdx)

    // 리뷰idx를 통해 리뷰를 삭제한다.
    suspend fun deleteReview(reviewIdx:Int) =
        authorReviewDataSource.deleteReview(reviewIdx)

}