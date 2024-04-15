package kr.co.lion.unipiece.ui.mypage.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.RowFollowBinding
import kr.co.lion.unipiece.ui.author.AuthorInfoActivity
import kr.co.lion.unipiece.util.CustomDialog

// 메인 화면의 RecyclerView의 어뎁터
class FollowAdapter :
    RecyclerView.Adapter<FollowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val rowFollowBinding = RowFollowBinding.inflate(inflater, parent, false)
        val followViewHolder = FollowViewHolder(parent.context, rowFollowBinding)
        return followViewHolder
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: FollowViewHolder, position: Int) {
        holder.rowFollowBinding.textViewFollowListAuthorName.text = "홍길동"
    }


}

class FollowViewHolder(context: Context, rowFollowBinding: RowFollowBinding) :
    RecyclerView.ViewHolder(rowFollowBinding.root) {
    val rowFollowBinding: RowFollowBinding

    init {
        this.rowFollowBinding = rowFollowBinding

        // 프로필 사진을 클릭 시
        this.rowFollowBinding.ivProfileImage.setOnClickListener {
            val authorInfoIntent = Intent(context, AuthorInfoActivity::class.java)
            context.startActivity(authorInfoIntent)
        }

        // 항목별 팔로우 취소 텍스트 버튼 클릭 시
        this.rowFollowBinding.textButtonFollowCancel.setOnClickListener {
            val followCancelDialog = CustomDialog("팔로우 취소", "홍길동 작가 팔로우를 취소하시겠습니까?")
            //followCancelDialog.show(,"FollowCancelCustomDialog")
            followCancelDialog.setButtonClickListener(object : CustomDialog.OnButtonClickListener {
                override fun okButtonClick() {

                }

                override fun noButtonClick() {

                }

            })
        }

        this.rowFollowBinding.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}