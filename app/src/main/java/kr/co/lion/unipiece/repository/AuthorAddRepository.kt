package kr.co.lion.unipiece.repository

import android.content.Context
import kr.co.lion.unipiece.db.remote.AuthorAddDataSource
import kr.co.lion.unipiece.model.AuthorAddData

class AuthorAddRepository {

    private val authorAddDataSource = AuthorAddDataSource()

    suspend fun uploadFileByApp(context: Context, fileName:String, uploadFileName:String) = authorAddDataSource.uploadFileByApp(context, fileName, uploadFileName)

    suspend fun uploadImageByApp(context: Context, fileName:String, uploadFileName:String) = authorAddDataSource.uploadImageByApp(context, fileName, uploadFileName)

    suspend fun insertAuthorAddData(authorAddData: AuthorAddData) = authorAddDataSource.insertAuthorAddData(authorAddData)

    suspend fun getAuthorInfoByAuthorIdx(authorIdx:Int) = authorAddDataSource.getAuthorInfoByAuthorIdx(authorIdx)

    suspend fun isAuthorAdd(userIdx: Int) = authorAddDataSource.isAuthorAdd(userIdx)
}
