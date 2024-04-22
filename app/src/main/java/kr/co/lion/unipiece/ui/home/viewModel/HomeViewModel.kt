package kr.co.lion.unipiece.ui.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.co.lion.unipiece.model.AuthorInfoData
import kr.co.lion.unipiece.repository.AuthorInfoRepository

class HomeViewModel : ViewModel() {

    //작가 정보 리스트
    private val _authorInfoDataList = MutableLiveData<List<AuthorInfoData>>()
    var authorInfoDataList: LiveData<List<AuthorInfoData>> = _authorInfoDataList

    private val authorInfoRepository = AuthorInfoRepository()


    //작가 리스트 가져오기
    suspend fun getAuthorInfoAll(){
        val authorInfo = authorInfoRepository.getAuthorInfoAll()
        _authorInfoDataList.value = authorInfo
    }

}