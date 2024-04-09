package kr.co.lion.unipiece.ui.mypage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentApplyVisitGalleryBinding
import kr.co.lion.unipiece.util.UserInfoFragmentName
import kr.co.lion.unipiece.util.VisitGalleryFragmentName

class ApplyVisitGalleryFragment : Fragment() {

    lateinit var fragmentApplyVisitGalleryBinding: FragmentApplyVisitGalleryBinding

    // 신청 수정 여부
    private var isModify = false

        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentApplyVisitGalleryBinding = FragmentApplyVisitGalleryBinding.inflate(inflater)

        settingToolbar()
        settingButtonMember()
        settingDatePicker()
        settingButtonApplyConfirm()

        return fragmentApplyVisitGalleryBinding.root
    }

    // 툴바 셋팅
    private fun settingToolbar(){
        isModify = requireArguments().getBoolean("isModify", false)

        fragmentApplyVisitGalleryBinding.apply {
            toolbarApplyVisitGallery.apply {

                title = if(isModify){
                    "전시실 방문 신청 수정"
                }else{
                    "전시실 방문 신청"
                }

                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    removeFragment()
                }
            }
        }
    }

    // 방문 날짜
    private fun settingDatePicker(){
        fragmentApplyVisitGalleryBinding.apply {
            datePickerApplyVisitGallery.apply {
                // 오늘 날짜 부터 선택 가능
                minDate = System.currentTimeMillis()
            }
        }
    }


    private fun settingButtonMember(){
        fragmentApplyVisitGalleryBinding.apply {
            // 감소 버튼
            buttonMemberCountDown.setOnClickListener {

            }
            // 증가 버튼
            buttonMemberCountUp.setOnClickListener {

            }
        }
    }

    // 신청 완료 버튼
    private fun settingButtonApplyConfirm(){
        fragmentApplyVisitGalleryBinding.buttonApplyVisitGalleryConfirm.apply {
            setOnClickListener {
                // 추후 수정 필요
                removeFragment()
            }
        }
    }

    // 선택한 날짜 값을 반환하는 메서드
    private fun gettingDateValue():String{
        val datePicker = fragmentApplyVisitGalleryBinding.datePickerApplyVisitGallery
        val year = datePicker.year
        val month = datePicker.month
        val day = datePicker.dayOfMonth
        // 추후 수정 필요
        val result = "${year}년 ${month + 1}월 ${day}일"
        Log.d("test1234", result)
        return result
    }

    // 프래그먼트 제거 메서드
    private fun removeFragment(){
        parentFragmentManager.popBackStack(VisitGalleryFragmentName.APPLY_VISIT_GALLERY_FRAGMENT.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

}