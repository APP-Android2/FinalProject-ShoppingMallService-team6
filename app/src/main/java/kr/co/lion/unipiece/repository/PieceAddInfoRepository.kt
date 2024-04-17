package kr.co.lion.unipiece.repository

import android.net.Uri
import kr.co.lion.unipiece.db.remote.PieceAddInfoDataSource
import kr.co.lion.unipiece.model.PieceAddInfoData

class PieceAddInfoRepository {
    private val pieceAddInfoDataSource = PieceAddInfoDataSource()

    suspend fun addPieceInfo(pieceAddInfoData: PieceAddInfoData): Boolean {
        return pieceAddInfoDataSource.addPieceInfo(pieceAddInfoData)
    }

    suspend fun getPieceAddInfo(authorIdx: Int) = pieceAddInfoDataSource.getPieceAddInfo(authorIdx)

    suspend fun uploadImage(imageUri: Uri): String  {
        return pieceAddInfoDataSource.uploadImage(imageUri)
    }

    suspend fun getPieceAddInfoImage(addPieceImg: String) = pieceAddInfoDataSource.getPieceAddInfoImage(addPieceImg)
}