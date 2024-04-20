package kr.co.lion.unipiece.repository

import kr.co.lion.unipiece.db.remote.PieceInfoDataSource

class PieceInfoRepository {

    private val pieceInfoDataSource = PieceInfoDataSource()

    suspend fun getPopPieceInfo() = pieceInfoDataSource.getPopPieceInfo()
    suspend fun getPopPieceSort(category: String) = pieceInfoDataSource.getPopPieceSort(category)
    suspend fun getPopPieceDetailSort(detailCategory: String) = pieceInfoDataSource.getPopPieceDetailSort(detailCategory)

    suspend fun getNewPieceInfo() = pieceInfoDataSource.getNewPieceInfo()
    suspend fun getNewPieceSort(category: String) = pieceInfoDataSource.getNewPieceSort(category)
    suspend fun getNewPieceDetailSort(detailCategory: String) = pieceInfoDataSource.getNewPieceDetailSort(detailCategory)

    suspend fun getPieceInfoImg(pieceIdx: String, pieceImg: String) = pieceInfoDataSource.getPieceInfoImg(pieceIdx, pieceImg)

    suspend fun getIdxPieceInfo(pieceIdx: Int) = pieceInfoDataSource.getIdxPieceInfo(pieceIdx)

    suspend fun getAuthorPieceInfo(authorIdx: Int) = pieceInfoDataSource.getAuthorPieceInfo(authorIdx)

    suspend fun updatePieceLike(pieceIdx: Int, pieceLike: Int) = pieceInfoDataSource.updatePieceLike(pieceIdx, pieceLike)
}