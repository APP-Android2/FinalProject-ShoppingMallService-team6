package kr.co.lion.unipiece.model

import com.google.firebase.Timestamp

data class GalleryInfoData (
    var galleryInfoName:String = "",
    var galleryInfoDate:String = "",
    var galleryInfoContent:String = "",
    var galleryInfoTime:Timestamp = Timestamp.now(),
    var galleryInfoImg:String = "",
    var galleryInfoAuthor:String = ""
)