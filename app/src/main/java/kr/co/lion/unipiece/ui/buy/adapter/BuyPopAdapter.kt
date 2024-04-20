package kr.co.lion.unipiece.ui.buy.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.databinding.ItemBuyBinding
import kr.co.lion.unipiece.model.PieceInfoData
import kr.co.lion.unipiece.model.SearchResultData
import kr.co.lion.unipiece.util.setImage
import java.text.DecimalFormat

class BuyPopAdapter (var pieceInfoList: List<PieceInfoData>, private val itemClickListener: (Int) -> Unit) : RecyclerView.Adapter<BuyPopViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType:Int): BuyPopViewHolder {
        val binding: ItemBuyBinding = ItemBuyBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return BuyPopViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: BuyPopViewHolder, position: Int){
        holder.bind(pieceInfoList[position])
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

    init {
        // 클릭 리스너 설정, 클릭 시 pieceIdx 전달
        binding.root.setOnClickListener {
            itemClickListener.invoke(adapterPosition)
        }
    }

    fun bind(item: PieceInfoData) {
        val priceFormat = DecimalFormat("###,###")
        val price = priceFormat.format(item.piecePrice)

        with(binding) {
            authorName.text = item.authorName
            pieceName.text = item.pieceName
            piecePrice.text = "${price}원"

            root.context.setImage(pieceImg, item.pieceImg)

        }
    }
}
