package kr.co.lion.unipiece.db.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.co.lion.unipiece.model.UserInfoData

class CartDataSource {

    private val db = Firebase.firestore


    // 유저 시퀀스
    suspend fun getCartSequence():Int{
        //값 초기화
        var cartSequence = 0

        var job1 = CoroutineScope(Dispatchers.IO).launch {
            //컬렉션에 접근
            val collectionReference = db.collection("Sequence")
            //문서에 접근
            val documentReference = collectionReference.document("UserSequence")
            //문서 내에 있는 데이터를 가져올 객체를 가져온다
            val documentSnapshot = documentReference.get().await()

            cartSequence = documentSnapshot.getLong("value")?.toInt()?: -1

        }
        job1.join()
        return cartSequence
    }

    //시퀀스 업데이트
    suspend fun updateCartSequence(cartSequence:Int){
        val job1 = CoroutineScope(Dispatchers.IO).launch {
            val collectionReference = db.collection("Sequence")
            val documentReference = collectionReference.document("CartSequence")
            val map = mutableMapOf<String, Long>()
            map["value"] = cartSequence.toLong()
            documentReference.set(map)
        }
        job1.join()
    }

    // 작품의 idx값을 가지고 작품의 정보 가져오기
    suspend fun getPieceDataByIdx(userIdx:Int) : UserInfoData?{
        var userInfoData: UserInfoData? = null

        var job1 = CoroutineScope(Dispatchers.IO).launch {
            val collectionReference = db.collection("UserInfo")
            val querySnapshot = collectionReference.whereEqualTo("userIdx", userIdx).get().await()
            if (querySnapshot.isEmpty == false){
                userInfoData = querySnapshot.documents[0].toObject(UserInfoData::class.java)
            }
        }
        job1.join()

        return userInfoData
    }

    //유저의 idx값을 가지고 유저의 정보 가져오기
    suspend fun getUserDataByIdx(userIdx:Int) : UserInfoData?{
        var userInfoData:UserInfoData? = null

        var job1 = CoroutineScope(Dispatchers.IO).launch {
            val collectionReference = db.collection("UserInfo")
            val querySnapshot = collectionReference.whereEqualTo("userIdx", userIdx).get().await()
            if (querySnapshot.isEmpty == false){
                userInfoData = querySnapshot.documents[0].toObject(UserInfoData::class.java)
            }
        }
        job1.join()

        return userInfoData
    }
}