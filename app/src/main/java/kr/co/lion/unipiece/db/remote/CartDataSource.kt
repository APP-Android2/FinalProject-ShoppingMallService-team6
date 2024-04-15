package kr.co.lion.unipiece.db.remote

import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.co.lion.unipiece.model.CartData
import kr.co.lion.unipiece.model.PieceInfoData

class CartDataSource {

    private val db = Firebase.firestore



    // 작품의 idx값을 가지고 작품의 정보 가져오기
    suspend fun getPieceDataByIdx(pieceIdx:Int) : MutableList<PieceInfoData>{
        val pieceInfoDataList = mutableListOf<PieceInfoData>()
        val job1 = CoroutineScope(Dispatchers.IO).launch {
            // 작품 정보 컬렉션에 접근
            val collectionReference = db.collection("PieceInfo")
            // pieceIdx 필드가 매개변수로 들어오는 pieceIdx와 같은 문서들을 가져온다.
            val querySnapshot = collectionReference.whereEqualTo("pieceIdx", pieceIdx).get().await()
            // 가져온 문서의 수만큼 반복한다.
            querySnapshot.forEach {
                val pieceInfoData = it.toObject(PieceInfoData::class.java)
                pieceInfoDataList.add(pieceInfoData)
            }
        }
        job1.join()

        return pieceInfoDataList
    }

}