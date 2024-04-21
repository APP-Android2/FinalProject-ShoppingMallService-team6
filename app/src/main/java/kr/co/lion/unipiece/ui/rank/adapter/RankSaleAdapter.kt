package kr.co.lion.unipiece.ui.rank.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.ItemRankSaleBinding
import kr.co.lion.unipiece.model.AuthorInfoData
import kr.co.lion.unipiece.model.PieceInfoData
import kr.co.lion.unipiece.util.setImage
import java.text.DecimalFormat

class RankSaleAdapter (val authorInfoList: List<AuthorInfoData>, private val itemClickListener: (Int) -> Unit) : RecyclerView.Adapter<RankSaleViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType:Int): RankSaleViewHolder {
        val binding: ItemRankSaleBinding = ItemRankSaleBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return RankSaleViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: RankSaleViewHolder, position: Int){
        holder.bind(authorInfoList[position])
    }

    override fun getItemCount(): Int = authorInfoList.size
}

class RankSaleViewHolder(val binding: ItemRankSaleBinding, private val itemClickListener: (Int) -> Unit): RecyclerView.ViewHolder(binding.root) {

    init {
        // 클릭 리스너 설정, 클릭 시 pieceIdx 전달
        binding.root.setOnClickListener {
            itemClickListener.invoke(adapterPosition)
        }
    }

    fun bind(item: AuthorInfoData) {

        with(binding) {
            authorName.text = item.authorName
            authorRank.text = (adapterPosition + 1).toString()
            saleCount.text = "${item.authorSale}회"

            root.context.setImage(authorImg, item.authorImg)

        }
    }
}