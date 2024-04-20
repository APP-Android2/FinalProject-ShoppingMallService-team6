package kr.co.lion.unipiece.ui.mypage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.FragmentModifyUserInfoBinding
import kr.co.lion.unipiece.ui.login.LoginActivity
import kr.co.lion.unipiece.ui.mypage.viewmodel.ModifyUserInfoViewModel
import kr.co.lion.unipiece.util.CustomDialog
import kr.co.lion.unipiece.util.UserInfoFragmentName

class ModifyUserInfoFragment : Fragment() {

    lateinit var fragmentModifyUserInfoBinding: FragmentModifyUserInfoBinding

    val modifyUserInfoViewModel:ModifyUserInfoViewModel by viewModels()

    val userIdx by lazy {
        UniPieceApplication.prefs.getUserIdx("userIdx", -1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        fragmentModifyUserInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_modify_user_info, container, false)
        fragmentModifyUserInfoBinding.modifyUserInfoViewModel = modifyUserInfoViewModel
        fragmentModifyUserInfoBinding.lifecycleOwner = this

        fetchData()
        setObserver()
        settingToolbar()
        settingButtonModifyUserInfoConfirm()
        settingButtonDeleteUser()

        return fragmentModifyUserInfoBinding.root
    }

    private fun fetchData(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                modifyUserInfoViewModel.getUserDataByIdx(userIdx)
            }
        }
    }

    private fun setObserver(){
        modifyUserInfoViewModel.checkComplete.observe(viewLifecycleOwner){
            if(it){
                Snackbar.make(requireView(),"회원 정보 수정 완료", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(ContextCompat.getColor(requireActivity(), R.color.first))
                    .setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                    .show()
                removeFragment()
            }else{
                Snackbar.make(requireView(),"네트워크 오류, 잠시 후 다시 시도해주세요", Snackbar.LENGTH_SHORT)
                    .setAnchorView(fragmentModifyUserInfoBinding.toolbarModifyUserInfo)
                    .setBackgroundTint(ContextCompat.getColor(requireActivity(), R.color.first))
                    .setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                    .show()
            }
        }

        modifyUserInfoViewModel.deleteComplete.observe(viewLifecycleOwner){
            if(it){
                // 로그인 화면으로 이동
                val loginIntent = Intent(requireActivity(), LoginActivity::class.java)
                UniPieceApplication.prefs.deleteUserIdx("userIdx")
                UniPieceApplication.prefs.deleteUserId("userId")
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(loginIntent)
            }else{
                Snackbar.make(requireView(),"네트워크 오류, 잠시 후 다시 시도해주세요", Snackbar.LENGTH_SHORT)
                    .setAnchorView(fragmentModifyUserInfoBinding.toolbarModifyUserInfo)
                    .setBackgroundTint(ContextCompat.getColor(requireActivity(), R.color.first))
                    .setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                    .show()
            }
        }
    }

    // 툴바 셋팅
    private fun settingToolbar(){
        fragmentModifyUserInfoBinding.apply {
            toolbarModifyUserInfo.apply {
                title = "회원 정보 수정"

                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    removeFragment()
                }
            }
        }
    }

    // 수정 완료 버튼
    private fun settingButtonModifyUserInfoConfirm(){
        fragmentModifyUserInfoBinding.buttonModifyUserInfoConfirm.setOnClickListener {
            // 빈칸 확인
            if(isInputEmpty()) return@setOnClickListener
            // 비밀번호 확인 확인
            if(checkPwd()) return@setOnClickListener
            // 회원 정보 업데이트
            lifecycleScope.launch {
                modifyUserInfoViewModel.updateUserInfo()
            }
        }
    }
    
    private fun isInputEmpty():Boolean{
        resetError()
        var result = false
        with(fragmentModifyUserInfoBinding){
            if(textInputModifyUserName.text.isNullOrBlank()){
                layoutUserName.error = "이름을 입력해주세요"
                result = true
            }
            if(textInputModifyUserNickName.text.isNullOrBlank()){
                layoutUserNickName.error = "닉네임을 입력해주세요"
                result = true
            }
            if(textInputModifyUserPhoneNumber.text.isNullOrBlank()){
                layoutUserPhoneNumber.error = "휴대폰 번호를 입력해주세요"
                result = true
            }
            if(textInputModifyUserPw.text.isNullOrBlank()){
                layoutUserPw.error = "비밀번호를 입력해주세요"
                result = true
            }
            if(textInputModifyUserPw2.text.isNullOrBlank()) {
                layoutUserPw2.error = "비밀번호 확인을 입력해주세요"
                result = true
            }
        }
        return result
    }

    private fun resetError(){
        with(fragmentModifyUserInfoBinding){
            layoutUserName.error = ""
            layoutUserName.isErrorEnabled = false
            layoutUserNickName.error = ""
            layoutUserNickName.isErrorEnabled = false
            layoutUserPhoneNumber.error = ""
            layoutUserPhoneNumber.isErrorEnabled = false
            layoutUserPw.error = ""
            layoutUserPw.isErrorEnabled = false
            layoutUserPw2.error = ""
            layoutUserPw2.isErrorEnabled = false
        }
    }

    private fun checkPwd():Boolean{
        var result = false
        with(fragmentModifyUserInfoBinding){
            layoutUserPw2.error = ""
            layoutUserPw2.isErrorEnabled = false
            // 비밀번호 입력값과 비밀번호 확인 입력값이 다르면 true를 반환하여 수정 버튼의 클릭리스너를 중단시킨다
            if(textInputModifyUserPw.text.toString() != textInputModifyUserPw2.text.toString()){
                layoutUserPw2.error = "입력하신 비밀번호와 다릅니다"
                result = true
            }
        }
        return result
    }

    // 회원 탈퇴 버튼
    private fun settingButtonDeleteUser(){
        fragmentModifyUserInfoBinding.buttonDeleteUser.apply {
            setOnClickListener {
                // 회원 탈퇴 확인 다이얼로그
                val dialog = CustomDialog("회원 탈퇴", "회원 탈퇴 시 되돌릴 수 없습니다. \n탈퇴하시겠습니까?")

                dialog.setButtonClickListener(object: CustomDialog.OnButtonClickListener{
                    override fun okButtonClick() {
                        lifecycleScope.launch {
                            // 회원 탈퇴 처리
                            modifyUserInfoViewModel.deleteUser()
                        }
                    }

                    override fun noButtonClick() {
                    }

                })

                dialog.show(parentFragmentManager, "DeleteUser")

            }

        }

    }

    private fun removeFragment(){
        parentFragmentManager.popBackStack(UserInfoFragmentName.MODIFY_USER_INFO_FRAGMENT.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

}