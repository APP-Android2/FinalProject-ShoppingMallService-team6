package kr.co.lion.unipiece.db.remote

import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.co.lion.unipiece.model.AuthorInfoData

class AuthorInfoDataSource {

    private val db = Firebase.firestore
    private val storage = Firebase.storage.reference

    // 작가 번호 시퀀스값을 가져온다.
    suspend fun getAuthorSequence():Int{

        var authorSequence = 0

        val job1 = CoroutineScope(Dispatchers.IO).launch {
            // 컬렉션에 접근할 수 있는 객체를 가져온다.
            val collectionReference = db.collection("Sequence")
            // 사용자 번호 시퀀스값을 가지고 있는 문서에 접근할 수 있는 객체를 가져온다.
            val documentReference = collectionReference.document("AuthorSequence")
            // 문서내에 있는 데이터를 가져올 수 있는 객체를 가져온다.
            val documentSnapShot = documentReference.get().await()

            authorSequence = documentSnapShot.getLong("value")?.toInt()!!
        }
        job1.join()

        return authorSequence
    }

    // 작가 시퀀스 값을 업데이트 한다.
    suspend fun updateAuthorSequence(authorSequence: Int){
        val job1 = CoroutineScope(Dispatchers.IO).launch {
            // 컬렉션에 접근할 수 있는 객체를 가져온다.
            val collectionReference = db.collection("Sequence")
            // 작가 번호 시퀀스값을 가지고 있는 문서에 접근할 수 있는 객체를 가져온다.
            val documentReference = collectionReference.document("AuthorSequence")
            // 저장할 데이터를 담을 HashMap을 만들어준다.
            val map = mutableMapOf<String, Long>()
            // "value"라는 이름의 필드가 있다면 값이 덮어씌워지고 필드가 없다면 필드가 새로 생성된다.
            map["value"] = authorSequence.toLong()
            // 저장한다.
            documentReference.set(map)
        }
        job1.join()
    }

    // 작가 정보를 저장한다.
    suspend fun insertAuthorInfoData(authorInfoData: AuthorInfoData){
        val job1 = CoroutineScope(Dispatchers.IO).launch {
            // 컬렉션에 접근할 수 있는 객체를 가져온다.
            val collectionReference = db.collection("AuthorInfo")
            // 컬렉션에 문서를 추가한다.
            // 문서를 추가할 때 객체나 맵을 지정한다.
            // 추가된 문서 내부의 필드는 객체가 가진 프로퍼티의 이름이나 맵에 있는 데이터의 이름으로 동일하게 결정된다.
            collectionReference.add(authorInfoData)
        }
        job1.join()
    }

    // 작가 번호를 통해 작가 정보를 가져와 반환한다
    suspend fun getAuthorInfoDataByIdx(authorIdx:Int) : AuthorInfoData? {

        var authorInfoData:AuthorInfoData? = null

        val job1 = CoroutineScope(Dispatchers.IO).launch {
            // AuthorInfo 컬렉션 접근 객체를 가져온다.
            val collectionReference = db.collection("AuthorInfo")
            // authorIdx 필드가 매개변수로 들어오는 authorIdx와 같은 문서들을 가져온다.
            val querySnapshot = collectionReference.whereEqualTo("authorIdx", authorIdx).get().await()
            // 가져온 문서객체들이 들어 있는 리스트에서 첫 번째 객체를 추출한다.
            // 회원 번호가 동일한 사용는 없기 때문에 무조건 하나만 나오기 때문이다
            authorInfoData = querySnapshot.documents[0].toObject(AuthorInfoData::class.java)
        }
        job1.join()

        return authorInfoData
    }

    // userIdx로 작가Idx를 가져와 반환한다
    suspend fun getAuthorIdxByUserIdx(userIdx:Int) : Int {
        return try {
            val querySnapshot = db.collection("AuthorInfo")
                .whereEqualTo("userIdx", userIdx)
                .get()
                .await()

            val authorIdx = querySnapshot.documents.firstOrNull()?.get("authorIdx") as? Long ?: 0

            authorIdx.toInt()

        } catch (e: Exception) {
            Log.e("firebase", "Failed to get authorIdx: ${Log.getStackTraceString(e)}")
            0
        }
    }

    // 모든 작가의 정보를 가져온다.
    suspend fun getAuthorInfoAll():MutableList<AuthorInfoData>{
        // 사용자 정보를 담을 리스트
        val authorList = mutableListOf<AuthorInfoData>()

        val job1 = CoroutineScope(Dispatchers.IO).launch {
            // 모든 사용자 정보를 가져온다.
            val querySnapshot = db.collection("AuthorInfo").get().await()
            // 가져온 문서의 수 만큼 반복한다.
            querySnapshot.forEach {
                // UserModel 객체에 담는다.
                val authorInfoData = it.toObject(AuthorInfoData::class.java)
                // 리스트에 담는다.
                authorList.add(authorInfoData)
            }
        }
        job1.join()

        return authorList
    }

    // 작가 이미지 url 받아오기
    suspend fun getAuthorInfoImg(authorImg: String): String? {
        val path = "AuthorInfo/$authorImg"
        return try {
            storage.child(path).downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error getPieceInfoImg: ${e.message} ${path}")
            null
        }
    }


    // 작가 정보를 수정하는 메서드
    suspend fun updateAuthorInfoData(authorInfoData: AuthorInfoData){
        val job1 = CoroutineScope(Dispatchers.IO).launch {
            // 컬렉션에 접근할 수 있는 객체를 가져온다.
            val collectionReference = db.collection("AuthorInfo")

            // 컬렉션이 가지고 있는 문서들 중에 수정할 작가 정보를 가져온다.
            val query = collectionReference.whereEqualTo("authorIdx", authorInfoData.authorIdx).get().await()

            // 저장할 데이터를 담을 HashMap을 만들어준다.
            val map = mutableMapOf<String, Any?>()
            map["authorImg"] = authorInfoData.authorImg
            map["authorName"] = authorInfoData.authorName
            map["authorBasic"] = authorInfoData.authorBasic
            map["authorInfo"] = authorInfoData.authorInfo

            // 저장한다.
            // 가져온 문서 중 첫 번째 문서에 접근하여 데이터를 수정한다.
            query.documents[0].reference.update(map)
        }
        job1.join()
    }

    // 작가 번호와 일치하는 팔로우 데이터를 가져와 총 개수를 반환한다.
    suspend fun getFollowCount(authorIdx: Int): Int{
        var count = 0
        val job1 = CoroutineScope(Dispatchers.IO).launch {
            // 컬렉션에 접근할 수 있는 객체를 가져온다.
            val collectionReference = db.collection("Following")

            // 컬렉션이 가지고 있는 문서들 중에 작가idx에 해당하는 데이터를 가져온다.
            val query = collectionReference.whereEqualTo("authorIdx", authorIdx).get().await()

            // 가져온 데이터의 개수를 반환한다.
            count = query.count()
        }
        job1.join()
        return count
    }

    // 해당 작가 팔로우 여부 체크
    suspend fun checkFollow(userIdx:Int, authorIdx:Int):Boolean{
        var check = false
        val job1 = CoroutineScope(Dispatchers.IO).launch {
            // 컬렉션에 접근할 수 있는 객체를 가져온다.
            val collectionReference = db.collection("Following")

            // 컬렉션이 가지고 있는 문서들 중에 회원이 해당 작가를 팔로우 한 데이터를 가져온다.
            val query = collectionReference
                .whereEqualTo("userIdx", userIdx)
                .whereEqualTo("authorIdx", authorIdx)
                .get().await()

            check = query.documents.isNotEmpty()
        }
        job1.join()
        return check
    }

    // 작가 팔로우 추가
    suspend fun addAuthorFollow(userIdx:Int, authorIdx:Int){
        val job1 = CoroutineScope(Dispatchers.IO).launch {
            // 컬렉션에 접근할 수 있는 객체를 가져온다.
            val collectionReference = db.collection("Following")

            // 저장할 데이터를 담을 HashMap을 만들어준다.
            val map = mutableMapOf<String, Any?>()
            map["userIdx"] = userIdx
            map["authorIdx"] = authorIdx

            // 컬렉션에 문서를 추가한다.
            collectionReference.add(map)
        }
        job1.join()
    }

    // 작가 팔로우 취소
    suspend fun cancelFollowing(userIdx:Int, authorIdx:Int){
        val job1 = CoroutineScope(Dispatchers.IO).launch {
            // 컬렉션에 접근할 수 있는 객체를 가져온다.
            val collectionReference = db.collection("Following")

            // 회원 번호와 작가 번호가 일치하는 문서에 접근할 수 있는 객체를 가져온다.
            val query = collectionReference
                .whereEqualTo("userIdx",userIdx)
                .whereEqualTo("authorIdx",authorIdx).get().await()

            // 삭제 한다.
            query.documents[0].reference.delete()
        }
        job1.join()
    }

    // 작가 확인
    suspend fun isAuthor(userIdx: Int): Boolean {
        return try {
            val querySnapshot = db.collection("AuthorInfo")
                .whereEqualTo("userIdx", userIdx)
                .get()
                .await()

            !querySnapshot.isEmpty
        } catch (e: Exception) {
            false
        }
    }

    // 작가 이미지 url 작가 idx로 받아오기
    suspend fun getAuthorIdxImg(authorIdx: Int): String? {

        val authorImg = "${authorIdx}.jpg"

        val path = "AuthorInfo/$authorImg"
        return try {
            storage.child(path).downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error getPieceInfoImg: ${e.message} ${path}")
            null
        }
    }

    // 작가 이미지 파일 업로드
    suspend fun uploadImage(authorIdx: Int, imageUri: Uri): Boolean {
        val imageFileName = "${authorIdx}.jpg"
        val imageRef = storage.child("AuthorInfo/$imageFileName")

        return try {
            imageRef.putFile(imageUri).await()
            true
        } catch (e: Exception) {
            Log.e("Firebase Error", "Error uploadImage: ${e.message}")
            false
        }
    }
}