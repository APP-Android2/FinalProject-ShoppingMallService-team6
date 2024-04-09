package kr.co.lion.unipiece.ui.author

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentRegisterAuthorBinding
import kr.co.lion.unipiece.util.AddAuthorFragmentName

class RegisterAuthorFragment : Fragment() {

    lateinit var fragmentRegisterAuthorBinding: FragmentRegisterAuthorBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentRegisterAuthorBinding = FragmentRegisterAuthorBinding.inflate(layoutInflater)
        initView()
        settingEvent()
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
                startActivity(Intent(requireActivity(), UpdateAuthorActivity::class.java))
            }
        }
    }
}