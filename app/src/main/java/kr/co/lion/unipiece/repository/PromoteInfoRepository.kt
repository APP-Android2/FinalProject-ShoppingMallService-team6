package kr.co.lion.unipiece.repository

import kr.co.lion.unipiece.db.remote.PromoteInfoDataSource

class PromoteInfoRepository {

    private val promoteInfoDataSource = PromoteInfoDataSource()

    suspend fun getPromoteImage() = promoteInfoDataSource.getPromoteImage()

    suspend fun gettingPromoteInfoByImage(promoteImg:String) = promoteInfoDataSource.gettingPromoteInfoByImage(promoteImg)

    suspend fun gettingDataByDate() = promoteInfoDataSource.gettingDataByDate()

    suspend fun getPromoteInfoImg(image:String) = promoteInfoDataSource.getPromoteInfoImg(image)
}