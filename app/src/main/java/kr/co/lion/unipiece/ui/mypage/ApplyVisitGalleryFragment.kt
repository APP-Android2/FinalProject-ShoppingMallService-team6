package kr.co.lion.unipiece.ui.mypage

import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivityVisitGalleryBinding
import kr.co.lion.unipiece.databinding.FragmentApplyVisitGalleryBinding
import kr.co.lion.unipiece.util.VisitGalleryFragmentName

class ApplyVisitGalleryFragment : Fragment() {

    lateinit var fragmentApplyVisitGalleryBinding: FragmentApplyVisitGalleryBinding
    lateinit var visitGalleryActivity: VisitGalleryActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentApplyVisitGalleryBinding = FragmentApplyVisitGalleryBinding.inflate(inflater)
        visitGalleryActivity = activity as VisitGalleryActivity

        settingToolbar()
        settingButtonMember()
        settingButtonApplyConfirm()

        return fragmentApplyVisitGalleryBinding.root
    }

    // 툴바 셋팅
    private fun settingToolbar(){
        fragmentApplyVisitGalleryBinding.apply {
            toolbarApplyVisitGallery.apply {
                title = "전시실 방문 신청"

                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    visitGalleryActivity.removeFragment(VisitGalleryFragmentName.APPLY_VISIT_GALLERY_FRAGMENT)
                }
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
                visitGalleryActivity.removeFragment(VisitGalleryFragmentName.APPLY_VISIT_GALLERY_FRAGMENT)
            }
        }
    }

}