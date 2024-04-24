package kr.co.lion.unipiece.db.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.co.lion.unipiece.model.AuthorAddData

class UpdateAuthorDataSource {

    val db = Firebase.firestore
    val storage = Firebase.storage.reference


    //authorIdx 값으로 작가 정보 가져오기
    suspend fun getAuthorInfoByAuthorIdx(authorIdx:Int): AuthorAddData?{
        var authorAddData: AuthorAddData? = null

        val job1 = CoroutineScope(Dispatchers.IO).launch {
            val collectionReference = db.collection("AuthorAdd")
            val querySnapshot = collectionReference.whereEqualTo("authorIdx", authorIdx).get().await()
            authorAddData = querySnapshot.documents[0].toObject(AuthorAddData::class.java)
        }
        job1.join()

        return authorAddData
    }
}