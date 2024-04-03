package kr.co.lion.unipiece.ui.buy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.ItemBuyBinding

class BuyNewAdapter (val imageList: ArrayList<Int>, private val itemClickListener: (Int) -> Unit) : RecyclerView.Adapter<BuyNewViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType:Int): BuyNewViewHolder {
        val binding: ItemBuyBinding = ItemBuyBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return BuyNewViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: BuyNewViewHolder, position: Int){
        holder.pieceImg.setImageResource(imageList[position])
    }

    override fun getItemCount(): Int = imageList.size
}

class BuyNewViewHolder(val binding: ItemBuyBinding, private val itemClickListener: (Int) -> Unit): RecyclerView.ViewHolder(binding.root) {
    val pieceImg: ImageView = binding.pieceImg
    val itemPiece: LinearLayout = binding.itemPiece
    init {
        itemPiece.setOnClickListener {
            itemClickListener.invoke(position)
        }
    }
}
