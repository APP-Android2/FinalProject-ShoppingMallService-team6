package kr.co.lion.unipiece.ui.author

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.FragmentAuthorInfoBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.author.adapter.AuthorPiecesAdapter
import kr.co.lion.unipiece.ui.author.viewmodel.AuthorInfoViewModel
import kr.co.lion.unipiece.ui.buy.BuyDetailActivity
import kr.co.lion.unipiece.util.AuthorInfoFragmentName
import kr.co.lion.unipiece.util.setImage
import kr.co.lion.unipiece.util.setMenuIconColor

class AuthorInfoFragment : Fragment() {

    lateinit var fragmentAuthorInfoBinding: FragmentAuthorInfoBinding
    val authorInfoViewModel: AuthorInfoViewModel by viewModels()

    val authorIdx by lazy {
        requireArguments().getInt("authorIdx")
    }
    val userIdx by lazy {
        UniPieceApplication.prefs.getUserIdx("userIdx",0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentAuthorInfoBinding = FragmentAuthorInfoBinding.inflate(inflater)
        fragmentAuthorInfoBinding.authorInfoViewModel = authorInfoViewModel
        fragmentAuthorInfoBinding.lifecycleOwner = this

        settingToolbar()

        lifecycleScope.launch(Dispatchers.Main){
            fetchData()
            initView()
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
            // 작가 정보 셋팅
            authorInfoViewModel?.authorInfoData?.observe(viewLifecycleOwner) {
                lifecycleScope.launch {
                    val authorName = it.authorName + " 작가"
                    textViewAuthorName.text = authorName
                    textViewAuthorBasicInfo.text = it.authorBasic
                    textViewAuthorDetailInfo.text = it.authorInfo

                    authorInfoViewModel?.checkAuthor(userIdx)

                    // 작가 이미지 셋팅
                    val authorImg = it.authorImg
                    val imageUrl = authorInfoViewModel?.getAuthorInfoImg(authorImg)
                    requireActivity().setImage(imageViewAuthor, imageUrl)

                    progressBarAuthorInfo.visibility = View.GONE
                    scrollViewAuthorInfo.visibility = View.VISIBLE
                }

            }

            authorInfoViewModel?.authorIsMe?.observe(viewLifecycleOwner){
                // 회원 유형에 따라 팔로우, 리뷰 버튼 표시
                if(it){
                    // 사용자가 해당 작가인 경우 리뷰 버튼만 보여주기
                    buttonAuthorReview.visibility = View.VISIBLE
                    // 툴바 수정 버튼 보여주기
                    toolbarAuthorInfo.menu.findItem(R.id.menu_edit).isVisible = true
                }else{
                    // 사용자가 해당 작가가 아닌 경우 리뷰, 팔로우 버튼 모두 보여주기
                    buttonAuthorReview.visibility = View.VISIBLE
                    buttonAuthorFollow.visibility = View.VISIBLE
                }
            }

            // 팔로우 수 셋팅
            authorInfoViewModel?.authorFollow?.observe(viewLifecycleOwner){
                textViewAuthorFollower.text = it
            }
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
                putInt("authorIdx", authorIdx)
                // 해당 작가가 본인인 경우 본인 여부 값 전달
                authorInfoViewModel.authorIsMe.value?.let { it1 -> putBoolean("authorIsMe", it1) }
            }
            authorReviewBottomSheetFragment.show(parentFragmentManager, "BottomSheet")
        }
    }

    // 리사이클러 뷰 셋팅
    private fun settingRecyclerView(){

        lifecycleScope.launch {
            // 해당 작가의 작품 리스트 받아오기
            authorInfoViewModel.getAuthorPieces(authorIdx)

            authorInfoViewModel.authorPieces.observe(viewLifecycleOwner) { piecesList ->
                // 리사이클러뷰 어댑터
                val authorPiecesAdapter = AuthorPiecesAdapter(piecesList) { pieceIdx ->
                    val pieceIntent = Intent(requireActivity(), BuyDetailActivity::class.java)
                    pieceIntent.putExtra("pieceIdx", pieceIdx)
                    pieceIntent.putExtra("authorIdx", authorIdx)
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