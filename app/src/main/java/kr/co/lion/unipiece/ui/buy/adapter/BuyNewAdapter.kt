package kr.co.lion.unipiece.ui.buy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.ItemBuyBinding
import kr.co.lion.unipiece.model.PieceInfoData
import kr.co.lion.unipiece.util.setImage

class BuyNewAdapter (val pieceInfoList: List<PieceInfoData>, private val itemClickListener: (Int) -> Unit) : RecyclerView.Adapter<BuyNewViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType:Int): BuyNewViewHolder {
        val binding: ItemBuyBinding = ItemBuyBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return BuyNewViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: BuyNewViewHolder, position: Int){
        holder.bind(pieceInfoList[position], itemClickListener)
    }

    override fun getItemCount(): Int = pieceInfoList.size
}

class BuyNewViewHolder(val binding: ItemBuyBinding, private val itemClickListener: (Int) -> Unit): RecyclerView.ViewHolder(binding.root) {
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
