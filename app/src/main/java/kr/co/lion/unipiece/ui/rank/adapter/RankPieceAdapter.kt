package kr.co.lion.unipiece.ui.rank.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.ItemRankPieceBinding
import kr.co.lion.unipiece.model.PieceInfoData
import kr.co.lion.unipiece.util.setImage
import java.text.DecimalFormat

class RankPieceAdapter (val pieceInfoList: List<PieceInfoData>, private val itemClickListener: (Int) -> Unit) : RecyclerView.Adapter<RankPieceViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType:Int): RankPieceViewHolder {
        val binding: ItemRankPieceBinding = ItemRankPieceBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return RankPieceViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: RankPieceViewHolder, position: Int){
        holder.bind(pieceInfoList[position])
    }

    override fun getItemCount(): Int = pieceInfoList.size
}

class RankPieceViewHolder(val binding: ItemRankPieceBinding, private val itemClickListener: (Int) -> Unit): RecyclerView.ViewHolder(binding.root) {

    init {
        // 클릭 리스너 설정, 클릭 시 pieceIdx 전달
        binding.root.setOnClickListener {
            itemClickListener.invoke(adapterPosition)
        }
    }

    fun bind(item: PieceInfoData) {
        val priceFormat = DecimalFormat("###,###")
        val price = priceFormat.format(item.piecePrice)

        with(binding) {
            authorName.text = item.authorName
            pieceName.text = item.pieceName
            piecePrice.text = "${price}원"
            pieceLike.text = item.pieceLike.toString()
            pieceRank.text = (adapterPosition + 1).toString()

            root.context.setImage(pieceImg, item.pieceImg)

        }
    }
}