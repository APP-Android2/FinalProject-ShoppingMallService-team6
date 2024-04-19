package kr.co.lion.unipiece.repository

import kr.co.lion.unipiece.db.remote.NewsInfoDataSource

class NewsInfoRepository {

    private val newsInfoDataSource = NewsInfoDataSource()

    suspend fun getNewsImage() = newsInfoDataSource.getNewsImage()

    suspend fun getNewsInfoByImage(newsImg:String) = newsInfoDataSource.getNewsInfoByImage(newsImg)

    suspend fun getNewsDataByDate() = newsInfoDataSource.getNewsDataByDate()

    suspend fun getNewsInfoImg(image:String) = newsInfoDataSource.getNewsInfoImg(image)

}