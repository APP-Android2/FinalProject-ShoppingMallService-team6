package kr.co.lion.unipiece.ui.search.adapter

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.TitleSearchAuthorBinding

class TitleAuthorViewHolder (
    private val binding: TitleSearchAuthorBinding,
    private val itemClickListener: (SearchResultData) -> Unit
) : RecyclerView.ViewHolder(binding.root){

    val title = binding.searchAuthorTitle

    fun bind(searchResultData: SearchResultData){

        if(!searchResultData.viewTitle) {
            title.visibility = View.INVISIBLE
        } else {
            title.visibility = View.VISIBLE
        }
        binding.root.setOnClickListener {

        }

    }
}