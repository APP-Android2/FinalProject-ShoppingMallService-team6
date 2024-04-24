package kr.co.lion.unipiece.db.remote

import android.content.Context
import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.co.lion.unipiece.model.AuthorAddData
import java.io.File

class AuthorAddDataSource {

    val db = Firebase.firestore
    val storage = Firebase.storage.reference


    //이미지 데이터를 firebase storage에 업로드하는 메서드(첨부파일)
    suspend fun uploadFileByApp(context: Context, fileName:String, uploadFileName:String){
        val path = context.getExternalFilesDir(null).toString()
        //서버에 업로드할 파일의 경로
        val file = File("${path}/${fileName}")
        val uri = Uri.fromFile(file)

        val job1 = CoroutineScope(Dispatchers.IO).launch {
            val storageRef = storage.child("AuthorAdd/${uploadFileName}")
            storageRef.putFile(uri)
        }
        job1.join()
    }


    //이미지 데이터를 firebase storage에 업로드하는 메서드(프로필 이미지)
    suspend fun uploadImageByApp(context: Context, fileName:String, uploadFileName:String){
        val path = context.getExternalFilesDir(null).toString()
        //서버에 업로드할 파일의 경로
        val file = File("${path}/${fileName}")
        val uri = Uri.fromFile(file)

        val job1 = CoroutineScope(Dispatchers.IO).launch {
            val storageRef = storage.child("AuthorImg/${uploadFileName}")
            storageRef.putFile(uri)
        }
        job1.join()
    }


    // 작가 정보를 저장한다.
    suspend fun insertAuthorAddData(authorAddData: AuthorAddData){
        val job1 = CoroutineScope(Dispatchers.IO).launch {
            // 컬렉션에 접근할 수 있는 객체를 가져온다.
            val collectionReference = db.collection("AuthorAdd")
            // 컬렉션에 문서를 추가한다.
            // 문서를 추가할 때 객체나 맵을 지정한다.
            // 추가된 문서 내부의 필드는 객체가 가진 프로퍼티의 이름이나 맵에 있는 데이터의 이름으로 동일하게 결정된다.
            collectionReference.add(authorAddData)
        }
        job1.join()
    }

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

    // 작가 등록 확인
    suspend fun isAuthorAdd(userIdx: Int): Boolean {
        return try {
            val querySnapshot = db.collection("AuthorAdd")
                .whereEqualTo("userIdx", userIdx)
                .get()
                .await()

            !querySnapshot.isEmpty
        } catch (e: Exception) {
            false
        }
    }
}