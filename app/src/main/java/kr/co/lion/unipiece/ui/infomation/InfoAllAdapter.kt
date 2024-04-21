package kr.co.lion.unipiece.ui.infomation

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.InfoAllBinding
import kr.co.lion.unipiece.model.GalleryInfoData
import kr.co.lion.unipiece.model.NewsInfoData
import kr.co.lion.unipiece.model.PromoteInfoData
import kr.co.lion.unipiece.util.setImage

class InfoAllAdapter(var promoteInfoList: List<PromoteInfoData>, var newsInfoList: List<NewsInfoData>, var galleryInfoList: List<GalleryInfoData>) : RecyclerView.Adapter<ViewHolderClass>() {

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
        return if (!promoteInfoList.isEmpty()){
            promoteInfoList.size
        }else if (!newsInfoList.isEmpty()) {
            newsInfoList.size
        }else{
            galleryInfoList.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        if (!promoteInfoList.isEmpty()){
            holder.infoAllBinding.root.context.setImage(holder.infoAllBinding.imageView2, promoteInfoList[position].promoteImg)
            holder.infoAllBinding.textInfoAllTitle.text = promoteInfoList[position].promoteName
            holder.infoAllBinding.textInfoAllDate.text = promoteInfoList[position].promoteDate
            holder.infoAllBinding.textInfoAllAuthorName.text = promoteInfoList[position].promotePlace
            holder.infoAllBinding.root.setOnClickListener {
                itemOnClickListener.recyclerviewClickListener(promoteInfoList[position].promoteImg)
            }
        }else if (!newsInfoList.isEmpty()){
            holder.infoAllBinding.root.context.setImage(holder.infoAllBinding.imageView2, newsInfoList[position].newsImg)
            holder.infoAllBinding.textInfoAllTitle.text = newsInfoList[position].newsName
            holder.infoAllBinding.textInfoAllDate.text = newsInfoList[position].newsDate
            holder.infoAllBinding.textInfoAllAuthorName.text = newsInfoList[position].newsSale
            holder.infoAllBinding.root.setOnClickListener {
                itemOnClickListener.recyclerviewClickListener(newsInfoList[position].newsImg)
            }
        }else{
            holder.infoAllBinding.root.context.setImage(holder.infoAllBinding.imageView2, galleryInfoList[position].galleryInfoImg)
            holder.infoAllBinding.textInfoAllTitle.text = galleryInfoList[position].galleryInfoName
            holder.infoAllBinding.textInfoAllDate.text = galleryInfoList[position].galleryInfoDate
            holder.infoAllBinding.textInfoAllAuthorName.text = galleryInfoList[position].galleryInfoAuthor
            holder.infoAllBinding.root.setOnClickListener {
                //view를 클릭하면 이미지의 이름을 넘겨준다
                itemOnClickListener.recyclerviewClickListener(galleryInfoList[position].galleryInfoImg)
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(list: List<PromoteInfoData>){
        promoteInfoList = list
        notifyDataSetChanged()
        Log.d("update adapter", list.toString())
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataNews(list: List<NewsInfoData>){
        newsInfoList = list
        notifyDataSetChanged()
        Log.d("update adapter", list.toString())
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataGallery(list: List<GalleryInfoData>){
        galleryInfoList = list
        notifyDataSetChanged()
        Log.d("update adapter", list.toString())
    }

    interface ItemOnClickListener{
        fun recyclerviewClickListener(image: String?)
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