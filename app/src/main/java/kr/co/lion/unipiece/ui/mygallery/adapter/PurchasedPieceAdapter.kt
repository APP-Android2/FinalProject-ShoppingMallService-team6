package kr.co.lion.unipiece.ui.mygallery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.RowPurchasedPieceBinding
import kr.co.lion.unipiece.model.PieceBuyInfoData
import kr.co.lion.unipiece.model.PieceInfoData
import kr.co.lion.unipiece.util.setImage
import java.text.DecimalFormat

class PurchasedPieceAdapter (val pieceBuyInfoList: List<Pair<PieceBuyInfoData, PieceInfoData?>>, private val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<PurchasedPieceViewHolderClass>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PurchasedPieceViewHolderClass {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = RowPurchasedPieceBinding.inflate(inflater, viewGroup, false)

        return PurchasedPieceViewHolderClass(binding)
    }

    override fun getItemCount(): Int {
        return pieceBuyInfoList.size
    }

    override fun onBindViewHolder(holder: PurchasedPieceViewHolderClass, position: Int) {
        holder.bind(pieceBuyInfoList[position], onItemClick)
    }
}

class PurchasedPieceViewHolderClass(private val binding: RowPurchasedPieceBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(pair: Pair<PieceBuyInfoData, PieceInfoData?>, onItemClick: (Int) -> Unit) {
        val pieceBuyInfoData = pair.first
        val pieceInfoData = pair.second

        with(binding) {
            textViewRowPurchasedPieceState.text = pieceBuyInfoData.pieceBuyState

            if (pieceInfoData != null) {
                val priceFormat = DecimalFormat("###,###")
                val price = priceFormat.format(pieceInfoData.piecePrice)

                textViewRowPurchasedPieceName.text = pieceInfoData.pieceName
                textViewRowPurchasedPieceArtistName.text = pieceInfoData.authorName
                textViewRowPurchasedPiecePrice.text = "${price}원"
                root.context.setImage(imageViewRowPurchasedPiece, pieceInfoData.pieceImg)
            }
        }

        binding.root.setOnClickListener {
            onItemClick(adapterPosition)
        }
    }
}