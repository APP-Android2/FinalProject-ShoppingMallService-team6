package kr.co.lion.unipiece.ui.author

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentAuthorInfoBinding
import kr.co.lion.unipiece.databinding.RowAuthorPiecesBinding
import kr.co.lion.unipiece.databinding.RowVisitGalleryHistoryBinding
import kr.co.lion.unipiece.util.AuthorInfoFragmentName

class AuthorInfoFragment : Fragment() {

    lateinit var fragmentAuthorInfoBinding: FragmentAuthorInfoBinding
    lateinit var authorInfoActivity: AuthorInfoActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentAuthorInfoBinding = FragmentAuthorInfoBinding.inflate(inflater)
        authorInfoActivity = activity as AuthorInfoActivity

        settingToolbar()
        initView()
        settingButtonFollow()
        settingButtonReview()
        settingRecyclerView()

        return fragmentAuthorInfoBinding.root
    }

    // 툴바 셋팅
    private fun settingToolbar(){
        fragmentAuthorInfoBinding.apply {
            toolbarAuthorInfo.apply {

                inflateMenu(R.menu.menu_edit_home)

                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    authorInfoActivity.finish()
                }

                // 회원 유형에 따라 메뉴 아이콘 다르게 표시
                menu.apply {
                    // 작가인 경우 작가 정보 수정 아이콘 표시
                    if(false){
                        getItem(R.id.menu_edit).isVisible = true
                    }else{
                        getItem(R.id.menu_edit).isVisible = false
                    }
                }

                // 툴바 메뉴 클릭 이벤트
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_edit -> {
                            val modifyBundle = Bundle()
                            authorInfoActivity.replaceFragment(AuthorInfoFragmentName.MODIFY_AUTHOR_INFO_FRAGMENT, true, modifyBundle)
                        }
                        R.id.menu_home -> {
                            authorInfoActivity.finish()
                        }
                    }
                    true
                }

            }
        }
    }

    private fun initView(){
        // 회원 유형에 따라 팔로우, 리뷰 버튼 표시
        fragmentAuthorInfoBinding.apply {
            // 작가가 아니면
            if(true){
                buttonAuthorFollow.isVisible = true
                buttonAuthorReview.isVisible = true
            }else{
                buttonAuthorFollow.isVisible = false
                buttonAuthorReview.isVisible = false
            }
        }
    }

    // 팔로우 버튼 클릭
    private fun settingButtonFollow(){
        // 팔로우 버튼 클릭 여부에 따라 버튼 디자인 변경
    }

    // 리뷰 버튼 클릭
    private fun settingButtonReview(){
        // 리뷰 버튼 클릭 시 리뷰 프래그먼트 보이기
        val authorReviewBottomSheetFragment = AuthorReviewBottomSheetFragment()
        authorReviewBottomSheetFragment.show(authorInfoActivity.supportFragmentManager, "BottomSheet")
    }

    // 리사이클러 뷰 셋팅
    private fun settingRecyclerView(){
        fragmentAuthorInfoBinding.recyclerViewAuthorPieces.apply {
            // 어댑터
            adapter = RecyclerViewAdapter()
            // 레이아웃 매니저, 가로 방향 셋팅
            layoutManager = LinearLayoutManager(authorInfoActivity,RecyclerView.HORIZONTAL, false)
            // 데코레이션
            // val deco = MaterialDividerItemDecoration(authorInfoActivity, MaterialDividerItemDecoration.HORIZONTAL)
            // addItemDecoration(deco)
        }
    }

    // 작품 리사이클러 뷰 어댑터
    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){
        inner class ViewHolder(rowAuthorPiecesBinding: RowAuthorPiecesBinding):
            RecyclerView.ViewHolder(rowAuthorPiecesBinding.root){
            val rowAuthorPiecesBinding: RowAuthorPiecesBinding

            init {
                this.rowAuthorPiecesBinding = rowAuthorPiecesBinding

                this.rowAuthorPiecesBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val rowAuthorPiecesBinding = RowAuthorPiecesBinding.inflate(layoutInflater)
            val viewHolder = ViewHolder(rowAuthorPiecesBinding)
            return viewHolder
        }

        override fun getItemCount(): Int {
            return 20
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // 이미지
            holder.rowAuthorPiecesBinding.imageViewAuthorPiece.setImageResource(R.drawable.ic_launcher_background)
        }
    }

}