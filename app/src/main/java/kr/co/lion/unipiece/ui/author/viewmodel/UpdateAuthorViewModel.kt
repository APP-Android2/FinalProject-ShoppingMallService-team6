package kr.co.lion.unipiece.ui.author.viewmodel

import androidx.lifecycle.ViewModel
import kr.co.lion.unipiece.model.AuthorAddData
import kr.co.lion.unipiece.repository.UpdateAuthorRepository

class UpdateAuthorViewModel: ViewModel() {

    private val updateAuthorRepository = UpdateAuthorRepository()


    suspend fun getAuthorInfoByAuthorIdx(authorIdx:Int): AuthorAddData? {
        return updateAuthorRepository.getAuthorInfoByAuthorIdx(authorIdx)
    }
}