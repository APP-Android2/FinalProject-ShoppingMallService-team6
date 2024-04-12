package kr.co.lion.unipiece.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentJoinBinding
import kr.co.lion.unipiece.util.LoginFragmentName

class JoinFragment : Fragment() {

    lateinit var fragmentJoinBinding: FragmentJoinBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentJoinBinding = FragmentJoinBinding.inflate(layoutInflater)
        settingToolBar()
        click()
        return fragmentJoinBinding.root
    }

    //툴바 설정
    private fun settingToolBar(){
        fragmentJoinBinding.apply {
            toolBarJoin.apply {
                title = "회원가입"
                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    parentFragmentManager.popBackStack(LoginFragmentName.JOIN_FRAGMENT.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)

                }
            }
        }
    }

    //회원가입 버튼 클릭
    private fun click(){
        fragmentJoinBinding.apply {
            buttonJoinMember.setOnClickListener {
                parentFragmentManager.popBackStack(LoginFragmentName.JOIN_FRAGMENT.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)

            }
        }
    }

    //정보 저장
    private fun insertData(){
        fragmentJoinBinding.apply {
            val name
        }
    }
}