package kr.co.lion.unipiece.ui.author

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentAuthorInfoBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.author.adapter.AuthorPiecesAdapter
import kr.co.lion.unipiece.ui.buy.BuyDetailActivity
import kr.co.lion.unipiece.util.AuthorInfoFragmentName
import kr.co.lion.unipiece.util.setMenuIconColor

class AuthorInfoFragment : Fragment() {

    lateinit var fragmentAuthorInfoBinding: FragmentAuthorInfoBinding
    lateinit var authorPiecesAdapter: AuthorPiecesAdapter
    lateinit var authorInfoViewModel: AuthorInfoViewModel

    // 이전 액티비티에서 작가Idx, 회원Idx 받아오기 추후 수정 필요
    // val authorIdx = requireArguments().getInt("authorIdx",1)
    val authorIdx = 1
    val userIdx = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentAuthorInfoBinding = FragmentAuthorInfoBinding.inflate(inflater)
        authorInfoViewModel = AuthorInfoViewModel()
        fragmentAuthorInfoBinding.authorInfoViewModel = authorInfoViewModel
        fragmentAuthorInfoBinding.lifecycleOwner = this

        lifecycleScope.launch(Dispatchers.Main){
            fetchData()
            initView()
            settingToolbar()
            settingButtonFollow()
            settingButtonReview()
        }

        return fragmentAuthorInfoBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingRecyclerView()
    }

    private suspend fun fetchData(){
        val job1 = lifecycleScope.launch {
            // 작가 정보 불러오기
            authorInfoViewModel.getAuthorInfoData(authorIdx)
            // 팔로워 수 불러오기
            authorInfoViewModel.getFollowCount(authorIdx)
        }
        job1.join()
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
                menu.findItem(R.id.menu_edit).isVisible = false
                lifecycleScope.launch {
                    // 추후 수정 필요
                    val authorCheck = authorInfoViewModel!!.checkAuthor(userIdx)
                    if(authorCheck){ // 나중에 ! 제거
                        // 작가 본인인 경우 작가 정보 수정 아이콘 표시
                        menu.findItem(R.id.menu_edit).isVisible = true
                    }
                }

                // 툴바 메뉴 클릭 이벤트
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_edit -> {
                            // 추후 전달할 데이터는 여기에 담기
                            val modifyBundle = Bundle()
                            // 작가idx 전달
                            modifyBundle.putInt("authorIdx", authorIdx)
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
        with(fragmentAuthorInfoBinding){
            lifecycleScope.launch {
                // 추후 수정 필요
                val authorCheck = authorInfoViewModel!!.checkAuthor(userIdx)
                // 회원 유형에 따라 팔로우, 리뷰 버튼 표시
                // 추후 수정
                if(authorCheck){
                    // 사용자가 해당 작가인 경우
                    buttonAuthorFollow.isVisible = false
                    buttonAuthorReview.isVisible = false
                }
            }
            // 작가 이미지 셋팅
            Glide.with(requireActivity())
                .load(authorInfoViewModel!!.authorInfoData.value?.authorImg)
                .into(imageViewAuthor)
        }
    }

    // 팔로우 버튼 셋팅
    private fun settingButtonFollow(){
        // 팔로우 여부 체크
        authorInfoViewModel.checkFollow(userIdx, authorIdx)

        // 팔로우 상태에 따라 버튼 변경
        authorInfoViewModel.checkFollow.observe(viewLifecycleOwner, Observer {
            with(fragmentAuthorInfoBinding.buttonAuthorFollow){
                if(it){
                    setBackgroundResource(R.drawable.button_radius)
                    setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                    text = "팔로잉"
                }else{
                    setBackgroundResource(R.drawable.button_radius2)
                    setTextColor(ContextCompat.getColor(requireActivity(), R.color.first))
                    text = "팔로우"
                }
            }
        })

        // 팔로우 버튼 클릭 시
        fragmentAuthorInfoBinding.buttonAuthorFollow.setOnClickListener {
            lifecycleScope.launch {
                if(authorInfoViewModel.checkFollow.value == true){
                    // 팔로우를 하고 있다면 팔로우 취소
                    authorInfoViewModel.cancelFollowing(userIdx, authorIdx)
                }else{
                    // 팔로우를 안하고 있다면 팔로우
                    authorInfoViewModel.followAuthor(userIdx, authorIdx)
                }
                authorInfoViewModel.checkFollow(userIdx, authorIdx)
                authorInfoViewModel.getFollowCount(authorIdx)
            }
        }
    }

    // 리뷰 버튼 클릭
    private fun settingButtonReview(){
        // 리뷰 버튼 클릭 시 리뷰 프래그먼트 보이기
        fragmentAuthorInfoBinding.buttonAuthorReview.setOnClickListener {
            val authorReviewBottomSheetFragment = AuthorReviewBottomSheetFragment()
            authorReviewBottomSheetFragment.arguments = Bundle().apply {
                putInt("userIdx", userIdx)
                putInt("authorIdx", authorIdx)
            }
            authorReviewBottomSheetFragment.show(parentFragmentManager, "BottomSheet")
        }
    }

    // 리사이클러 뷰 셋팅
    private fun settingRecyclerView(){
        // 테스트 데이터
        val piecesList = arrayListOf<Int>(
            R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_foreground,
        )
        
        // 해당 작가의 작품 리스트 받아오기

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