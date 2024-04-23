package kr.co.lion.unipiece.ui.search.adapter

import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.ItemSearchAuthorBinding
import kr.co.lion.unipiece.model.AuthorInfoData
import kr.co.lion.unipiece.model.SearchAuthorData
import kr.co.lion.unipiece.model.SearchPieceData
import kr.co.lion.unipiece.model.SearchResultData
import kr.co.lion.unipiece.util.setImage

class SearchAuthorViewHolder(
    private val binding: ItemSearchAuthorBinding,
    private val itemClickListener: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root){

    init {
        binding.root.setOnClickListener {
            itemClickListener.invoke(adapterPosition)
        }
    }

    fun bind(item: AuthorInfoData){

        with(binding){
            root.context.setImage(authorImg, item.authorImg)
            authorName.text = item.authorName
            followCount.text = "${item.authorFollow} 팔로워"
        }

    }
}