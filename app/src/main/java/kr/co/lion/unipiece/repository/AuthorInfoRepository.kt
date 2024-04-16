package kr.co.lion.unipiece.repository

import kr.co.lion.unipiece.db.remote.AuthorInfoDataSource
import kr.co.lion.unipiece.model.AuthorInfoData

class AuthorInfoRepository {
    private val authorInfoDataSource = AuthorInfoDataSource()

    // 작가 번호 시퀀스값을 가져온다.
    suspend fun getAuthorSequence() = authorInfoDataSource.getAuthorSequence()

    // 작가 시퀀스 값을 업데이트 한다.
    suspend fun updateAuthorSequence(authorSequence: Int) = authorInfoDataSource.updateAuthorSequence(authorSequence)

    // 작가 정보를 저장한다.
    suspend fun insertAuthorInfoData(authorInfoData: AuthorInfoData) = authorInfoDataSource.insertAuthorInfoData(authorInfoData)

    // 작가 번호를 통해 작가 정보를 가져와 반환한다
    suspend fun getAuthorInfoDataByIdx(authorIdx:Int) = authorInfoDataSource.getAuthorInfoDataByIdx(authorIdx)

    // userIdx로 작가idx를 가져와 반환한다
    suspend fun getAuthorIdxByUserIdx(userIdx:Int) = authorInfoDataSource.getAuthorIdxByUserIdx(userIdx)

    // 모든 작가의 정보를 가져온다.
    suspend fun getAuthorInfoAll():MutableList<AuthorInfoData> = authorInfoDataSource.getAuthorInfoAll()

    // 작가 정보를 수정하는 메서드
    suspend fun updateAuthorInfoData(authorInfoData: AuthorInfoData) = authorInfoDataSource.updateAuthorInfoData(authorInfoData)

    // 작가 팔로우 수를 가져오는 메서드
    suspend fun getAuthorFollow(authorIdx: Int) = authorInfoDataSource.getFollowCount(authorIdx)

    // 해당 작가 팔로우 여부 체크
    suspend fun checkFollow(userIdx:Int, authorIdx:Int) = authorInfoDataSource.checkFollow(userIdx, authorIdx)

    // 작가 팔로우 추가
    suspend fun addAuthorFollow(userIdx:Int, authorIdx:Int) = authorInfoDataSource.addAuthorFollow(userIdx, authorIdx)

    // 작가 팔로우 취소
    suspend fun cancelFollowing(userIdx:Int, authorIdx:Int) = authorInfoDataSource.cancelFollowing(userIdx, authorIdx)


}