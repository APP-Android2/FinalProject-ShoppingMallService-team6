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
import com.bumptech.glide.Glide
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentAuthorInfoBinding
import kr.co.lion.unipiece.databinding.RowAuthorPiecesBinding
import kr.co.lion.unipiece.model.AuthorInfoData
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.author.adapter.AuthorPiecesAdapter
import kr.co.lion.unipiece.ui.buy.BuyDetailActivity
import kr.co.lion.unipiece.ui.mypage.ModifyUserInfoFragment
import kr.co.lion.unipiece.ui.mypage.adapter.VisitGalleryAdapter
import kr.co.lion.unipiece.util.AuthorInfoFragmentName
import kr.co.lion.unipiece.util.UserInfoFragmentName
import kr.co.lion.unipiece.util.setMenuIconColor

class AuthorInfoFragment : Fragment() {

    lateinit var fragmentAuthorInfoBinding: FragmentAuthorInfoBinding
    lateinit var authorPiecesAdapter: AuthorPiecesAdapter
    lateinit var authorInfoViewModel: AuthorInfoViewModel

    // 작가 팔로우 여부
    var authorFollow = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentAuthorInfoBinding = FragmentAuthorInfoBinding.inflate(inflater)
        authorInfoViewModel = AuthorInfoViewModel()
        fragmentAuthorInfoBinding.authorInfoViewModel = authorInfoViewModel
        fragmentAuthorInfoBinding.lifecycleOwner = this

        settingToolbar()
        initView()
        settingButtonFollow()
        settingButtonReview()


        return fragmentAuthorInfoBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingRecyclerView()
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
                            val intent = Intent(requireActivity(), MainActivity::class.java)
                                .apply{ // MainActivity가 이미 실행 중인 경우 해당 인스턴스를 재사용합니다.
                                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                }

                            intent.putExtra("HomeFragment", true)
                            requireActivity().finish()
                            startActivity(intent)
                        }
                    }
                    true
                }

            }
        }
    }

    private fun initView(){
        // 작가 데이터 받아오기
        val authorInfoData = AuthorInfoData()
        // 받아온 데이터로 작가 객체 초기화
        
        // 뷰모델과 연결
        authorInfoViewModel.authorName.value = authorInfoData.authorName
        authorInfoViewModel.authorFollower.value = authorInfoData.authorFollower + " 명 팔로우"
        authorInfoViewModel.authorBasic.value = authorInfoData.authorBasic
        authorInfoViewModel.authorInfo.value = authorInfoData.authorInfo

        // 회원 정보 데이터 받아오기

        // 받아온 데이터로 회원 정보 객체 초기화

        // 회원 유형에 따라 팔로우, 리뷰 버튼 표시
        fragmentAuthorInfoBinding.apply {
            
            // 추후 수정
            if(1==authorInfoData.userIdx){
                buttonAuthorFollow.isVisible = true
                buttonAuthorReview.isVisible = true
            }else{
                // 사용자가 해당 작가인 경우
                buttonAuthorFollow.isVisible = false
                buttonAuthorReview.isVisible = false
            }
            
            // 작가 이미지 넣기
            Glide.with(requireActivity())
                .load(authorInfoData.authorImg)
                .into(imageViewAuthor)
        }
    }

    // 팔로우 버튼 클릭
    private fun settingButtonFollow(){
        changeFollowButton()

        fragmentAuthorInfoBinding.buttonAuthorFollow.setOnClickListener {
            authorFollow = !authorFollow
            changeFollowButton()
        }
    }

    // 팔로우 버튼 변경
    private fun changeFollowButton(){
        with(fragmentAuthorInfoBinding.buttonAuthorFollow){
            authorInfoViewModel.changeFollowState(authorFollow)
            setTextColor(authorInfoViewModel.buttonAuthorFollowTextColor.value!!)
            setBackgroundResource(authorInfoViewModel.buttonAuthorFollowBackground.value!!)
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
        // 테스트 데이터
        val piecesList = arrayListOf<Int>(
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_foreground,
        )

        // 리사이클러뷰 어댑터
        authorPiecesAdapter = AuthorPiecesAdapter(piecesList){
            val pieceIntent = Intent(requireActivity(), BuyDetailActivity::class.java)
            startActivity(pieceIntent)
        }

        // 리사이클러뷰 셋팅
        fragmentAuthorInfoBinding.recyclerViewAuthorPieces.apply {
            // 어댑터
            adapter = authorPiecesAdapter
            // 레이아웃 매니저, 가로 방향 셋팅
            layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
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