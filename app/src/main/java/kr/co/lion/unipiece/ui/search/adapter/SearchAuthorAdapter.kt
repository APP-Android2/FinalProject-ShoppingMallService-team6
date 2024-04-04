package kr.co.lion.unipiece.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.ItemSearchAuthorBinding

class SearchAuthorAdapter(val imageList: ArrayList<Int>, private val itemClickListener: (Int) -> Unit): RecyclerView.Adapter<SearchAuthorViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAuthorViewHolder {
        val binding: ItemSearchAuthorBinding = ItemSearchAuthorBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SearchAuthorViewHolder(binding, itemClickListener)
    }

    override fun getItemCount(): Int = imageList.size

    override fun onBindViewHolder(holder: SearchAuthorViewHolder, position: Int) {
        holder.authorImg.setImageResource(imageList[position])
    }


}

class SearchAuthorViewHolder(val binding: ItemSearchAuthorBinding, private val itemClickListener: (Int) -> Unit): RecyclerView.ViewHolder(binding.root) {
    val authorImg: ImageView = binding.authorImg
    val itemAuthor: LinearLayout = binding.itemAuthor

    init {
        itemAuthor.setOnClickListener {
            itemClickListener.invoke(position)
        }
    }
}
