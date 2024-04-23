package kr.co.lion.unipiece.ui.search.adapter

import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.ItemSearchPieceBinding
import kr.co.lion.unipiece.model.PieceInfoData
import kr.co.lion.unipiece.model.SearchAuthorData
import kr.co.lion.unipiece.model.SearchPieceData
import kr.co.lion.unipiece.model.SearchResultData
import kr.co.lion.unipiece.util.setImage
import java.text.DecimalFormat

class SearchPieceViewHolder(
    private val binding: ItemSearchPieceBinding,
    private val itemClickListener: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root){

    init {
        binding.root.setOnClickListener {
            itemClickListener.invoke(adapterPosition)
        }
    }

    fun bind(item: PieceInfoData){

        val priceFormat = DecimalFormat("###,###")
        val price = priceFormat.format(item.piecePrice)

        with(binding) {
            root.context.setImage(pieceImg, item.pieceImg)
            authorName.text = item.authorName
            pieceName.text = item.pieceName
            piecePrice.text = price
        }

    }
}