package kr.co.lion.unipiece.ui.buy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ItemBuyBinding
import kr.co.lion.unipiece.model.PieceInfoData

class BuyPopAdapter (val pieceInfoList: List<PieceInfoData>, private val itemClickListener: (Int) -> Unit) : RecyclerView.Adapter<BuyPopViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType:Int): BuyPopViewHolder {
        val binding: ItemBuyBinding = ItemBuyBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return BuyPopViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: BuyPopViewHolder, position: Int){
        holder.bind(pieceInfoList[position], itemClickListener)
    }

    override fun getItemCount(): Int = pieceInfoList.size
}

class BuyPopViewHolder(val binding: ItemBuyBinding, private val itemClickListener: (Int) -> Unit): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PieceInfoData, itemClickListener: (Int) -> Unit) {
        with(binding) {
            authorName.text = item.authorName
            pieceName.text = item.pieceName
            piecePrice.text = "${item.piecePrice}원"

            Glide.with(root).load(item.pieceImg)
                .placeholder(R.drawable.ic_launcher_foreground) // 로딩 중일 때
                .error(R.drawable.icon) // 오류 발생 시
                .into(pieceImg)
        }

        // 클릭 리스너 설정, 클릭 시 pieceIdx 전달
        binding.root.setOnClickListener {
            itemClickListener.invoke(item.pieceIdx)
        }
    }
}
