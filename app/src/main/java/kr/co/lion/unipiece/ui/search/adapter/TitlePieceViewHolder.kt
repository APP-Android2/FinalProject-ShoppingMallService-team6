package kr.co.lion.unipiece.ui.search.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.TitleSearchPieceBinding
import kr.co.lion.unipiece.model.SearchResultData

class TitlePieceViewHolder (
    private val binding: TitleSearchPieceBinding,
    private val itemClickListener: (SearchResultData) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    val title = binding.searchPieceTitle

    fun bind(searchResultData: SearchResultData) {

        if(!searchResultData.viewTitle) {
            title.visibility = View.INVISIBLE
        } else {
            title.visibility = View.VISIBLE
        }

        binding.root.setOnClickListener {

        }
    }

}