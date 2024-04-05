package kr.co.lion.unipiece.ui.rank.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.ItemRankFollowerBinding


class RankFollowerAdapter(val imageList: ArrayList<Int>, private val itemClickListener: (Int) -> Unit) : RecyclerView.Adapter<RankFollowerViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType:Int): RankFollowerViewHolder {
        val binding: ItemRankFollowerBinding = ItemRankFollowerBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return RankFollowerViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: RankFollowerViewHolder, position: Int){
        holder.authorImg.setImageResource(imageList[position])
        holder.authorRank.text = (position + 1).toString()
    }

    override fun getItemCount(): Int = imageList.size
}

class RankFollowerViewHolder(val binding: ItemRankFollowerBinding, private val itemClickListener: (Int) -> Unit): RecyclerView.ViewHolder(binding.root) {
    val authorImg: ImageView = binding.authorImg
    val authorRank: TextView = binding.authorRank
    val authorName: TextView = binding.authorName
    val followCount: TextView = binding.followCount

    init {
        binding.root.setOnClickListener {
            itemClickListener.invoke(position)
        }
    }
}