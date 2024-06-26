package kr.co.lion.unipiece.ui.mypage.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.RowVisitGalleryHistoryBinding
import kr.co.lion.unipiece.model.VisitAddData

class VisitGalleryAdapter(var visitList: List<VisitAddData>, private val itemClickListener: (position: Int) -> Unit): RecyclerView.Adapter<VisitGalleryViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitGalleryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val rowVisitGalleryHistoryBinding = RowVisitGalleryHistoryBinding.inflate(inflater, parent, false)
        val viewHolder = VisitGalleryViewHolder(rowVisitGalleryHistoryBinding)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return visitList.size
    }

    override fun onBindViewHolder(holder: VisitGalleryViewHolder, position: Int) {
        // 날짜
        holder.rowVisitGalleryHistoryBinding.textViewRowVisitListDate.text= visitList[position].timestampToString()
        // 이름
        holder.rowVisitGalleryHistoryBinding.textViewRowVisitListName.text= visitList[position].visitorName
        // 연락처
        holder.rowVisitGalleryHistoryBinding.textViewRowVisitListPhoneNumber.text= visitList[position].visitorPhone
        // 방문인원
        holder.rowVisitGalleryHistoryBinding.textViewRowVisitListMemberCount.text= "${visitList[position].visitorNumber}명"
        // 승인상태
        holder.rowVisitGalleryHistoryBinding.textViewRowVisitListStatus.text= visitList[position].visitState
        // 신청 수정 버튼 여부
        when(visitList[position].visitState){
            "승인 대기" -> {
                holder.rowVisitGalleryHistoryBinding.buttonRowVisitListModify.visibility = View.VISIBLE
            }
            "승인 완료" -> {
                holder.rowVisitGalleryHistoryBinding.buttonRowVisitListModify.visibility = View.GONE
                // holder.rowVisitGalleryHistoryBinding.textViewRowVisitListStatus.setTextColor(Color.BLUE)
            }
            "승인 거절" -> {
                holder.rowVisitGalleryHistoryBinding.buttonRowVisitListModify.visibility = View.GONE
                // holder.rowVisitGalleryHistoryBinding.textViewRowVisitListStatus.setTextColor(Color.RED)
            }
        }

        // 신청 수정 버튼 클릭 이벤트
        holder.rowVisitGalleryHistoryBinding.buttonRowVisitListModify.setOnClickListener {
            // 전시실 방문 신청 프래그먼트 교체
            itemClickListener(visitList[position].visitIdx)
        }
    }

    fun updateList(data:List<VisitAddData>){
        visitList = data
        notifyDataSetChanged()
    }

}

class VisitGalleryViewHolder(rowVisitGalleryHistoryBinding: RowVisitGalleryHistoryBinding):RecyclerView.ViewHolder(rowVisitGalleryHistoryBinding.root){
    val rowVisitGalleryHistoryBinding:RowVisitGalleryHistoryBinding

    init {
        this.rowVisitGalleryHistoryBinding = rowVisitGalleryHistoryBinding

        this.rowVisitGalleryHistoryBinding.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}