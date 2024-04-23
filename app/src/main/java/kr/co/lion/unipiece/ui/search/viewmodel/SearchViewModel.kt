package kr.co.lion.unipiece.ui.search.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.co.lion.unipiece.model.AuthorInfoData
import kr.co.lion.unipiece.model.PieceInfoData
import kr.co.lion.unipiece.model.SearchResultData
import kr.co.lion.unipiece.repository.AuthorInfoRepository
import kr.co.lion.unipiece.repository.PieceInfoRepository
import kr.co.lion.unipiece.ui.search.adapter.SearchResultViewType
import kr.co.lion.unipiece.ui.search.adapter.SearchResultViewType.*

class SearchViewModel() : ViewModel() {
    private val pieceInfoRepository = PieceInfoRepository()

    private val authorInfoRepository = AuthorInfoRepository()

    private val _authorList = MutableLiveData<List<AuthorInfoData>>()
    val authorList: LiveData<List<AuthorInfoData>> = _authorList

    private val _pieceList = MutableLiveData<List<PieceInfoData>>()
    val pieceList: LiveData<List<PieceInfoData>> = _pieceList

    private val _searchList = MutableLiveData<List<SearchResultData>>()
    val searchList: LiveData<List<SearchResultData>> = _searchList

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun search(searchText: String, callback: (Boolean) -> Unit) =
        viewModelScope.launch {

           searchAuthor(searchText)
            searchPiece(searchText)

            val recyclerviewList =  mutableListOf<SearchResultData>()

            val success = try {
                    authorList.value?.let { list ->
                        if (list.isNotEmpty()) {
                            recyclerviewList.add(SearchResultData(PieceInfoData(), AuthorInfoData(), AUTHOR_TITLE))
                            recyclerviewList.add(SearchResultData(PieceInfoData(), AuthorInfoData(), AUTHOR_TITLE, false))
                        }
                        list.map {
                            recyclerviewList.add(SearchResultData(PieceInfoData(), it, AUTHOR_CONTENT))
                        }
                    }

                    val authorCount = recyclerviewList.filter { it.viewType == AUTHOR_CONTENT }.count() % 2

                    pieceList.value?.let { list ->
                        if (list.isNotEmpty()) {
                            if(authorCount == 0){
                                recyclerviewList.add(SearchResultData(PieceInfoData(), AuthorInfoData(), PIECE_TITLE))
                                recyclerviewList.add(SearchResultData(PieceInfoData(), AuthorInfoData(), PIECE_TITLE, false))
                            } else {
                                recyclerviewList.add(SearchResultData(PieceInfoData(), AuthorInfoData(), PIECE_TITLE, false))
                                recyclerviewList.add(SearchResultData(PieceInfoData(), AuthorInfoData(), PIECE_TITLE, true))
                                recyclerviewList.add(SearchResultData(PieceInfoData(), AuthorInfoData(), PIECE_TITLE, false))
                            }
                        }
                        list.map {
                            recyclerviewList.add(SearchResultData(it, AuthorInfoData(), PIECE_CONTENT))
                        }
                    }

                    _searchList.value = recyclerviewList

                    Log.d("search", _searchList.value.toString())
                    true

                } catch (e: Exception) {
                    Log.e("Firebase Error", "searchAuthor : ${e.message}")
                    false
                }

            callback(success)
        }

    fun setLoading(loading: Boolean) = viewModelScope.launch {
        _loading.value = loading
    }

    suspend fun searchAuthor(searchAuthor: String)  {
        withContext(Dispatchers.IO){
            val response = authorInfoRepository.searchAuthor(searchAuthor)
            val updateAuthorInfoList = updateImageAuthor(response)
            _authorList.postValue(updateAuthorInfoList)
            Log.d("search author", _authorList.value.toString())
        }
    }

    suspend fun searchPiece(searchPiece: String) {
        withContext(Dispatchers.IO) {
            val response = pieceInfoRepository.searchPiece(searchPiece)
            val updatePieceInfoList = updateImagePieceInfo(response)
            _pieceList.postValue(updatePieceInfoList)
            Log.d("search piece", _pieceList.value.toString())
        }
    }

    suspend fun updateImagePieceInfo(pieceInfoList: List<PieceInfoData>): List<PieceInfoData> {
        return pieceInfoList.map { pieceInfoData ->
            val pieceImgUrl = getPieceImg(pieceInfoData.pieceIdx.toString(), pieceInfoData.pieceImg)
            pieceInfoData.copy(pieceImg = pieceImgUrl ?: pieceInfoData.pieceImg)
        }
    }

    suspend fun updateImageAuthor(authorInfoList: List<AuthorInfoData>): List<AuthorInfoData> {
        return authorInfoList.map { authorInfoData ->
            val authorImgUrl = getAuthorImg(authorInfoData.authorImg)
            authorInfoData.copy(authorImg = authorImgUrl ?: authorInfoData.authorImg)
        }
    }

    private suspend fun getPieceImg(pieceIdx: String, pieceImg: String): String? {
        return pieceInfoRepository.getPieceInfoImg(pieceIdx, pieceImg)
    }


    private suspend fun getAuthorImg(authorImg: String): String? {
        return authorInfoRepository.getAuthorInfoImg(authorImg)
    }
}