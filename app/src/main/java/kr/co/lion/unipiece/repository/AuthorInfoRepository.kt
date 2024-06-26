package kr.co.lion.unipiece.repository

import android.content.Context
import android.net.Uri
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

    // 작가 이미지 url 받아오기
    suspend fun getAuthorInfoImg(authorImg:String):String? = authorInfoDataSource.getAuthorInfoImg(authorImg)

    // 작가 정보를 수정하는 메서드
    suspend fun updateAuthorInfoData(authorInfoData: AuthorInfoData) = authorInfoDataSource.updateAuthorInfoData(authorInfoData)

    // 작가 확인
    suspend fun isAuthor(userIdx: Int) = authorInfoDataSource.isAuthor(userIdx)

    // 작가 이미지 url 작가 idx로 받아오기
    suspend fun getAuthorInfoImg(authorIdx: Int) = authorInfoDataSource.getAuthorIdxImg(authorIdx)

    // 작가 이미지 파일 업로드
    suspend fun uploadImage(authorIdx: Int, imageUri: Uri): Boolean = authorInfoDataSource.uploadImage(authorIdx, imageUri)

    // 판매횟수 순서대로 작가 정보 가져오기
    suspend fun getAuthorInfoSale():List<AuthorInfoData> = authorInfoDataSource.getAuthorInfoSale()

    // 팔로워순대로 작가 정보 가져오기
    suspend fun getAuthorInfoFollow():List<AuthorInfoData> = authorInfoDataSource.getAuthorInfoFollow()

    suspend fun updateAuthorFollow(authorIdx: Int, authorFollow: Int) = authorInfoDataSource.updateAuthorFollow(authorIdx, authorFollow)

    suspend fun searchAuthor(authorName: String) = authorInfoDataSource.searchAuthor(authorName)

    suspend fun getAuthorInfoByAuthorIdxs(authorIdxs: List<Int>) = authorInfoDataSource.getAuthorInfoByAuthorIdxs(authorIdxs)

    // 작가 이미지 url 받아오기
    suspend fun getAuthorInfoImg(authorIdx: String,authorImg:String) = authorInfoDataSource.getAuthorInfoImg(authorIdx,authorImg)

}