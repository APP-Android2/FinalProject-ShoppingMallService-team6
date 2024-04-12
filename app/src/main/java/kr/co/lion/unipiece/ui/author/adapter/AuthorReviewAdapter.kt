package kr.co.lion.unipiece.ui.author.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.RowAuthorReviewBottomSheetBinding

class AuthorReviewAdapter(val reviewList: ArrayList<Any>): RecyclerView.Adapter<AuthorReviewViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorReviewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val rowAuthorReviewBottomSheetBinding = RowAuthorReviewBottomSheetBinding.inflate(inflater)
        val viewHolder = AuthorReviewViewHolder(rowAuthorReviewBottomSheetBinding)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return 20
    }

    override fun onBindViewHolder(holder: AuthorReviewViewHolder, position: Int) {
        // 닉네임
        holder.rowAuthorReviewBottomSheetBinding.textViewRowAuthorReviewNickName.text = "김토끼 $position"
        // 댓글 내용
        holder.rowAuthorReviewBottomSheetBinding.textViewRowAuthorReviewText.text = "홍작가님 작품 너무 대박이에요"
        // 삭제 버튼은 본인 댓글만 보여지게
        if(true){
            holder.rowAuthorReviewBottomSheetBinding.buttonRowAuthorReviewDelete.isVisible = true
        }else{
            holder.rowAuthorReviewBottomSheetBinding.buttonRowAuthorReviewDelete.isVisible = false
        }
    }
}

class AuthorReviewViewHolder(rowAuthorReviewBottomSheetBinding: RowAuthorReviewBottomSheetBinding):
    RecyclerView.ViewHolder(rowAuthorReviewBottomSheetBinding.root){
    val rowAuthorReviewBottomSheetBinding: RowAuthorReviewBottomSheetBinding

    init {
        this.rowAuthorReviewBottomSheetBinding = rowAuthorReviewBottomSheetBinding

        this.rowAuthorReviewBottomSheetBinding.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}