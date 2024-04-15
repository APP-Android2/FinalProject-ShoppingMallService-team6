package kr.co.lion.unipiece.db.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.co.lion.unipiece.model.AuthorReviewData

class AuthorReviewDataSource {

    private val db = Firebase.firestore

    // 리뷰 시퀀스값을 가져온다.
    suspend fun getReviewSequence():Int{

        var reviewSequence = 0

        val job1 = CoroutineScope(Dispatchers.IO).launch {
            // 컬렉션에 접근할 수 있는 객체를 가져온다.
            val collectionReference = db.collection("Sequence")
            // 리뷰 시퀀스값을 가지고 있는 문서에 접근할 수 있는 객체를 가져온다.
            val documentReference = collectionReference.document("ReviewSequence")
            // 문서내에 있는 데이터를 가져올 수 있는 객체를 가져온다.
            val documentSnapShot = documentReference.get().await()

            reviewSequence = documentSnapShot.getLong("value")?.toInt()!!
        }
        job1.join()

        return reviewSequence
    }

    // 리뷰 시퀀스 값을 업데이트 한다.
    suspend fun updateReviewSequence(reviewSequence: Int){
        val job1 = CoroutineScope(Dispatchers.IO).launch {
            // 컬렉션에 접근할 수 있는 객체를 가져온다.
            val collectionReference = db.collection("Sequence")
            // 작가 번호 시퀀스값을 가지고 있는 문서에 접근할 수 있는 객체를 가져온다.
            val documentReference = collectionReference.document("ReviewSequence")
            // 저장할 데이터를 담을 HashMap을 만들어준다.
            val map = mutableMapOf<String, Long>()
            // "value"라는 이름의 필드가 있다면 값이 덮어씌워지고 필드가 없다면 필드가 새로 생성된다.
            map["value"] = reviewSequence.toLong()
            // 저장한다.
            documentReference.set(map)
        }
        job1.join()
    }


    // 리뷰를 저장한다.
    suspend fun insertReviewData(authorReviewData: AuthorReviewData){
        val job1 = CoroutineScope(Dispatchers.IO).launch {
            // 컬렉션에 접근할 수 있는 객체를 가져온다.
            val collectionReference = db.collection("AuthorReview")
            // 컬렉션에 문서를 추가한다.
            // 문서를 추가할 때 객체나 맵을 지정한다.
            // 추가된 문서 내부의 필드는 객체가 가진 프로퍼티의 이름이나 맵에 있는 데이터의 이름으로 동일하게 결정된다.
            collectionReference.add(authorReviewData)
        }
        job1.join()
    }

    // 작가idx를 통해 리뷰를 가져와 반환한다
    suspend fun getAuthorReviewDataByIdx(authorIdx:Int) : MutableList<AuthorReviewData> {

        val reviewList = mutableListOf<AuthorReviewData>()

        val job1 = CoroutineScope(Dispatchers.IO).launch {
            // AuthorInfo 컬렉션 접근 객체를 가져온다.
            val collectionReference = db.collection("AuthorReview")
            // authorIdx 필드가 매개변수로 들어오는 authorIdx와 같은 문서들을 가져온다.
            val querySnapshot = collectionReference.whereEqualTo("authorIdx", authorIdx).get().await()
            // 가져온 문서의 수 만큼 반복한다.
            querySnapshot.forEach {
                val reviewData = it.toObject(AuthorReviewData::class.java)
                reviewList.add(reviewData)
            }
        }
        job1.join()

        return reviewList
    }

    // 리뷰idx를 통해 리뷰를 삭제한다.
    suspend fun deleteReview(reviewIdx:Int){
        val job1 = CoroutineScope(Dispatchers.IO).launch {
            // 컬렉션에 접근할 수 있는 객체를 가져온다.
            val collectionReference = db.collection("AuthorReview")
            // reviewIdx 필드가 매개변수로 들어오는 reviewIdx와 같은 문서를 가져온다.
            val querySnapshot = collectionReference.whereEqualTo("reviewIdx", reviewIdx).get().await()
            val documentReference = querySnapshot.documents[0].reference
            documentReference.delete()
        }
        job1.join()
    }

}