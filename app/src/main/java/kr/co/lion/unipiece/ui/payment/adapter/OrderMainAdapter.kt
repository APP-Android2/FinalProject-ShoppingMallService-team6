package kr.co.lion.unipiece.ui.payment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.RowOrderMainBinding


// 주문하기 화면의 RecyclerView의 어뎁터
class OrderMainAdapter :
    RecyclerView.Adapter<OrderMainViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderMainViewHolder {

        val rowOrderMainBinding = RowOrderMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return OrderMainViewHolder(rowOrderMainBinding)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: OrderMainViewHolder, position: Int) {
        holder.rowOrderMainBinding.textViewRowOrderMainAuthorName.text = "작가 이름"
        holder.rowOrderMainBinding.textViewRowOrderMainPieceName.text = "작품 이름"
        holder.rowOrderMainBinding.textViewRowOrderMainAddDeliveryPrice.text = "추가 배송비 4,000원"
        holder.rowOrderMainBinding.textViewRowOrderMainPiecePrice.text = "작품 가격"

    }
}

class OrderMainViewHolder(rowOrderMainBinding: RowOrderMainBinding) :
    RecyclerView.ViewHolder(rowOrderMainBinding.root) {
    val rowOrderMainBinding: RowOrderMainBinding

    init {
        this.rowOrderMainBinding = rowOrderMainBinding
        this.rowOrderMainBinding.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}