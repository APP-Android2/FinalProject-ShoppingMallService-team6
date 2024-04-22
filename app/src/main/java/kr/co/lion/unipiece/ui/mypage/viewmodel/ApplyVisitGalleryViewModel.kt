package kr.co.lion.unipiece.ui.mypage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.co.lion.unipiece.model.VisitAddData
import kr.co.lion.unipiece.repository.VisitAddRepository

class ApplyVisitGalleryViewModel: ViewModel() {
    private val visitAddRepository = VisitAddRepository()

    private val _visitAddData = MutableLiveData<VisitAddData?>()
    val visitAddData: LiveData<VisitAddData?> = _visitAddData

    // 전시실 방문 신청 시퀀스 불러오기
    suspend fun getVisitAddSequence():Int{
        return visitAddRepository.getVisitAddSequence()
    }

    // 전시실 방문 신청 시퀀스 업데이트
    suspend fun updateVisitAddSequence(visitAddSequence:Int):Boolean{
        return visitAddRepository.updateVisitAddSequence(visitAddSequence)
    }

    // 전시실 방문 신청 빈데이터 셋팅
    fun setEmptyData(visitAddData: VisitAddData){
        _visitAddData.value = visitAddData
    }

    // 전시실 방문 신청 데이터 불러오기
    suspend fun getVisitAddData(visitIdx:Int):Boolean{
        val result = visitAddRepository.getVisitAddDataByIdx(visitIdx)
        return if(result != null){
            _visitAddData.value = result
            true
        }else{
            false
        }
    }

    // 전시실 방문 신청 등록
    suspend fun insertVisitAddData(visitAddData: VisitAddData):Boolean{
        return visitAddRepository.insertVisitAddData(visitAddData)
    }

    // 전시실 방문 신청 수정
    suspend fun updateVisitAddData(visitAddData: VisitAddData):Boolean{
        return visitAddRepository.updateVisitAddData(visitAddData)
    }

    fun isEmpty():Boolean{
        return _visitAddData.value == null
    }
}