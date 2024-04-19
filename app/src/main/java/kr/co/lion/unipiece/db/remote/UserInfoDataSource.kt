package kr.co.lion.unipiece.db.remote

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.co.lion.unipiece.model.UserInfoData

class UserInfoDataSource {

    private val db = Firebase.firestore

    //사용자 번호 시퀀스를 가져온다
    suspend fun getUserSequence():Int{
        //값 초기화
        var userSequence = 0

        var job1 = CoroutineScope(Dispatchers.IO).launch {
            //컬렉션에 접근
            val collectionReference = db.collection("Sequence")
            //문서에 접근
            val documentReference = collectionReference.document("UserSequence")
            //문서 내에 있는 데이터를 가져올 객체를 가져온다
            val documentSnapshot = documentReference.get().await()

            userSequence = documentSnapshot.getLong("value")?.toInt()?: -1

        }
        job1.join()
        return userSequence
    }

    //시퀀스 업데이트
    suspend fun updateUserSequence(userSequence:Int){
        val job1 = CoroutineScope(Dispatchers.IO).launch {
            val collectionReference = db.collection("Sequence")
            val documentReference = collectionReference.document("UserSequence")
            val map = mutableMapOf<String, Long>()
            map["value"] = userSequence.toLong()
            documentReference.set(map)
        }
        job1.join()
    }

    //사용자 정보 저장
    suspend fun insetUserData(userInfoData: UserInfoData){
        val job1 = CoroutineScope(Dispatchers.IO).launch {
            val collectionReference = db.collection("UserInfo")
            collectionReference.add(userInfoData)
        }
        job1.join()
    }

    //입력한 아이디가 이미 있는지 확인한다
    suspend fun checkUserId(userId:String):Boolean{
        var chk = false

        val job1 = CoroutineScope(Dispatchers.IO).launch {
            val collectionReference = db.collection("UserInfo")
            val querySnapshot = collectionReference.whereEqualTo("userId", userId).get().await()
            chk = querySnapshot.isEmpty
        }
        job1.join()
        return chk
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

    //유저의 아이디를 가지고 유저의 정보 가져오기
    suspend fun getUserDataByUserId(userId:String) : UserInfoData? {
        //사용자 정보를 담을 객체
        var userInfoData:UserInfoData? = null

        var job1 = CoroutineScope(Dispatchers.IO).launch {
            val collectionReference = db.collection("UserInfo")
            val querySnapshot = collectionReference.whereEqualTo("userId", userId).get().await()
            if (querySnapshot.isEmpty == false){
                userInfoData = querySnapshot.documents[0].toObject(UserInfoData::class.java)
            }
        }
        job1.join()

        return userInfoData
    }

    // 유저 정보 업데이트
    suspend fun updateUserData(userInfoData: UserInfoData):Boolean{
        return try{
            val userInfoDataMap = mapOf(
                "userName" to userInfoData.userName,
                "nickName" to userInfoData.nickName,
                "phoneNumber" to userInfoData.phoneNumber,
                "userPwd" to userInfoData.userPwd,
            )
            val collectionReference = db.collection("UserInfo")
            val querySnapshot = collectionReference.whereEqualTo("userIdx", userInfoData.userIdx).get().await()
            querySnapshot.documents[0].reference.update(userInfoDataMap).await()
            true
        }catch (e: Exception){
            Log.e("Firebase Error", "Error updateUserData: ${e.message}")
            false
        }
    }

    // 회원 탈퇴 처리
    suspend fun deleteUser(userIdx:Int):Boolean{
        return try{
            val userInfoDataMap = mapOf(
                "userState" to false
            )
            val collectionReference = db.collection("UserInfo")
            val querySnapshot = collectionReference.whereEqualTo("userIdx", userIdx).get().await()
            querySnapshot.documents[0].reference.update(userInfoDataMap).await()
            true
        }catch (e: Exception){
            Log.e("Firebase Error", "Error deleteUser: ${e.message}")
            false
        }
    }

}