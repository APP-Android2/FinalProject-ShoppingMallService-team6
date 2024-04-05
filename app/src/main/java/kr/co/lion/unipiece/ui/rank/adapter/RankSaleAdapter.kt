package kr.co.lion.unipiece.ui.rank.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.ItemRankSaleBinding

class RankSaleAdapter (val imageList: ArrayList<Int>, private val itemClickListener: (Int) -> Unit) : RecyclerView.Adapter<RankSaleViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType:Int): RankSaleViewHolder {
        val binding: ItemRankSaleBinding = ItemRankSaleBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return RankSaleViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: RankSaleViewHolder, position: Int){
        holder.authorImg.setImageResource(imageList[position])
        holder.authorRank.text = (position + 1).toString()
    }

    override fun getItemCount(): Int = imageList.size
}

class RankSaleViewHolder(val binding: ItemRankSaleBinding, private val itemClickListener: (Int) -> Unit): RecyclerView.ViewHolder(binding.root) {
    val authorImg: ImageView = binding.authorImg
    val authorRank: TextView = binding.authorRank
    val authorName: TextView = binding.authorName
    val saleCount: TextView = binding.saleCount

    init {
        binding.root.setOnClickListener {
            itemClickListener.invoke(position)
        }
    }
}