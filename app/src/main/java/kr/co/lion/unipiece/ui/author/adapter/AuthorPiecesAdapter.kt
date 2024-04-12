package kr.co.lion.unipiece.ui.author.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.RowAuthorPiecesBinding

class AuthorPiecesAdapter(val piecesList: ArrayList<Int>, private val itemClickListener: (position: Int) -> Unit): RecyclerView.Adapter<AuthorPiecesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorPiecesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val rowAuthorPiecesBinding = RowAuthorPiecesBinding.inflate(inflater)
        val viewHolder = AuthorPiecesViewHolder(rowAuthorPiecesBinding)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return piecesList.size
    }

    override fun onBindViewHolder(holder: AuthorPiecesViewHolder, position: Int) {
        // 이미지
        holder.rowAuthorPiecesBinding.imageViewAuthorPiece.setImageResource(piecesList[position])
        // 작품 클릭 시 설명 화면 이동
        holder.rowAuthorPiecesBinding.root.setOnClickListener {
            itemClickListener(position)
        }
    }
}

class AuthorPiecesViewHolder(rowAuthorPiecesBinding: RowAuthorPiecesBinding):
    RecyclerView.ViewHolder(rowAuthorPiecesBinding.root){
    val rowAuthorPiecesBinding: RowAuthorPiecesBinding

    init {
        this.rowAuthorPiecesBinding = rowAuthorPiecesBinding

        this.rowAuthorPiecesBinding.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}