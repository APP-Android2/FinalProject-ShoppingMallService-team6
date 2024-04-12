package kr.co.lion.unipiece.ui.mypage.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.RowVisitGalleryHistoryBinding

class VisitGalleryAdapter(val visitList: ArrayList<Any>, private val itemClickListener: (position: Int) -> Unit): RecyclerView.Adapter<VisitGalleryViewHolder>(){

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
        holder.rowVisitGalleryHistoryBinding.textViewRowVisitListDate.text="2024.04.01"
        // 이름
        holder.rowVisitGalleryHistoryBinding.textViewRowVisitListName.text="김길동 $position"
        // 연락처
        holder.rowVisitGalleryHistoryBinding.textViewRowVisitListPhoneNumber.text="010-1234-5678"
        // 방문인원
        holder.rowVisitGalleryHistoryBinding.textViewRowVisitListMemberCount.text="1명"
        // 승인상태
        holder.rowVisitGalleryHistoryBinding.textViewRowVisitListStatus.text="승인 대기중"
        // 신청 수정 버튼 여부
        holder.rowVisitGalleryHistoryBinding.buttonRowVisitListModify.isVisible = true

        // 신청 수정 버튼 클릭 이벤트
        holder.rowVisitGalleryHistoryBinding.buttonRowVisitListModify.setOnClickListener {
            // 회원 정보 수정 프래그먼트 교체
            itemClickListener(position)
        }
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