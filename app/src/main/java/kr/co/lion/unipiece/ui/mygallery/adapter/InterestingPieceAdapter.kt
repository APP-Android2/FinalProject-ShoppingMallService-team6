package kr.co.lion.unipiece.ui.mygallery.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.RowInterestingPieceBinding
import kr.co.lion.unipiece.model.PieceInfoData
import kr.co.lion.unipiece.util.setImage
import java.text.DecimalFormat

class InterestingPieceAdapter(val likePieceDataList: List<PieceInfoData>, private val onItemClick: (Int) -> Unit, private val onLikeClick: (Int) -> Unit) : RecyclerView.Adapter<InterestingPieceViewHolderClass>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): InterestingPieceViewHolderClass {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = RowInterestingPieceBinding.inflate(inflater, viewGroup, false)

        return InterestingPieceViewHolderClass(binding)
    }

    override fun getItemCount(): Int {
        return likePieceDataList.size
    }

    override fun onBindViewHolder(holder: InterestingPieceViewHolderClass, position: Int) {
        holder.bind(likePieceDataList[position], onItemClick, onLikeClick)
    }
}

class InterestingPieceViewHolderClass(val binding: RowInterestingPieceBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(likePieceData: PieceInfoData, onItemClick: (Int) -> Unit, onLikeClick: (Int) -> Unit) {
        val priceFormat = DecimalFormat("###,###")
        val price = priceFormat.format(likePieceData.piecePrice)

        with(binding) {
            textViewRowInterestingPieceName.text = likePieceData.pieceName
            textViewRowInterestingPieceArtistName.text = likePieceData.authorName
            textViewRowInterestingPiecePrice.text = "${price}원"
            root.context.setImage(imageViewRowInterestingPiece, likePieceData.pieceImg)

            imageViewLikedPiece.setOnClickListener {
                onLikeClick(adapterPosition)
            }
        }

        binding.root.setOnClickListener {
            onItemClick(adapterPosition)
        }
    }
}
