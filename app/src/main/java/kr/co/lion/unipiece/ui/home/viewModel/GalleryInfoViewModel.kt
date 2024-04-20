package kr.co.lion.unipiece.ui.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.co.lion.unipiece.model.GalleryInfoData
import kr.co.lion.unipiece.repository.GalleryInfoRepository

class GalleryInfoViewModel : ViewModel() {

    private val galleryInfoRepository = GalleryInfoRepository()

    private val _galleryInfoList = MutableLiveData<List<GalleryInfoData>>()
    var galleryInfoList: LiveData<List<GalleryInfoData>> = _galleryInfoList




}