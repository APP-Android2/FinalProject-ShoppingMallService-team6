package kr.co.lion.unipiece.db.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.co.lion.unipiece.model.AuthorInfoData

class AuthorInfoDataSource {

    private val db = Firebase.firestore

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
}