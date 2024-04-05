package kr.co.lion.unipiece.ui.mygallery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.RowSalePieceBinding

class SalePieceAdapter (private val onItemClick: (position: Int) -> Unit): RecyclerView.Adapter<SalePieceViewHolderClass>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalePieceViewHolderClass {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowSalePieceBinding.inflate(inflater, parent, false)
        return SalePieceViewHolderClass(binding, onItemClick)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: SalePieceViewHolderClass, position: Int) {
        holder.bind(position)
    }
}

class SalePieceViewHolderClass(private val binding: RowSalePieceBinding, onItemClick: (position: Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {
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
        binding.textViewRowSalePieceName.text = "$position"
    }
}