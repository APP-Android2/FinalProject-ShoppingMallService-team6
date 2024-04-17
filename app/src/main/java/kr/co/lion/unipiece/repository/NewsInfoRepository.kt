package kr.co.lion.unipiece.repository

import kr.co.lion.unipiece.db.remote.NewsInfoDataSource

class NewsInfoRepository {

    private val newsInfoDataSource = NewsInfoDataSource()

    suspend fun getNewsImage() = newsInfoDataSource.getNewsImage()

}