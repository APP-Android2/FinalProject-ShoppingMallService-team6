package kr.co.lion.unipiece.ui.payment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.RowCartBinding
import kr.co.lion.unipiece.model.CartData
import kr.co.lion.unipiece.model.DeliveryData
import kr.co.lion.unipiece.model.PieceInfoData
import kr.co.lion.unipiece.util.setImage
import java.text.DecimalFormat

class CartAdapter(
    private var cartList: List<PieceInfoData>,
    private val pieceImgOnClickListener: (HashMap<String,Int>) -> Unit,
    private val authorNameOnClickListener : (Int) -> Unit,
) : RecyclerView.Adapter<CartViewHolder>() {

    fun getCurrentData(): List<PieceInfoData> {
        return cartList
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CartViewHolder {
        val binding =
            RowCartBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return CartViewHolder(
            viewGroup.context, binding, pieceImgOnClickListener, authorNameOnClickListener
        )
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartList[position])

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(list: List<PieceInfoData>) {
        cartList = list
        notifyDataSetChanged()
        Log.d("update adapter", list.toString())
    }
}

// 리사이클러뷰 뷰홀더
class CartViewHolder(
    private val context: Context,
    private val binding: RowCartBinding,
    private val pieceImgOnClickListener: (HashMap<String,Int>) -> Unit,
    private val authorNameOnClickListener: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        data: PieceInfoData
    ) {
        with(binding) {

            // 항목 하나
            with(root) {
                layoutParams = ViewGroup.LayoutParams(
                    // 클릭되는 범위 지정
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            // 작품 이미지 세팅
            root.context.setImage(imageView, data.pieceImg)
            imageView.setOnClickListener {
                val pieceIdx = data.pieceIdx
                val authorIdx = data.authorIdx
                var hashMap = HashMap<String,Int>()
                hashMap["pieceIdx"] = pieceIdx
                hashMap["authorIdx"] = authorIdx
                pieceImgOnClickListener.invoke(hashMap)

            }
            // 해당 작품 이름
            textViewRowCartPieceName.text = data.pieceName

            // 해당 작품 작가 이름
            textViewRowCartAuthorName.text = data.authorName
            textViewRowCartAuthorName.setOnClickListener {
                authorNameOnClickListener.invoke(data.authorIdx)
            }
            // 해당 작품 가격 (,표시)
            val dec = DecimalFormat("#,###")
            val result = data.piecePrice
            textViewRowCartPiecePrice.text = "${dec.format(result)} 원"




        }
    }
}


