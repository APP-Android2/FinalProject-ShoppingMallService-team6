package kr.co.lion.unipiece.ui.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.AuthorListBinding
import kr.co.lion.unipiece.model.AuthorInfoData
import kr.co.lion.unipiece.model.PromoteInfoData
import kr.co.lion.unipiece.repository.AuthorInfoRepository
import kr.co.lion.unipiece.util.getImageUrlFromAuthorInfo
import kr.co.lion.unipiece.util.setImage


class AuthorAdapter(var authorInfoDataList: List<AuthorInfoData>) : RecyclerView.Adapter<AuthorViewHolder>() {

    private lateinit var authorItemClickListener:AuthorOnClickListener


    fun setRecyclerviewClickListener(authorItemOnClickListener: AuthorOnClickListener){
        this.authorItemClickListener = authorItemOnClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        val authorListBinding = AuthorListBinding.inflate(layoutInflater)
        val authorViewHolder = AuthorViewHolder(authorListBinding)
        return authorViewHolder
    }

    override fun getItemCount(): Int {
        return authorInfoDataList.size
    }

    override fun onBindViewHolder(holder: AuthorViewHolder, position: Int) {
        val authorImage = authorInfoDataList[position].authorImg
        //Log.d("seonguk1234", "${authorImage}")
        val imageName = holder.authorListBinding.root.context.getImageUrlFromAuthorInfo(authorImage)
        holder.authorListBinding.root.context.setImage(holder.authorListBinding.imageAuthorNameList, imageName)
        holder.authorListBinding.textAuthorNameList.text = authorInfoDataList[position].authorName
        holder.authorListBinding.root.setOnClickListener {
            authorItemClickListener.authorItemClickListener()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(list: List<AuthorInfoData>){
        authorInfoDataList = list
        notifyDataSetChanged()
        Log.d("update adapter", list.toString())
    }

    interface AuthorOnClickListener{
        fun authorItemClickListener()
    }
}

class AuthorViewHolder(authorListBinding: AuthorListBinding):RecyclerView.ViewHolder(authorListBinding.root){
    val authorListBinding: AuthorListBinding

    init {
        this.authorListBinding = authorListBinding
    }
}