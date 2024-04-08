package kr.co.lion.unipiece.ui.search.adapter

import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.ItemSearchPieceBinding

class SearchPieceViewHolder(
    private val binding: ItemSearchPieceBinding,
    private val itemClickListener: (SearchResultData) -> Unit
) : RecyclerView.ViewHolder(binding.root){
    val pieceImage = binding.pieceImg
    val authorName = binding.authorName
    val pieceName = binding.pieceName
    val piecePrice = binding.piecePrice

    fun bind(searchPiecedata: SearchPieceData){
        pieceImage.setImageResource(searchPiecedata.pieceImg)
        authorName.text = searchPiecedata.authorName
        pieceName.text = searchPiecedata.pieceName
        piecePrice.text = searchPiecedata.piecePrice

        binding.root.setOnClickListener {
            itemClickListener.invoke(SearchResultData(SearchAuthorData(), searchPiecedata, SearchResultViewType.PIECE_CONTENT))
        }

    }
}