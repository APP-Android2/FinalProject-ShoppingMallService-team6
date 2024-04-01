package kr.co.lion.unipiece.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentJoinBinding
import kr.co.lion.unipiece.util.LoginFragmentName

class JoinFragment : Fragment() {

    lateinit var fragmentJoinBinding: FragmentJoinBinding

    lateinit var loginActivity: LoginActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentJoinBinding = FragmentJoinBinding.inflate(layoutInflater)
        loginActivity = activity as LoginActivity
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
                    loginActivity.removeFragment(LoginFragmentName.JOIN_FRAGMENT)
                }
            }
        }
    }

    //회원가입 버튼 클릭
    private fun click(){
        fragmentJoinBinding.apply {
            buttonJoinMember.setOnClickListener {
                loginActivity.removeFragment(LoginFragmentName.JOIN_FRAGMENT)
            }
        }
    }
}