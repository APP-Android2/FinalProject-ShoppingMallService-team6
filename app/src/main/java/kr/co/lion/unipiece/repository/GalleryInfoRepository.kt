package kr.co.lion.unipiece.repository

import kr.co.lion.unipiece.db.remote.GalleryInfoDataSource

class GalleryInfoRepository {

    private val galleryInfoDataSource = GalleryInfoDataSource()


    suspend fun getGalleryImg() = galleryInfoDataSource.getGalleryImg()

    suspend fun getGalleryByImage(galleryImg:String) = galleryInfoDataSource.getGalleryByImage(galleryImg)


    suspend fun getGalleryDataByDate() = galleryInfoDataSource.getGalleryDataByDate()

    suspend fun getGalleryInfoImg(image:String) = galleryInfoDataSource.getGalleryInfoImg(image)

}