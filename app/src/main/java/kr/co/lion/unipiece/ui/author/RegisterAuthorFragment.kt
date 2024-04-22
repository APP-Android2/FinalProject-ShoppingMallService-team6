package kr.co.lion.unipiece.ui.author

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentRegisterAuthorBinding
import kr.co.lion.unipiece.util.AddAuthorFragmentName
import kr.co.lion.unipiece.util.CustomDialog
import kr.co.lion.unipiece.util.showSoftInput

class RegisterAuthorFragment : Fragment() {

    lateinit var fragmentRegisterAuthorBinding: FragmentRegisterAuthorBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentRegisterAuthorBinding = FragmentRegisterAuthorBinding.inflate(layoutInflater)
        initView()
        settingEvent()
        settingView()
        return fragmentRegisterAuthorBinding.root
    }

    //툴바 설정
    private fun initView(){
        fragmentRegisterAuthorBinding.apply {
            toolBarRegister.apply {
                title = "작가 등록"
                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    parentFragmentManager.popBackStack(AddAuthorFragmentName.REGISTER_AUTHOR_FRAGMENT.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)

                }
            }
        }
    }

    //이벤트 설정
    private fun settingEvent(){
        fragmentRegisterAuthorBinding.apply {
            buttonAuthorRegister.setOnClickListener {
                val chk = checkEmptyList()
                if (chk){
                    checkImg()
                }
            }
        }
    }


    private fun settingView(){
        fragmentRegisterAuthorBinding.apply {
            //포커스주기
            requireActivity().showSoftInput(textRegisterUni)

            //에러 해결
            textRegisterUni.addTextChangedListener {
                textRegisterUniLayout.error = null
            }
            textRegisterName.addTextChangedListener {
                textRegisterNameLayout.error = null
            }
            textRegisterMajor.addTextChangedListener {
                textRegisterMajorLayout.error = null
            }
        }
    }




    private fun checkEmptyList():Boolean{
        fragmentRegisterAuthorBinding.apply {
            var emptyList:View? = null

            val uni = textRegisterUni.text.toString()
            val name = textRegisterName.text.toString()
            val major = textRegisterMajor.text.toString()

            if (uni.trim().isEmpty()){
                textRegisterUniLayout.error = "학교를 입력해주세요"
                if (emptyList == null){
                    emptyList = textRegisterUni
                }else{
                    textRegisterUniLayout.error = null
                }
            }

            if (name.trim().isEmpty()){
                textRegisterNameLayout.error = "이름을 입력해주세요"
                if (emptyList == null){
                    emptyList = textRegisterName
                }else{
                    textRegisterNameLayout.error = null
                }
            }

            if (major.trim().isEmpty()){
                textRegisterMajorLayout.error = "학과를 입력해주세요"
                if (emptyList == null){
                    emptyList = textRegisterMajor
                }else{
                    textRegisterMajorLayout.error = null
                }
            }
            if (emptyList != null){
                requireActivity().showSoftInput(emptyList)
                return false
            }else{
                return true
            }
        }
    }

    //이미지 체크
    private fun checkImg(){
        fragmentRegisterAuthorBinding.apply {
            val image = textAddFile.text.toString()
            if (image.trim().isEmpty()){
                val dialog = CustomDialog("첨부파일 오류", "첨부파일을 추가해주세요")
                dialog.setButtonClickListener(object : CustomDialog.OnButtonClickListener{
                    override fun okButtonClick() {

                    }

                    override fun noButtonClick() {

                    }

                })
                dialog.show(parentFragmentManager, "CustomDialog")
            }else{
                startActivity(Intent(requireActivity(), UpdateAuthorActivity::class.java))
            }
        }
    }
}