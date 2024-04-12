package kr.co.lion.unipiece.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentJoinBinding
import kr.co.lion.unipiece.db.remote.UserInfoDataSource
import kr.co.lion.unipiece.model.UserInfoData
import kr.co.lion.unipiece.repository.UserInfoRepository
import kr.co.lion.unipiece.util.CustomDialog
import kr.co.lion.unipiece.util.LoginFragmentName
import kr.co.lion.unipiece.util.hideSoftInput
import kr.co.lion.unipiece.util.showSoftInput

class JoinFragment : Fragment() {

    lateinit var fragmentJoinBinding: FragmentJoinBinding

    val viewModel: LoginViewModel by viewModels()

    //아이디 중복 검사 여부
    var checkUserId = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentJoinBinding = FragmentJoinBinding.inflate(layoutInflater)
        settingToolBar()
        click()
        initView()
        checkId()
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
                val chk = checkEmptyText()
                if (chk == true){
                    noCheckUserId()

                }
            }
        }
    }

    //화면 설정
    private fun initView(){
        fragmentJoinBinding.apply {
            //포커스
            requireActivity().showSoftInput(textJoinName)

            //에러 해결
            textJoinName.addTextChangedListener {
                textJoinNameLayout.error = null
            }
            textJoinNickname.addTextChangedListener {
                textJoinNicknameLayout.error = null
            }
            textJoinNumber.addTextChangedListener {
                textJoinNumberLayout.error = null
            }
            textJoinUserId.addTextChangedListener {
                textJoinUserIdLayout.error = null
            }
            textJoinUserPw.addTextChangedListener {
                textJoinPwLayout.error = null
            }
            textJoinCheckPw.addTextChangedListener {
                textJoinCheckPwLayout.error = null
            }
            textJoinUserId.addTextChangedListener {
                checkUserId = false
            }
        }
    }

    //입력 검사
    private fun checkEmptyText():Boolean{
        fragmentJoinBinding.apply {
            var errorText:View? = null

            val userName = textJoinName.text?.toString()?:""
            val nickName = textJoinNickname.text?.toString()?:""
            val phoneNumber = textJoinNumber.text?.toString()?:""
            val userId = textJoinUserId.text?.toString()?:""
            val userPwd = textJoinUserPw.text?.toString()?:""
            val checkPwd = textJoinCheckPw.text?.toString()?:""

            //이름
            if (userName.trim().isEmpty()){
                textJoinNameLayout.error = "이름을 입력해주세요"
                if (errorText == null){
                    errorText = textJoinName
                }else{
                    textJoinNameLayout.error = null
                }
            }
            //닉네임
            if (nickName.trim().isEmpty()){
                textJoinNicknameLayout.error = "닉네임을 입력해주세요"
                if (errorText == null){
                    errorText = textJoinNickname
                }else{
                    textJoinNicknameLayout.error = null
                }
            }
            //전화번호
            if (phoneNumber.trim().isEmpty()){
                textJoinNumberLayout.error = "휴대폰 번호를 입력해주세요"
                if (errorText == null){
                    errorText = textJoinNumber
                }else{
                    textJoinNumberLayout.error = null
                }
            }
            //아이디
            if (userId.trim().isEmpty()){
                textJoinUserIdLayout.error = "아이디를 입력해주세요"
                if(errorText == null){
                    errorText = textJoinUserId
                }else{
                    textJoinUserIdLayout.error = null
                }
            }
            //비밀번호
            if (userPwd.trim().isEmpty()){
                textJoinPwLayout.error = "비밀번호를 입력해주세요"
                if (errorText == null){
                    errorText = textJoinUserPw
                }else{
                    textJoinPwLayout.error = null
                }
            }
            //비밀번호 체크
            if (checkPwd != userPwd){
                textJoinCheckPwLayout.error = "비밀번호가 일치하지 않습니다"
                if (errorText == null){
                    errorText = textJoinCheckPw
                }else{
                    textJoinCheckPwLayout.error = null
                }
            }
            if (errorText != null){
                requireActivity().showSoftInput(errorText)
                return false
            }else{
                return true
            }
        }
    }

    //아이디 중복 검사를 하지 않은 경우
    private fun noCheckUserId(){
        fragmentJoinBinding.apply {
            if (checkUserId == false){
                val dialog = CustomDialog("아이디 중복 확인", "아이디 중복 확인을 해주세요")
                dialog.setButtonClickListener(object : CustomDialog.OnButtonClickListener{
                    override fun okButtonClick() {
                        requireActivity().showSoftInput(textJoinUserId)

                    }

                    override fun noButtonClick() {

                    }

                })
                dialog.show(parentFragmentManager, "CustomDialog")

                return
            }else{
                insertUserData()
                requireActivity().hideSoftInput()
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
                viewModel.insertUserData(userName, nickName, phoneNumber, userId, userPwd, true){ success ->
                    if (success){
                        // 작업이 완료된 후에 popBackStack() 호출
                        parentFragmentManager.popBackStack(LoginFragmentName.JOIN_FRAGMENT.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    }
                }
            }
        }
    }

    //아이디 중복 검사
    private fun checkId(){
        fragmentJoinBinding.apply {
            buttonJoinCheckUserId.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main){
                    val userInfoDataSource = UserInfoDataSource()

                    val userId = textJoinUserId.text.toString()

                    checkUserId = userInfoDataSource.checkUserId(userId)

                    if (checkUserId == false) {
                        textJoinUserId.setText("")
                        val dialog = CustomDialog("아이디 중복 오류", "이미 사용중인 아이디 입니다")
                        dialog.setButtonClickListener(object : CustomDialog.OnButtonClickListener {
                            override fun okButtonClick() {
                                requireActivity().showSoftInput(textJoinUserId)
                            }

                            override fun noButtonClick() {

                            }

                        })
                        dialog.show(parentFragmentManager, "CustomDialog")
                    }
                    if (userId.isEmpty()){
                        val dialog = CustomDialog("아이디 입력 오류", "아이디를 입력해주세요")
                        dialog.setButtonClickListener(object :CustomDialog.OnButtonClickListener{
                            override fun okButtonClick() {
                                requireActivity().showSoftInput(textJoinUserId)
                            }

                            override fun noButtonClick() {

                            }

                        })
                        dialog.show(parentFragmentManager, "CustomDialog")
                    }else if (checkUserId == true){
                        val dialog = CustomDialog("사용 가능한 아이디 입니다", "회원가입을 진행해주세요!")
                        dialog.setButtonClickListener(object :CustomDialog.OnButtonClickListener{
                            override fun okButtonClick() {
                                requireActivity().hideSoftInput()
                            }

                            override fun noButtonClick() {

                            }

                        })
                        dialog.show(parentFragmentManager, "CustomDialog")
                    }
                    }
                }
            }
        }
    }
