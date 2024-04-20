package kr.co.lion.unipiece.ui.mypage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.co.lion.unipiece.model.VisitAddData
import kr.co.lion.unipiece.repository.VisitAddRepository

class VisitGalleryViewModel: ViewModel() {
    private val visitAddRepository = VisitAddRepository()

    private val _visitGalleryList = MutableLiveData<List<VisitAddData>>()
    val visitGalleryList: LiveData<List<VisitAddData>> = _visitGalleryList

    // 전시실 방문 신청 목록 불러오기
    suspend fun getVisitAddList(userIdx:Int){
        _visitGalleryList.value = visitAddRepository.getVisitAddList(userIdx)
    }
}