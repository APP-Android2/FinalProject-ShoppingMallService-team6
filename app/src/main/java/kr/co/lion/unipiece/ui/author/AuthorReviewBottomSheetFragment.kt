package kr.co.lion.unipiece.ui.author

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentAuthorReviewBottomSheetBinding
import kr.co.lion.unipiece.databinding.RowAuthorPiecesBinding
import kr.co.lion.unipiece.databinding.RowAuthorReviewBottomSheetBinding

class AuthorReviewBottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var fragmentAuthorReviewBottomSheetBinding: FragmentAuthorReviewBottomSheetBinding
    lateinit var authorInfoActivity: AuthorInfoActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentAuthorReviewBottomSheetBinding = FragmentAuthorReviewBottomSheetBinding.inflate(inflater)
        authorInfoActivity = activity as AuthorInfoActivity

        settingRecyclerView()
        settingButtonAuthorReviewAdd()
        
        return fragmentAuthorReviewBottomSheetBinding.root
    }


    // 리사이클러 뷰 셋팅
    private fun settingRecyclerView(){
        fragmentAuthorReviewBottomSheetBinding.recyclerViewAuthorReview.apply {
            // 어댑터
            adapter = RecyclerViewAdapter()
            // 레이아웃 매니저, 가로 방향 셋팅
            layoutManager = LinearLayoutManager(authorInfoActivity)
            // 데코레이션
            val deco = MaterialDividerItemDecoration(authorInfoActivity, MaterialDividerItemDecoration.VERTICAL)
            addItemDecoration(deco)
        }
    }

    // 작품 리사이클러 뷰 어댑터
    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){
        inner class ViewHolder(rowAuthorReviewBottomSheetBinding: RowAuthorReviewBottomSheetBinding):
            RecyclerView.ViewHolder(rowAuthorReviewBottomSheetBinding.root){
            val rowAuthorReviewBottomSheetBinding: RowAuthorReviewBottomSheetBinding

            init {
                this.rowAuthorReviewBottomSheetBinding = rowAuthorReviewBottomSheetBinding

                this.rowAuthorReviewBottomSheetBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val rowAuthorReviewBottomSheetBinding = RowAuthorReviewBottomSheetBinding.inflate(layoutInflater)
            val viewHolder = ViewHolder(rowAuthorReviewBottomSheetBinding)
            return viewHolder
        }

        override fun getItemCount(): Int {
            return 20
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // 닉네임
            holder.rowAuthorReviewBottomSheetBinding.textViewRowAuthorReviewNickName.text = "김토끼 $position"
            // 댓글 내용
            holder.rowAuthorReviewBottomSheetBinding.textViewRowAuthorReviewText.text = "홍작가님 작품 너무 대박이에요"
            // 삭제 버튼은 본인 댓글만 보여지게
            if(true){
                holder.rowAuthorReviewBottomSheetBinding.buttonRowAuthorReviewDelete.isVisible = true
            }else{
                holder.rowAuthorReviewBottomSheetBinding.buttonRowAuthorReviewDelete.isVisible = false
            }
        }
    }

    // 확인 버튼
    private fun settingButtonAuthorReviewAdd(){
        // 작성 내용 체크
        
        // 작성 내용 저장
    }
}