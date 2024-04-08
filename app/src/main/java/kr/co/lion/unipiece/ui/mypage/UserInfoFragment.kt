package kr.co.lion.unipiece.ui.mypage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentUserInfoBinding
import kr.co.lion.unipiece.ui.payment.delivery.DeliveryActivity
import kr.co.lion.unipiece.util.UserInfoFragmentName
import kr.co.lion.unipiece.util.setMenuIconColor

class UserInfoFragment : Fragment() {

    lateinit var fragmentUserInfoBinding: FragmentUserInfoBinding
    lateinit var userInfoActivity: UserInfoActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentUserInfoBinding = FragmentUserInfoBinding.inflate(inflater)
        userInfoActivity = activity as UserInfoActivity

        settingToolbar()
        settingButtonModifyUserInfo()
        settingButtonManageAddress()

        return fragmentUserInfoBinding.root
    }

    // 툴바 셋팅
    private fun settingToolbar(){
        fragmentUserInfoBinding.apply {
            toolbarUserInfo.apply {
                title = "회원 정보"

                inflateMenu(R.menu.menu_home)
                requireContext().setMenuIconColor(menu, R.id.menu_home, R.color.second)

                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    userInfoActivity.finish()
                }
            }
        }
    }

    // 회원 정보 수정 버튼
    private fun settingButtonModifyUserInfo(){

        fragmentUserInfoBinding.buttonModifyUserInfo.apply {
            setOnClickListener {
                // 추후 전달할 데이터는 여기에 담기
                val modifyUserInfoBundle = Bundle()
                // 회원 정보 수정 프래그먼트 교체
                userInfoActivity.replaceFragment(UserInfoFragmentName.MODIFY_USER_INFO_FRAGMENT,true, modifyUserInfoBundle)
            }
        }

    }

    // 배송지 관리 버튼
    private fun settingButtonManageAddress(){

        fragmentUserInfoBinding.buttonManageAddress.apply {
            setOnClickListener {
                // 추후 전달할 데이터는 여기에 담기
                val deliveryIntent = Intent(requireActivity(), DeliveryActivity::class.java)
                // 배송지 관리 액티비티 실행
                startActivity(deliveryIntent)
            }
        }
    }

}