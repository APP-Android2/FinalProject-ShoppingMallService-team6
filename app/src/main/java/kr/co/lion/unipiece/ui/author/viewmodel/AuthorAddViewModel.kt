package kr.co.lion.unipiece.ui.author.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.co.lion.unipiece.model.AuthorAddData
import kr.co.lion.unipiece.repository.AuthorAddRepository

class AuthorAddViewModel : ViewModel() {

    private val authorAddRepository = AuthorAddRepository()

    fun insertAuthorInfo(
        userIdx: Int, authorFile: String, authorName:String, authorMajor:String, authorUni:String,
        authorInfo:String, authorImg:String?, authorNew:Boolean, authorIdx:Int, authorUniState:String, authorRegisterTime:Timestamp, callback:(Boolean) -> Unit
    ){
        viewModelScope.launch {

            val authorAddData = AuthorAddData(userIdx, authorFile, authorName, authorMajor, authorUni, authorInfo, authorImg, authorNew, authorIdx, authorUniState, authorRegisterTime)

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

    //작가 정보 가져오기
    suspend fun getAuthorInfoByAuthorIdx(authorIdx:Int): AuthorAddData? {
        return authorAddRepository.getAuthorInfoByAuthorIdx(authorIdx)
    }
}