package kr.co.lion.unipiece.ui.rank.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.ItemRankFollowerBinding
import kr.co.lion.unipiece.model.AuthorInfoData
import kr.co.lion.unipiece.util.setImage


class RankFollowerAdapter(val authorInfoList: List<AuthorInfoData>, private val itemClickListener: (Int) -> Unit) : RecyclerView.Adapter<RankFollowerViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType:Int): RankFollowerViewHolder {
        val binding: ItemRankFollowerBinding = ItemRankFollowerBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return RankFollowerViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: RankFollowerViewHolder, position: Int){
        holder.bind(authorInfoList[position])
    }

    override fun getItemCount(): Int = authorInfoList.size
}

class RankFollowerViewHolder(val binding: ItemRankFollowerBinding, private val itemClickListener: (Int) -> Unit): RecyclerView.ViewHolder(binding.root) {

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
            followCount.text = "${item.authorFollow} 팔로워"

            root.context.setImage(authorImg, item.authorImg)

        }
    }
}