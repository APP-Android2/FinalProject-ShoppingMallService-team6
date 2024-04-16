package kr.co.lion.unipiece.ui.buy.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.ItemBuyBinding
import kr.co.lion.unipiece.model.PieceInfoData
import kr.co.lion.unipiece.model.SearchResultData
import kr.co.lion.unipiece.util.setImage

class BuyPopAdapter (var pieceInfoList: List<PieceInfoData>, private val itemClickListener: (Int) -> Unit) : RecyclerView.Adapter<BuyPopViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType:Int): BuyPopViewHolder {
        val binding: ItemBuyBinding = ItemBuyBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return BuyPopViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: BuyPopViewHolder, position: Int){
        holder.bind(pieceInfoList[position], itemClickListener)
    }

    override fun getItemCount(): Int = pieceInfoList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(list: List<PieceInfoData>){
        pieceInfoList = list
        notifyDataSetChanged()
        Log.d("update adapter", list.toString())
    }
}

class BuyPopViewHolder(val binding: ItemBuyBinding, private val itemClickListener: (Int) -> Unit): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PieceInfoData, itemClickListener: (Int) -> Unit) {
        with(binding) {
            authorName.text = item.authorName
            pieceName.text = item.pieceName
            piecePrice.text = "${item.piecePrice}원"

            root.context.setImage(pieceImg, item.pieceImg)

        }

        // 클릭 리스너 설정, 클릭 시 pieceIdx 전달
        binding.root.setOnClickListener {
            itemClickListener.invoke(item.pieceIdx)
        }
    }
}
