package kr.co.lion.unipiece.repository

import kr.co.lion.unipiece.db.remote.UpdateAuthorDataSource

class UpdateAuthorRepository {

    private val updateAuthorDataSource = UpdateAuthorDataSource()

    suspend fun getAuthorInfoByAuthorIdx(authorIdx:Int) = updateAuthorDataSource.getAuthorInfoByAuthorIdx(authorIdx)
}