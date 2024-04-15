package kr.co.lion.unipiece.repository

import kr.co.lion.unipiece.db.remote.PieceInfoDataSource

class PieceInfoRepository {

    private val pieceInfoDataSource = PieceInfoDataSource()

    suspend fun getPopPieceInfo() = pieceInfoDataSource.getPopPieceInfo()
    suspend fun getPopPieceSort(category: String) = pieceInfoDataSource.getPopPieceSort(category)
    suspend fun getPopPieceDetailSort(detailCategory: String) = pieceInfoDataSource.getPopPieceDetailSort(detailCategory)

    suspend fun getNewPieceInfo() = pieceInfoDataSource.getNewPieceInfo()

    suspend fun getPieceInfoImg(pieceIdx: String, pieceImg: String) = pieceInfoDataSource.getPieceInfoImg(pieceIdx, pieceImg)
}