package kr.co.lion.unipiece.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.AuthorListBinding

class AuthorAdapter : RecyclerView.Adapter<AuthorViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        val authorListBinding = AuthorListBinding.inflate(layoutInflater)
        val authorViewHolder = AuthorViewHolder(authorListBinding)
        return authorViewHolder
    }

    override fun getItemCount(): Int {
        return 15
    }

    override fun onBindViewHolder(holder: AuthorViewHolder, position: Int) {
        holder.authorListBinding.imageAuthorNameList.setImageResource(R.drawable.mypage_icon)
        holder.authorListBinding.textAuthorNameList.text = "작가명"
    }
}

class AuthorViewHolder(authorListBinding: AuthorListBinding):RecyclerView.ViewHolder(authorListBinding.root){
    val authorListBinding: AuthorListBinding

    init {
        this.authorListBinding = authorListBinding
    }
}