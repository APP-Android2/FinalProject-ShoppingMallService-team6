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

    suspend fun getPieceAddSequence(): Int =  pieceAddInfoDataSource.getPieceAddSequence()

    suspend fun updatePieceAddSequence(pieceAddSequence: Int) = pieceAddInfoDataSource.updatePieceAddSequence(pieceAddSequence)

    suspend fun uploadImage(authorIdx: Int, imageUri: Uri): String  {
        return pieceAddInfoDataSource.uploadImage(authorIdx, imageUri)
    }

    suspend fun getPieceAddInfoImage(authorIdx: Int, addPieceImg: String) = pieceAddInfoDataSource.getPieceAddInfoImage(authorIdx, addPieceImg)
}