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
import kr.co.lion.unipiece.ui.author.AuthorInfoActivity
import kr.co.lion.unipiece.ui.login.LoginActivity
import kr.co.lion.unipiece.ui.payment.cart.CartActivity
import kr.co.lion.unipiece.util.setMenuIconColor


class MyPageFragment : Fragment() {

    lateinit var fragmentMyPageBinding: FragmentMyPageBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentMyPageBinding = FragmentMyPageBinding.inflate(layoutInflater)

        setToolbar()
        clickButtonUserInfo()
        clickButtonVisitGallery()
        clickButtonFollowAuthor()
        clickButtonAuthorInfo()
        clickButtonLogout()

        return fragmentMyPageBinding.root
    }

    // 툴바 셋팅
    fun setToolbar(){
        fragmentMyPageBinding.apply {
            materialToolbar.apply {

                // 장바구니가 있는 메뉴 셋팅
                inflateMenu(R.menu.menu_cart)
                requireContext().setMenuIconColor(menu, R.id.menu_cart, R.color.second)
                // 장바구니 메뉴 클릭 시
                setOnMenuItemClickListener {
                    when(it.itemId) {
                        R.id.menu_cart -> {
                            val cartIntent = Intent(requireActivity(), CartActivity::class.java)
                            startActivity(cartIntent)
                        }

                    }
                    true
                }
            }
        }
    }

    // 회원 정보 보기
    fun clickButtonUserInfo(){
        with(fragmentMyPageBinding){
            // 회원 정보 보기 버튼 클릭 시
            textButtonMyPageInfo.setOnClickListener{
                val userInfoIntent = Intent(requireActivity(), UserInfoActivity::class.java)
                startActivity(userInfoIntent)
            }
        }
    }

    // 전시실 방문 신청 목록
    fun clickButtonVisitGallery(){
        with(fragmentMyPageBinding){
            // 전시실 방문 신청 목록 버튼 클릭 시
            textButtonMyPageVisitList.setOnClickListener{
                val visitGalleryIntent = Intent(requireActivity(), VisitGalleryActivity::class.java)
                startActivity(visitGalleryIntent)
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
                val authorInfoIntent = Intent(requireActivity(), AuthorInfoActivity::class.java)
                startActivity(authorInfoIntent)
            }
        }
    }

    // 로그아웃
    fun clickButtonLogout(){
        with(fragmentMyPageBinding){
            // 로그아웃 버튼 클릭 시
            textButtonMyPageLogout.setOnClickListener {
                val loginIntent = Intent(requireActivity(), LoginActivity::class.java)
                startActivity(loginIntent)
                requireActivity().finish()
            }
        }
    }
}