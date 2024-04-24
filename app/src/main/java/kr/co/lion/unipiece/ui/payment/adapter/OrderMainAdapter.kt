package kr.co.lion.unipiece.ui.payment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.RowCartBinding
import kr.co.lion.unipiece.databinding.RowOrderMainBinding
import kr.co.lion.unipiece.model.PieceInfoData
import kr.co.lion.unipiece.util.setImage
import java.text.DecimalFormat


// 주문하기 화면의 RecyclerView의 어뎁터
class OrderMainAdapter(
    private var orderList: List<PieceInfoData>,
    private val pieceImgOnClickListener: (HashMap<String, Int>) -> Unit,
    private val authorNameOnClickListener: (Int) -> Unit,
) :
    RecyclerView.Adapter<OrderMainViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): OrderMainViewHolder {

        val binding =
            RowOrderMainBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return OrderMainViewHolder(
            viewGroup.context,
            binding,
            pieceImgOnClickListener,
            authorNameOnClickListener
        )
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: OrderMainViewHolder, position: Int) {
        holder.bind(orderList[position])

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(list: List<PieceInfoData>) {
        orderList = list
        notifyDataSetChanged()
        Log.d("update adapter", list.toString())
    }

    // 총합을 계산하는 함수
    fun calculateTotalPrice(): Int {
        return orderList.sumOf { it.piecePrice }
    }


}

class OrderMainViewHolder(
    private val context: Context,
    private val binding: RowOrderMainBinding,
    private val pieceImgOnClickListener: (HashMap<String, Int>) -> Unit,
    private val authorNameOnClickListener: (Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: PieceInfoData) {
        with(binding) {

            // 항목 하나
            with(root) {
                // 클릭 시 범위 지정
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            // 작품 이미지 세팅
            root.context.setImage(imageViewPieceRowOrderMain, data.pieceImg)
            imageViewPieceRowOrderMain.setOnClickListener {
                val pieceIdx = data.pieceIdx
                val authorIdx = data.authorIdx
                val hashMap = HashMap<String,Int>()
                hashMap["pieceIdx"] = pieceIdx
                hashMap["authorIdx"] = authorIdx
                pieceImgOnClickListener.invoke(hashMap)

            }
            // 해당 작품 이름
            textViewRowOrderMainPieceName.text = data.pieceName

            // 해당 작품 작가 이름
            textViewRowOrderMainAuthorName.text = data.authorName
            textViewRowOrderMainAuthorName.setOnClickListener {
                authorNameOnClickListener.invoke(data.authorIdx)
            }
            // 해당 작품 가격 (,표시)
            val dec = DecimalFormat("#,###")
            val result = data.piecePrice
            textViewRowOrderMainPiecePrice.text = "${dec.format(result)} 원"

        }
    }
}