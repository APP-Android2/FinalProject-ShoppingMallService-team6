package kr.co.lion.unipiece.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentJoinBinding
import kr.co.lion.unipiece.db.remote.UserInfoDataSource
import kr.co.lion.unipiece.repository.UserInfoRepository
import kr.co.lion.unipiece.util.LoginFragmentName

class JoinFragment : Fragment() {

    lateinit var fragmentJoinBinding: FragmentJoinBinding

    val viewModel: LoginViewModel by viewModels()

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
                insertUserData()

            }
        }
    }

    private fun insertUserData() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            fragmentJoinBinding.apply {


                val userName = textJoinName.text.toString()
                val nickName = textJoinNickname.text.toString()
                val phoneNumber = textJoinNumber.text.toString()
                val userId = textJoinUserId.text.toString()
                val userPwd = textJoinUserPw.text.toString()

                // insertUserData()를 호출하고 완료될 때까지 기다림
                viewModel.insertUserData(userName, nickName, phoneNumber, userId, userPwd, false){ succes ->
                    if (succes){
                        // 작업이 완료된 후에 popBackStack() 호출
                        parentFragmentManager.popBackStack(LoginFragmentName.JOIN_FRAGMENT.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    }
                }
            }
        }
    }

}