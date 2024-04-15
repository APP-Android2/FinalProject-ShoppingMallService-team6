package kr.co.lion.unipiece.repository

import kr.co.lion.unipiece.db.remote.PieceInfoDataSource

class PieceInfoRepository {

    private val pieceInfoDataSource = PieceInfoDataSource()

    suspend fun getPopPieceInfo() = pieceInfoDataSource.getPopPieceInfo()

    suspend fun getPopPieceInfoImg(pieceIdx: String, pieceImg: String) = pieceInfoDataSource.getPieceInfoImg(pieceIdx, pieceImg)
}