package kr.co.lion.unipiece.ui.mygallery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.RowInterestingPieceBinding

class InterestingPieceAdapter(private val onItemClick: (position: Int) -> Unit): RecyclerView.Adapter<InterestingPieceViewHolderClass>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestingPieceViewHolderClass {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowInterestingPieceBinding.inflate(inflater, parent, false)
        return InterestingPieceViewHolderClass(binding, onItemClick)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: InterestingPieceViewHolderClass, position: Int) {
        holder.bind(position)
    }
}

class InterestingPieceViewHolderClass(private val binding: RowInterestingPieceBinding, onItemClick: (position: Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {
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
        binding.textViewRowInterestingPieceName.text = "$position"
    }
}
