package kr.co.lion.unipiece.ui.mygallery.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.RowSalePieceBinding
import kr.co.lion.unipiece.model.PieceAddInfoData
import kr.co.lion.unipiece.ui.mygallery.SalesApplicationActivity
import kr.co.lion.unipiece.util.setImage

class SalePieceAdapter (val pieceAddInfoList: List<PieceAddInfoData>, private val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<SalePieceViewHolderClass>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SalePieceViewHolderClass {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = RowSalePieceBinding.inflate(inflater, viewGroup, false)

        return SalePieceViewHolderClass(binding)
    }

    override fun getItemCount(): Int {
        return pieceAddInfoList.size
    }

    override fun onBindViewHolder(holder: SalePieceViewHolderClass, position: Int) {
        holder.bind(pieceAddInfoList[position], onItemClick)
    }
}

class SalePieceViewHolderClass(val binding: RowSalePieceBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(pieceAddInfoData: PieceAddInfoData, onItemClick: (Int) -> Unit) {
        with(binding) {
            root.context.setImage(imageViewSalePiece, pieceAddInfoData.addPieceImg)

            textViewSalePieceState.text = pieceAddInfoData.addPieceState
            textViewRowSalePieceName.text = pieceAddInfoData.addPieceName
            textViewRowSalePieceArtistName.text = pieceAddInfoData.addAuthorName
            textViewRowSalePiecePrice.text = "${pieceAddInfoData.addPiecePrice}원"

            if(pieceAddInfoData.addPieceState != "판매 승인 대기") {
                binding.buttonRowSalePieceModify.isVisible = false
            } else {
                binding.buttonRowSalePieceModify.setOnClickListener {
                    val intent = Intent(root.context, SalesApplicationActivity::class.java)
                    intent.putExtra("isModify", true)
                    root.context.startActivity(intent)
                }
            }
        }

        binding.root.setOnClickListener {
            onItemClick(adapterPosition)
        }
    }
}