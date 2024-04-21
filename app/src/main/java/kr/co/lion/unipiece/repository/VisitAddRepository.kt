package kr.co.lion.unipiece.repository

import kr.co.lion.unipiece.db.remote.VisitAddDataSource
import kr.co.lion.unipiece.model.VisitAddData

class VisitAddRepository {
    private val visitAddDataSource = VisitAddDataSource()

    suspend fun insertVisitAddData(visitAddData: VisitAddData):Boolean = visitAddDataSource.insertVisitAddData(visitAddData)

    suspend fun getVisitAddDataByIdx(visitIdx:Int): VisitAddData? = visitAddDataSource.getVisitAddDataByIdx(visitIdx)

    suspend fun getVisitAddList(userIdx: Int): List<VisitAddData> = visitAddDataSource.getVisitAddList(userIdx)

    suspend fun updateVisitAddData(visitAddData: VisitAddData): Boolean = visitAddDataSource.updateVisitAddData(visitAddData)

    suspend fun updateVisitState(state: String, visitIdx: Int): Boolean = visitAddDataSource.updateVisitState(state, visitIdx)
}