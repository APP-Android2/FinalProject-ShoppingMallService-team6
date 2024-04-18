package kr.co.lion.unipiece.ui.infomation

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.InfoAllBinding
import kr.co.lion.unipiece.model.PieceInfoData
import kr.co.lion.unipiece.model.PromoteInfoData
import kr.co.lion.unipiece.util.setImage
import kotlin.coroutines.coroutineContext

class InfoAllAdapter(var promoteInfoList: List<PromoteInfoData>) : RecyclerView.Adapter<ViewHolderClass>() {

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
        return promoteInfoList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        holder.infoAllBinding.root.context.setImage(holder.infoAllBinding.imageView2, promoteInfoList[position].promoteImg)
        holder.infoAllBinding.textInfoAllTitle.text = promoteInfoList[position].promoteName
        holder.infoAllBinding.textInfoAllDate.text = promoteInfoList[position].promoteDate
        holder.infoAllBinding.textInfoAllAuthorName.text = promoteInfoList[position].promotePlace
        holder.infoAllBinding.root.setOnClickListener {
            itemOnClickListener.recyclerviewClickListener()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(list: List<PromoteInfoData>){
        promoteInfoList = list
        notifyDataSetChanged()
        Log.d("update adapter", list.toString())
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