package kr.co.lion.unipiece.repository

import kr.co.lion.unipiece.db.remote.LikePieceDataSource

class LikePieceRepository {

    private val likePieceDataSource = LikePieceDataSource()

    suspend fun addLikePiece(pieceIdx: Int, userIdx: Int) = likePieceDataSource.addLikePiece(pieceIdx, userIdx)

    suspend fun cancelLikePiece(pieceIdx: Int, userIdx: Int) = likePieceDataSource.cancelLikePiece(pieceIdx, userIdx)

    suspend fun countLikePiece(pieceIdx: Int) = likePieceDataSource.countLikePiece(pieceIdx)

    suspend fun isLikePiece(pieceIdx: Int, userIdx: Int) = likePieceDataSource.isLikePiece(pieceIdx, userIdx)
}