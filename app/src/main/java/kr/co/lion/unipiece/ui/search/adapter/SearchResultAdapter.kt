package kr.co.lion.unipiece.ui.search.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ItemSearchAuthorBinding
import kr.co.lion.unipiece.databinding.ItemSearchPieceBinding
import kr.co.lion.unipiece.databinding.TitleSearchAuthorBinding
import kr.co.lion.unipiece.databinding.TitleSearchPieceBinding
import kr.co.lion.unipiece.ui.search.adapter.SearchResultViewType.*

class SearchResultAdapter(
    private val itemClickListener: (SearchResultData) -> Unit )
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var items = ArrayList<SearchResultData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            R.layout.title_search_author -> {
                TitleAuthorViewHolder(TitleSearchAuthorBinding.inflate(LayoutInflater.from(parent.context),
                    parent, false),
                    itemClickListener
                )
            }
            R.layout.item_search_author -> {
                SearchAuthorViewHolder(ItemSearchAuthorBinding.inflate(LayoutInflater.from(parent.context),
                    parent, false),
                    itemClickListener)
            }
            R.layout.title_search_piece -> {
                TitlePieceViewHolder(TitleSearchPieceBinding.inflate(LayoutInflater.from(parent.context),
                    parent, false),
                    itemClickListener)
            }
            else -> {
                SearchPieceViewHolder(ItemSearchPieceBinding.inflate(LayoutInflater.from(parent.context),
                    parent, false),
                    itemClickListener)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            R.layout.title_search_author -> {
                (holder as TitleAuthorViewHolder).bind(items[position])
            }
            R.layout.item_search_author -> {
                (holder as SearchAuthorViewHolder).bind(items[position].searchAuthorData)
            }
            R.layout.title_search_piece -> {
                (holder as TitlePieceViewHolder).bind(items[position])
            }
            R.layout.item_search_piece -> {
                (holder as SearchPieceViewHolder).bind(items[position].searchPieceData)
            }
        }

    }

    override fun getItemViewType(position: Int) = when (items[position].viewType) {
        AUTHOR_TITLE -> R.layout.title_search_author
        AUTHOR_CONTENT -> R.layout.item_search_author
        PIECE_TITLE -> R.layout.title_search_piece
        PIECE_CONTENT -> R.layout.item_search_piece
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: ArrayList<SearchResultData>){
        items = list
        notifyDataSetChanged()
    }
}