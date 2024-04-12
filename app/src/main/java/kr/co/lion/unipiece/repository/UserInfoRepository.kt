package kr.co.lion.unipiece.repository

import kr.co.lion.unipiece.db.remote.UserInfoDataSource
import kr.co.lion.unipiece.model.UserInfoData

class UserInfoRepository {

    private val userInfoDataSource = UserInfoDataSource()

    suspend fun getUserSequence() = userInfoDataSource.getUserSequence()

    suspend fun updateUserSequence(userSequence: Int) = userInfoDataSource.updateUserSequence(userSequence)

    suspend fun insertUserData(userInfoData: UserInfoData) = userInfoDataSource.insetUserData(userInfoData)

    suspend fun checkUserId(userId:String):Boolean = userInfoDataSource.checkUserId(userId)

    suspend fun getUserDataByIdx(userIdx:Int) = userInfoDataSource.getUserDataByIdx(userIdx)

}