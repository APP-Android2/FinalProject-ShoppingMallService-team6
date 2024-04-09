package kr.co.lion.unipiece.ui.author

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentAuthorInfoBinding
import kr.co.lion.unipiece.databinding.RowAuthorPiecesBinding
import kr.co.lion.unipiece.ui.buy.BuyDetailActivity
import kr.co.lion.unipiece.ui.mypage.ModifyUserInfoFragment
import kr.co.lion.unipiece.util.AuthorInfoFragmentName
import kr.co.lion.unipiece.util.UserInfoFragmentName
import kr.co.lion.unipiece.util.setMenuIconColor

class AuthorInfoFragment : Fragment() {

    lateinit var fragmentAuthorInfoBinding: FragmentAuthorInfoBinding

    var authorFollow = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentAuthorInfoBinding = FragmentAuthorInfoBinding.inflate(inflater)

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
                requireContext().setMenuIconColor(menu, R.id.menu_edit, R.color.third)
                requireContext().setMenuIconColor(menu, R.id.menu_home, R.color.second)

                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    requireActivity().finish()
                }

                // 회원 유형에 따라 메뉴 아이콘 다르게 표시
                // 추후 수정 필요
                if(true){
                    // 작가인 경우 작가 정보 수정 아이콘 표시
                    menu.findItem(R.id.menu_edit).isVisible = true
                }else{
                    menu.findItem(R.id.menu_edit).isVisible = false
                }

                // 툴바 메뉴 클릭 이벤트
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_edit -> {
                            // 추후 전달할 데이터는 여기에 담기
                            val modifyBundle = Bundle()
                            // 작가 정보 수정 프래그먼트 교체
                            replaceFragment(modifyBundle)
                        }
                        R.id.menu_home -> {
                            requireActivity().finish()
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
            // 추후 수정
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
        changeFollowButton()

        fragmentAuthorInfoBinding.buttonAuthorFollow.setOnClickListener {
            if(!authorFollow){
                authorFollow = true
            }else{
                authorFollow = false
            }
            changeFollowButton()
        }
    }

    // 팔로우 버튼 변경
    private fun changeFollowButton(){
        val followButton = fragmentAuthorInfoBinding.buttonAuthorFollow
        if(authorFollow){
            followButton.apply {
                setBackgroundResource(R.drawable.button_radius)
                setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                text = "팔로잉"
            }
        }else{
            followButton.apply {
                setBackgroundResource(R.drawable.button_radius2)
                setTextColor(ContextCompat.getColor(requireActivity(), R.color.first))
                text = "팔로우"
            }
        }
    }

    // 리뷰 버튼 클릭
    private fun settingButtonReview(){
        // 리뷰 버튼 클릭 시 리뷰 프래그먼트 보이기
        fragmentAuthorInfoBinding.buttonAuthorReview.setOnClickListener {
            val authorReviewBottomSheetFragment = AuthorReviewBottomSheetFragment()
            authorReviewBottomSheetFragment.show(parentFragmentManager, "BottomSheet")
        }
    }

    // 리사이클러 뷰 셋팅
    private fun settingRecyclerView(){
        fragmentAuthorInfoBinding.recyclerViewAuthorPieces.apply {
            // 어댑터
            adapter = RecyclerViewAdapter()
            // 레이아웃 매니저, 가로 방향 셋팅
            layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
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
                    ViewGroup.LayoutParams.WRAP_CONTENT,
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
            // 작품 클릭 시 설명 화면 이동
            holder.rowAuthorPiecesBinding.root.setOnClickListener {
                val pieceIntent = Intent(requireActivity(), BuyDetailActivity::class.java)
                startActivity(pieceIntent)
            }
        }
    }

    // 프래그먼트 교체 메서드
    private fun replaceFragment(bundle: Bundle){
        val supportFragmentManager = parentFragmentManager.beginTransaction()
        val newFragment = ModifyAuthorInfoFragment()
        newFragment.arguments = bundle
        supportFragmentManager.replace(R.id.fragmentContainerViewAuthorInfo, newFragment)
            .addToBackStack(AuthorInfoFragmentName.MODIFY_AUTHOR_INFO_FRAGMENT.str)
            .commit()
    }
}