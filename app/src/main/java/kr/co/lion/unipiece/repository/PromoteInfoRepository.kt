package kr.co.lion.unipiece.repository

import kr.co.lion.unipiece.db.remote.PromoteInfoDataSource

class PromoteInfoRepository {

    private val promoteInfoDataSource = PromoteInfoDataSource()

    suspend fun getPromoteImage() = promoteInfoDataSource.getPromoteImage()

}