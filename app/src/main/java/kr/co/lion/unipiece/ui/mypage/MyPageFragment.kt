package kr.co.lion.unipiece.ui.mypage


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentMyPageBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.payment.cart.CartActivity
import kr.co.lion.unipiece.util.MainFragmentName


class MyPageFragment : Fragment() {

    lateinit var fragmentMyPageBinding: FragmentMyPageBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentMyPageBinding = FragmentMyPageBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        setToolbar()
        clickButtonFollowAuthor()

        return fragmentMyPageBinding.root
    }

    // 툴바 셋팅
    fun setToolbar(){
        fragmentMyPageBinding.apply {
            materialToolbar.apply {

                // 장바구니가 있는 메뉴 셋팅
                inflateMenu(R.menu.menu_cart)
                // 장바구니 메뉴 클릭 시
                setOnMenuItemClickListener {
                    when(it.itemId) {
                        R.menu.menu_cart -> {
                            val cartIntent = Intent(requireActivity(), CartActivity::class.java)
                            startActivity(cartIntent)
                            requireActivity().finish()
                        }

                    }
                    true
                }
                // 뒤로가기 메뉴 셋팅
                setNavigationIcon(R.drawable.back_icon)
                // 뒤로가기 내비 아이콘 클릭 시
                setNavigationOnClickListener {
                    mainActivity.replaceFragment(MainFragmentName.HOME_FRAGMENT,false)
                }
            }
        }
    }

    // 회원 정보 보기
    fun clickButtonUserInfo(){
        with(fragmentMyPageBinding){
            // 회원 정보 보기 버튼 클릭 시
            textButtonMyPageInfo.setOnClickListener{

            }
        }
    }

    // 전시실 방문 신청 목록
    fun clickButtonVisitGallery(){
        with(fragmentMyPageBinding){
            // 전시실 방문 신청 목록 버튼 클릭 시
            textButtonMyPageVisitList.setOnClickListener{

            }
        }
    }

    // 팔로잉한 작가
    fun clickButtonFollowAuthor(){
        with(fragmentMyPageBinding){
            // 팔로잉한 작가 버튼 클릭 시
            textButtonMyPageFollowAuth.setOnClickListener {
                val followIntent = Intent(requireActivity(), FollowActivity::class.java)
                startActivity(followIntent)
            }
        }
    }

    // 작가 정보 보기
    fun clickButtonAuthorInfo(){
        with(fragmentMyPageBinding){
            // 작가 정보 보기 버튼 클릭 시
            textButtonMyPageAuthInfo.setOnClickListener {

            }
        }
    }

    // 로그아웃
    fun clickButtonLogout(){
        with(fragmentMyPageBinding){
            // 로그아웃 버튼 클릭 시
            textButtonMyPageLogout.setOnClickListener {

            }
        }
    }
}