package kr.co.lion.unipiece.ui.mygallery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.RowPurchasedPieceBinding

class PurchasedPieceAdapter (private val onItemClick: (position: Int) -> Unit): RecyclerView.Adapter<PurchasedPieceViewHolderClass>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchasedPieceViewHolderClass {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowPurchasedPieceBinding.inflate(inflater, parent, false)
        return PurchasedPieceViewHolderClass(binding, onItemClick)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: PurchasedPieceViewHolderClass, position: Int) {
        holder.bind(position)
    }
}

class PurchasedPieceViewHolderClass(private val binding: RowPurchasedPieceBinding, onItemClick: (position: Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClick(position)
            }
        }

        binding.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

    }

    fun bind(position: Int) {
        binding.textViewRowPurchasedPieceName.text = "$position"
    }
}