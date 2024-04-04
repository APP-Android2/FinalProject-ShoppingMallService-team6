package kr.co.lion.unipiece.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.ItemSearchPieceBinding

class SearchPieceAdapter(val imageList: ArrayList<Int>, private val itemClickListener: (Int) -> Unit): RecyclerView.Adapter<SearchPieceViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPieceViewHolder {
        val binding: ItemSearchPieceBinding = ItemSearchPieceBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SearchPieceViewHolder(binding, itemClickListener)
    }

    override fun getItemCount(): Int = imageList.size

    override fun onBindViewHolder(holder: SearchPieceViewHolder, position: Int) {
        holder.pieceImg.setImageResource(imageList[position])
    }


}

class SearchPieceViewHolder(val binding: ItemSearchPieceBinding, private val itemClickListener: (Int) -> Unit): RecyclerView.ViewHolder(binding.root) {
    val pieceImg: ImageView = binding.pieceImg
    val itemPiece: LinearLayout = binding.itemPiece

    init {
        itemPiece.setOnClickListener {
            itemClickListener.invoke(position)
        }
    }
}