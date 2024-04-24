package kr.co.lion.unipiece.repository

import android.net.Uri
import kr.co.lion.unipiece.db.remote.PieceBuyInfoDataSource
import kr.co.lion.unipiece.model.PieceBuyInfoData

class PieceBuyInfoRepository {
    private val pieceBuyInfoDataSource = PieceBuyInfoDataSource()

    suspend fun getPieceBuyInfo(userIdx: Int): List<PieceBuyInfoData> {
        return pieceBuyInfoDataSource.getPieceBuyInfo(userIdx)
    }

    suspend fun getPieceBuyInfoByPieceBuyIdx(pieceBuyIdx: Int): PieceBuyInfoData? {
        return pieceBuyInfoDataSource.getPieceBuyInfoByPieceBuyIdx(pieceBuyIdx)
    }

    suspend fun updatePieceBuyCancel(pieceBuyInfoData: PieceBuyInfoData): Boolean {
        return pieceBuyInfoDataSource.updatePieceBuyCancel(pieceBuyInfoData)
    }

    suspend fun updatePieceBuyRefund(pieceBuyInfoData: PieceBuyInfoData): Boolean {
        return pieceBuyInfoDataSource.updatePieceBuyRefund(pieceBuyInfoData)
    }

    suspend fun uploadRefundImage(pieceBuyIdx: Int, imageUri: Uri): String {
        return pieceBuyInfoDataSource.uploadRefundImage(pieceBuyIdx, imageUri)
    }
}