package kr.co.lion.unipiece.db.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthorFollowDataSource {
    private val db = Firebase.firestore

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
}