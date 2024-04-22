package kr.co.lion.unipiece.ui.rank.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.model.AuthorInfoData
import kr.co.lion.unipiece.model.PieceInfoData
import kr.co.lion.unipiece.repository.AuthorInfoRepository
import kr.co.lion.unipiece.repository.PieceInfoRepository

class RankViewModel(): ViewModel() {

    private val pieceInfoRepository = PieceInfoRepository()

    private val authorInfoRepository = AuthorInfoRepository()

    private val _pieceRankList = MutableLiveData<List<PieceInfoData>>()
    val pieceRankList : LiveData<List<PieceInfoData>> = _pieceRankList

    private val _authorSaleList= MutableLiveData<List<AuthorInfoData>>()
    val authorSaleList : LiveData<List<AuthorInfoData>> = _authorSaleList

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading

    fun setLoading(check: Boolean){
        _loading.value = check
    }

    suspend fun getPopPieceInfo(){
        val response = pieceInfoRepository.getPopPieceInfo()
        val updatedPieceInfoList = updateImagePieceInfo(response)

        _pieceRankList.value = updatedPieceInfoList
    }

    suspend fun getAuthorInfoSale(){
        val response = authorInfoRepository.getAuthorInfoSale()
        val updateAuthorInfoList = updateImageAuthor(response)

        _authorSaleList.value = updateAuthorInfoList
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