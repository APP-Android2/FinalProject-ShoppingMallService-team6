package kr.co.lion.unipiece.ui.mypage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentModifyUserInfoBinding
import kr.co.lion.unipiece.ui.login.LoginActivity
import kr.co.lion.unipiece.util.UserInfoFragmentName

class ModifyUserInfoFragment : Fragment() {

    lateinit var fragmentModifyUserInfoBinding: FragmentModifyUserInfoBinding
    lateinit var userInfoActivity: UserInfoActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        fragmentModifyUserInfoBinding = FragmentModifyUserInfoBinding.inflate(inflater)
        userInfoActivity = activity as UserInfoActivity

        settingToolbar()
        settingButtonModifyUserInfoConfirm()
        settingButtonDeleteUser()

        return fragmentModifyUserInfoBinding.root
    }

    // 툴바 셋팅
    private fun settingToolbar(){
        fragmentModifyUserInfoBinding.apply {
            toolbarModifyUserInfo.apply {
                title = "회원 정보 수정"

                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    userInfoActivity.removeFragment(UserInfoFragmentName.MODIFY_USER_INFO_FRAGMENT)
                }
            }
        }
    }

    // 수정 완료 버튼
    private fun settingButtonModifyUserInfoConfirm(){
        fragmentModifyUserInfoBinding.buttonModifyUserInfoConfirm.apply {
            
            // 추후 제거
            setOnClickListener {
                userInfoActivity.removeFragment(UserInfoFragmentName.MODIFY_USER_INFO_FRAGMENT)
            }
            
            // 입력 확인
            
            // 데이터 수정 처리
            
            // 회원 정보 프래그먼트로 이동

        }

    }

    // 회원 탈퇴 버튼
    private fun settingButtonDeleteUser(){
        fragmentModifyUserInfoBinding.buttonDeleteUser.apply {
            setOnClickListener {
                // 회원 탈퇴 확인 다이얼로그

                // 회원 탈퇴 처리

                // 로그인 화면으로 이동
                requireActivity().finish()
                val loginIntent = Intent(requireActivity(), LoginActivity::class.java)
                startActivity(loginIntent)
            }

        }

    }

}