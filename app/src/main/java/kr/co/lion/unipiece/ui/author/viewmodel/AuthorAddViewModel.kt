package kr.co.lion.unipiece.ui.author.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.co.lion.unipiece.model.AuthorAddData
import kr.co.lion.unipiece.repository.AuthorAddRepository

class AuthorAddViewModel : ViewModel() {

    private val authorAddRepository = AuthorAddRepository()

    fun insertAuthorInfo(
        userIdx: Int, authorFile: String, authorName:String, authorMajor:String, authorUni:String,
        authorInfo:String, authorImg:String, authorNew:Boolean, callback:(Boolean) -> Unit
    ){
        viewModelScope.launch {

            val authorAddData = AuthorAddData(userIdx, authorFile, authorName, authorMajor, authorUni, authorInfo, authorImg, authorNew)

            val success = withContext(Dispatchers.IO){
                try {
                    authorAddRepository.insertAuthorAddData(authorAddData)
                    true
                }catch (e:Exception){
                    false
                }
            }
            callback(success)

        }
    }


    //첨부파일 업로드
    suspend fun uploadFileByApp(context: Context, fileName:String, uploadFileName:String){
        return authorAddRepository.uploadFileByApp(context, fileName, uploadFileName)
    }


    //프로필 이미지 업로드
    suspend fun uploadImageByApp(context: Context, fileName: String, uploadFileName: String){
        return authorAddRepository.uploadImageByApp(context, fileName, uploadFileName)
    }
}