package kr.co.lion.unipiece.ui.rank.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.ItemRankPieceBinding

class RankPieceAdapter (val imageList: ArrayList<Int>, private val itemClickListener: (Int) -> Unit) : RecyclerView.Adapter<RankPieceViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType:Int): RankPieceViewHolder {
        val binding: ItemRankPieceBinding = ItemRankPieceBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return RankPieceViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: RankPieceViewHolder, position: Int){
        holder.pieceImg.setImageResource(imageList[position])
        holder.pieceRank.text = (position + 1).toString()
    }

    override fun getItemCount(): Int = imageList.size
}

class RankPieceViewHolder(val binding: ItemRankPieceBinding, private val itemClickListener: (Int) -> Unit): RecyclerView.ViewHolder(binding.root) {
    val pieceImg: ImageView = binding.pieceImg
    val pieceRank: TextView = binding.pieceRank
    val authorName: TextView = binding.authorName
    val pieceLike: TextView = binding.pieceLike
    val pieceName: TextView = binding.pieceName
    val piecePrice: TextView = binding.piecePrice
    init {
        binding.root.setOnClickListener {
            itemClickListener.invoke(position)
        }
    }
}