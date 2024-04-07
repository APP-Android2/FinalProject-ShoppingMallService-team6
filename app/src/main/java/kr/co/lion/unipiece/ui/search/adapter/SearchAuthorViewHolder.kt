package kr.co.lion.unipiece.ui.search.adapter

import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.ItemSearchAuthorBinding

class SearchAuthorViewHolder(
    private val binding: ItemSearchAuthorBinding,
    private val itemClickListener: (SearchResultData) -> Unit
) : RecyclerView.ViewHolder(binding.root){
    val authorImage = binding.authorImg
    val authorName = binding.authorName
    val followCount = binding.followCount

    fun bind(searchAuthordata: SearchAuthorData){
        authorImage.setImageResource(searchAuthordata.authorImage)
        authorName.text = searchAuthordata.authorName
        followCount.text = searchAuthordata.followCount

        val searchResultData = SearchResultData(searchAuthordata, SearchPieceData(), SearchResultViewType.AUTHOR_CONTENT)

        binding.root.setOnClickListener {
            itemClickListener.invoke(searchResultData)
        }

    }
}