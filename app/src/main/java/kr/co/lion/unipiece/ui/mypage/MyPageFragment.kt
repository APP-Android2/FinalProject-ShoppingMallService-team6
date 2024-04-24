package kr.co.lion.unipiece.ui.mypage


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.FragmentMyPageBinding
import kr.co.lion.unipiece.ui.author.AuthorInfoActivity
import kr.co.lion.unipiece.ui.login.LoginActivity
import kr.co.lion.unipiece.ui.mypage.viewmodel.MyPageViewModel
import kr.co.lion.unipiece.ui.payment.CartActivity
import kr.co.lion.unipiece.util.setMenuIconColor


class MyPageFragment : Fragment() {

    lateinit var binding: FragmentMyPageBinding
    val myPageViewModel:MyPageViewModel by viewModels()

    val userIdx by lazy {
        UniPieceApplication.prefs.getUserIdx("userIdx", -1)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyPageBinding.inflate(inflater,container,false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchData()
        initView()
    }

    fun fetchData(){
        lifecycleScope.launch {
            // 닉네임 설정
            binding.textViewMyPageNicName.text = myPageViewModel.getUserNickname(userIdx)
            // 작가 여부 확인
            val isAuthor = myPageViewModel.isAuthor(userIdx)
            // 작가인 경우에만 작가 정보 버튼 보여주기
            if(isAuthor){
                binding.textButtonMyPageAuthInfo.visibility = View.VISIBLE
                binding.textViewMyPageAuth.visibility = View.VISIBLE
                binding.MaterialDividerAuthInfo.visibility = View.VISIBLE
            }
            // 작가 정보를 불러오는데 시간차가 있어서 프로그래스바 사용
            binding.progressBarMyPage.visibility = View.GONE
        }
    }

    fun initView() {

        // 바인딩
        with(binding) {

            // 로그아웃버튼 이슈 관련 화면구성 코드 ///////////////////////////////////////////////////////////////////
            with(root){
                with(viewTreeObserver){
                    addOnGlobalLayoutListener(object :
                        ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            val fragmentLayout = binding.root
                            val screenHeight = resources.displayMetrics.heightPixels

                            // 프래그먼트의 높이 계산
                            val layoutParams = fragmentLayout.layoutParams
                            val density = resources.displayMetrics.density
                            val additionalHeightInPixel = (80 * density).toInt()
                            // 나머지 화면 높이 계산
                            layoutParams.height = screenHeight - additionalHeightInPixel
                            fragmentLayout.layoutParams = layoutParams

                            // OnGlobalLayoutListener 제거
                            binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        }
                    })
                }
            }
            // 툴바 셋팅 //////////////////////////////////////////////////////////////////////////
            with(materialToolbar){

                // 장바구니가 있는 메뉴 셋팅
                inflateMenu(R.menu.menu_cart)
                requireContext().setMenuIconColor(menu, R.id.menu_cart, R.color.second)
                // 장바구니 메뉴 클릭 시
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menu_cart -> {
                            val cartIntent = Intent(requireActivity(), CartActivity::class.java)
                            startActivity(cartIntent)
                        }

                    }
                    true
                }
            }

            // 회원 정보 보기 ////////////////////////////////////////////////////////////////////
            with(textButtonUserInfo){
                // 버튼 클릭 시
                setOnClickListener {
                    val userInfoIntent = Intent(requireActivity(), UserInfoActivity::class.java)
                    startActivity(userInfoIntent)
                }
            }

            // 전시실 방문 신청 목록 //////////////////////////////////////////////////////////////////////
            with(textButtonMyPageVisitList){
                // 버튼 클릭 시
                setOnClickListener {
                    val visitGalleryIntent = Intent(requireActivity(), VisitGalleryActivity::class.java)
                    startActivity(visitGalleryIntent)
                }
            }

            // 팔로잉한 작가 /////////////////////////////////////////////////////////////////
            with(textButtonMyPageFollowAuth){
                // 버튼 클릭 시
                setOnClickListener {
                    val followIntent = Intent(requireActivity(), FollowActivity::class.java)
                    startActivity(followIntent)
                }
            }

            // 작가 정보 보기 ////////////////////////////////////////////////////////////////////////
            with(textButtonMyPageAuthInfo){
                // 버튼 클릭 시
                setOnClickListener {
                    val authorInfoIntent = Intent(requireActivity(), AuthorInfoActivity::class.java)
                    startActivity(authorInfoIntent)
                }
            }

            // 로그아웃 ///////////////////////////////////////////////////////////////////
            with(textButtonMyPageLogout) {
                // 버튼 클릭 시
                setOnClickListener {
                    val loginIntent = Intent(requireActivity(), LoginActivity::class.java)
                    UniPieceApplication.prefs.deleteUserIdx("userIdx")
                    UniPieceApplication.prefs.deleteUserId("userId")
                    startActivity(loginIntent)
                    requireActivity().finish()
                }
            }
        }
    }
}