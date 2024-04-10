package kr.co.lion.unipiece.ui.infomation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.InfoAllBinding

class InfoAllAdapter() : RecyclerView.Adapter<ViewHolderClass>() {

    private lateinit var itemOnClickListener: ItemOnClickListener

    fun setRecyclerviewClickListener(itemOnClickListener: ItemOnClickListener){
        this.itemOnClickListener = itemOnClickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {

        val layoutInflater = LayoutInflater.from(parent.context)

        var infoAllBinding = InfoAllBinding.inflate(layoutInflater)
        var viewHolder = ViewHolderClass(infoAllBinding)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return 30
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        holder.infoAllBinding.imageView2.setImageResource(R.drawable.icon)
        holder.infoAllBinding.textInfoAllTitle.text = "UniPiece"
        holder.infoAllBinding.textInfoAllDate.text = "2024-04-01 ~ 2024-04-26"
        holder.infoAllBinding.textInfoAllAuthorName.text = "멋쟁이 사람들"
        holder.infoAllBinding.root.setOnClickListener {
            itemOnClickListener.recyclerviewClickListener()
        }
    }

    interface ItemOnClickListener{
        fun recyclerviewClickListener()
    }
}

class ViewHolderClass(infoAllBinding: InfoAllBinding):RecyclerView.ViewHolder(infoAllBinding.root){
    var infoAllBinding : InfoAllBinding

    init {
        this.infoAllBinding = infoAllBinding
        this.infoAllBinding.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

    }
}