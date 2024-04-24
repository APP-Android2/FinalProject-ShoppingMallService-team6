package kr.co.lion.unipiece.ui.mygallery.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.RowSalePieceBinding
import kr.co.lion.unipiece.model.PieceAddInfoData
import kr.co.lion.unipiece.ui.mygallery.SalesApplicationActivity
import kr.co.lion.unipiece.util.setImage
import java.text.DecimalFormat

class SalePieceAdapter (val pieceAddInfoList: List<PieceAddInfoData>, private val activityResultLauncher: ActivityResultLauncher<Intent>, private val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<SalePieceViewHolderClass>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SalePieceViewHolderClass {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = RowSalePieceBinding.inflate(inflater, viewGroup, false)

        return SalePieceViewHolderClass(binding, activityResultLauncher)
    }

    override fun getItemCount(): Int {
        return pieceAddInfoList.size
    }

    override fun onBindViewHolder(holder: SalePieceViewHolderClass, position: Int) {
        holder.bind(pieceAddInfoList[position], onItemClick)
    }
}

class SalePieceViewHolderClass(val binding: RowSalePieceBinding, private val activityResultLauncher: ActivityResultLauncher<Intent>): RecyclerView.ViewHolder(binding.root) {
    fun bind(pieceAddInfoData: PieceAddInfoData, onItemClick: (Int) -> Unit) {
        val priceFormat = DecimalFormat("###,###")
        val price = priceFormat.format(pieceAddInfoData.addPiecePrice)

        with(binding) {
            textViewSalePieceState.text = pieceAddInfoData.addPieceState
            textViewRowSalePieceName.text = pieceAddInfoData.addPieceName
            textViewRowSalePieceArtistName.text = pieceAddInfoData.addAuthorName
            textViewRowSalePiecePrice.text = "${price}원"

           if(pieceAddInfoData.addPieceState == "판매 승인 거절") {
               textViewRowSalePieceMessage.text = pieceAddInfoData.addPieceMessage
           } else {
               textViewRowSalePieceMessage.isVisible = false
           }

            if(pieceAddInfoData.addPieceState != "판매 승인 대기") {
                binding.buttonRowSalePieceModify.isVisible = false
            } else {
                binding.buttonRowSalePieceModify.setOnClickListener {
                    val intent = Intent(root.context, SalesApplicationActivity::class.java)
                    intent.putExtra("isModify", true)
                    intent.putExtra("addPieceIdx", pieceAddInfoData.addPieceIdx)
                    intent.putExtra("authorIdx", pieceAddInfoData.authorIdx)
                    activityResultLauncher.launch(intent)
                }
            }

            root.context.setImage(imageViewSalePiece, pieceAddInfoData.addPieceImg)
        }

        binding.root.setOnClickListener {
            onItemClick(adapterPosition)
        }
    }
}