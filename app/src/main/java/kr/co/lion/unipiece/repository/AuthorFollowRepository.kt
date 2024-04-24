package kr.co.lion.unipiece.repository

import kr.co.lion.unipiece.db.remote.AuthorFollowDataSource

class AuthorFollowRepository {
    private val authorFollowDataSource = AuthorFollowDataSource()

    // 작가 팔로우 수를 가져오는 메서드
    suspend fun getAuthorFollow(authorIdx: Int) = authorFollowDataSource.getFollowCount(authorIdx)

    // 해당 작가 팔로우 여부 체크
    suspend fun checkFollow(userIdx:Int, authorIdx:Int) = authorFollowDataSource.checkFollow(userIdx, authorIdx)

    // 작가 팔로우 추가
    suspend fun addAuthorFollow(userIdx:Int, authorIdx:Int) = authorFollowDataSource.addAuthorFollow(userIdx, authorIdx)

    // 작가 팔로우 취소
    suspend fun cancelFollowing(userIdx:Int, authorIdx:Int) = authorFollowDataSource.cancelFollowing(userIdx, authorIdx)
}