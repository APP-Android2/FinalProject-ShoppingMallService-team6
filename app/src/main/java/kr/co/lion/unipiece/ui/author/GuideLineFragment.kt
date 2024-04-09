package kr.co.lion.unipiece.ui.author

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentGuideLineBinding
import kr.co.lion.unipiece.util.AddAuthorFragmentName

class GuideLineFragment : Fragment() {

    lateinit var fragmentGuideLineBinding: FragmentGuideLineBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentGuideLineBinding = FragmentGuideLineBinding.inflate(layoutInflater)
        initView()
        settingEvent()
        return fragmentGuideLineBinding.root
    }


    private fun initView(){
        fragmentGuideLineBinding.apply {
            toolBarGuideLine.apply {
                title = "작가 등록 안내사항"
                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    parentFragmentManager.popBackStack(AddAuthorFragmentName.GUIDE_LINE_FRAGMENT.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    requireActivity().finish()
                }
            }

            textGuideLine1.text = "1, 이거이거이거 확인하시구요\n2, 이것도 확인 하셔야해요\n3, 이건 보셨나요??"
        }
    }


    //이벤트 설정
    private fun settingEvent(){
        fragmentGuideLineBinding.apply {
            buttonGuideOK.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.containerAuthor, RegisterAuthorFragment())
                    .addToBackStack(AddAuthorFragmentName.REGISTER_AUTHOR_FRAGMENT.str)
                    .commit()
            }
        }
    }
}





