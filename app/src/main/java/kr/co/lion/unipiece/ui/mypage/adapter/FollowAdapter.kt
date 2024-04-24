package kr.co.lion.unipiece.ui.mypage.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.RowFollowBinding
import kr.co.lion.unipiece.model.AuthorInfoData
import kr.co.lion.unipiece.model.DeliveryData
import kr.co.lion.unipiece.ui.author.AuthorInfoActivity
import kr.co.lion.unipiece.util.CustomDialog

// 메인 화면의 RecyclerView의 어뎁터
class FollowAdapter(
    private var authorinfoList: List<AuthorInfoData>,
    private val rowClickListener: (Int) -> Unit,
    private val followCancelButtonOnClickListener: (Int) -> Unit
) : RecyclerView.Adapter<FollowViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FollowViewHolder {

        val binding = RowFollowBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return FollowViewHolder(
            viewGroup.context,
            binding,
            rowClickListener,
            followCancelButtonOnClickListener
        )
    }

    override fun getItemCount(): Int {
        return authorinfoList.size
    }

    override fun onBindViewHolder(holder: FollowViewHolder, position: Int) {
        holder.bind(authorinfoList[position],rowClickListener)
    }


}

class FollowViewHolder(
    private val context: Context,
    private val binding: RowFollowBinding,
    private val rowClickListener: (Int) -> Unit,
    private val followCancelButtonOnClickListener: (Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: AuthorInfoData, rowClickListener: (Int) -> Unit) {

        with(binding) {

            // 항목 클릭 시
            root.setOnClickListener{
                rowClickListener.invoke(data.authorIdx)
            }

            // 프로필 사진을 클릭 시
            ivProfileImage.setOnClickListener {
                rowClickListener.invoke(data.authorIdx)
            }

            // 항목별 팔로우 취소 텍스트 버튼 클릭 시
            textButtonFollowCancel.setOnClickListener {
                followCancelButtonOnClickListener.invoke(data.authorIdx)
            }

            // 항목 클릭 시 클릭되는 범위 설정
            root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }





    }
}