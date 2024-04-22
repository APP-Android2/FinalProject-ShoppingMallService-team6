package kr.co.lion.unipiece.ui.mypage

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.FragmentApplyVisitGalleryBinding
import kr.co.lion.unipiece.model.VisitAddData
import kr.co.lion.unipiece.ui.mypage.viewmodel.ApplyVisitGalleryViewModel
import kr.co.lion.unipiece.util.VisitGalleryFragmentName
import kr.co.lion.unipiece.util.hideSoftInput
import java.text.SimpleDateFormat

class ApplyVisitGalleryFragment : Fragment() {

    lateinit var fragmentApplyVisitGalleryBinding: FragmentApplyVisitGalleryBinding
    private val applyVisitGalleryViewModel:ApplyVisitGalleryViewModel by viewModels()

    // 신청 수정 여부
    private val isModify:Boolean by lazy {
        requireArguments().getBoolean("isModify", false)
    }

    val userIdx by lazy {
        UniPieceApplication.prefs.getUserIdx("userIdx",-1)
    }

    val visitIdx by lazy {
        requireArguments().getInt("visitIdx", -1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentApplyVisitGalleryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_apply_visit_gallery, container, false)
        fragmentApplyVisitGalleryBinding.viewModel = applyVisitGalleryViewModel
        fragmentApplyVisitGalleryBinding.lifecycleOwner = this

        fetchData()
        settingToolbar()
        settingButtonMember()
        settingDatePicker()
        settingButtonApplyConfirm()

        return fragmentApplyVisitGalleryBinding.root
    }

    private fun fetchData(){
        lifecycleScope.launch {
            if (isModify){
                val result = applyVisitGalleryViewModel.getVisitAddData(visitIdx)
                if(!result){
                    showSnackBar("네트워크 오류, 잠시후 다시 시도해주세요")
                    removeFragment()
                }
            }else{
                val emptyData = VisitAddData()
                applyVisitGalleryViewModel.setEmptyData(emptyData)
            }
            fragmentApplyVisitGalleryBinding.progressBarApplyVisit.visibility = View.GONE
        }
    }

    // 툴바 셋팅
    private fun settingToolbar(){
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
        fragmentApplyVisitGalleryBinding.buttonDatePicker.apply {
            setOnClickListener {
                requireActivity().hideSoftInput()
                requireActivity().setTheme(R.style.Theme_Material3)

                val today = MaterialDatePicker.todayInUtcMilliseconds()

                val constraintsBuilder = CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.from(today))

                val datePicker = MaterialDatePicker.Builder.datePicker()
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setCalendarConstraints(constraintsBuilder.build())
                    .setTheme(R.style.Theme_App_DatePicker)
                    .build()

                datePicker.addOnPositiveButtonClickListener { selection ->
                    fragmentApplyVisitGalleryBinding.buttonDatePicker.text = datePicker.headerText
                }

                datePicker.show(parentFragmentManager, datePicker.toString())
            }
        }
    }


    private fun settingButtonMember(){
        fragmentApplyVisitGalleryBinding.apply {
            // 감소 버튼
            buttonMemberCountDown.setOnClickListener {
                var value = editTextApplyVisitGalleryMemberCounter.text.toString().toInt() -1
                if(value<1){
                    value = 1
                }
                editTextApplyVisitGalleryMemberCounter.setText(value.toString())
            }
            // 증가 버튼
            buttonMemberCountUp.setOnClickListener {
                val value = editTextApplyVisitGalleryMemberCounter.text.toString().toInt() +1
                editTextApplyVisitGalleryMemberCounter.setText(value.toString())
            }
        }
    }

    // 신청 완료 버튼
    private fun settingButtonApplyConfirm(){
        fragmentApplyVisitGalleryBinding.buttonApplyVisitGalleryConfirm.apply {
            setOnClickListener {
                fragmentApplyVisitGalleryBinding.progressBarApplyVisit.visibility = View.VISIBLE
                requireActivity().hideSoftInput()
                // 입력 사항 확인
                if(isInputEmpty())return@setOnClickListener

                if(isModify){
                    // 수정인 경우
                    modifyData()
                }else{
                    // 신청인 경우
                    insertData()
                }
            }
        }
    }

    private fun insertData(){
        lifecycleScope.launch {
            var isSuccess = true

            val visitIdx = applyVisitGalleryViewModel.getVisitAddSequence()
            if(visitIdx == -1){
                isSuccess = false
                return@launch
            }
            if(applyVisitGalleryViewModel.visitAddData.value != null){
                // 방문일을 타임스탬프로 변환
                val date = dateToTimestamp()
                // 신청할 데이터
                val addData = date?.let { it1 ->
                    applyVisitGalleryViewModel.visitAddData.value!!.copy(
                        visitorDate = it1,
                        visitState = "승인 대기",
                        userIdx = userIdx,
                        visitIdx = visitIdx + 1
                    )
                }

                // 방문 신청 데이터 추가
                val insertResult = addData?.let { it1 ->
                    applyVisitGalleryViewModel.insertVisitAddData(it1)
                }
                if(!insertResult!!){
                    isSuccess = false
                    return@launch
                }

                // 시퀀스 업데이트
                val sequenceResult = applyVisitGalleryViewModel.updateVisitAddSequence(visitIdx+1)
                if(!sequenceResult){
                    isSuccess = false
                    return@launch
                }
            }else{
                isSuccess = false
            }

            if(isSuccess){
                showSnackBar("신청이 완료되었습니다")
                removeFragment()
            }else{
                fragmentApplyVisitGalleryBinding.progressBarApplyVisit.visibility = View.GONE
                showSnackBar("네트워크 오류, 잠시후 다시 시도해주세요")
            }
        }
    }

    private fun modifyData(){
        // 수정인 경우
        lifecycleScope.launch {
            // 방문일을 타임스탬프로 변환
            val date = dateToTimestamp()
            val addData = applyVisitGalleryViewModel.visitAddData.value?.copy(
                visitorDate = date!!
            )
            val updateResult = applyVisitGalleryViewModel.updateVisitAddData(addData!!)
            if(updateResult){
                showSnackBar("수정이 완료되었습니다")
                removeFragment()
            }else{
                fragmentApplyVisitGalleryBinding.progressBarApplyVisit.visibility = View.GONE
                showSnackBar("네트워크 오류, 잠시후 다시 시도해주세요")
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun dateToTimestamp(): Timestamp? {
        // 방문일을 타임스탬프로 변환
        val string = fragmentApplyVisitGalleryBinding.buttonDatePicker.text.toString()
            .replace("년 ","-")
            .replace("월 ","-")
            .replace("일","")
        val timestamp = SimpleDateFormat("yyyy-MM-dd")
            .parse(string)
            ?.let { it1 -> Timestamp(it1) }
        return timestamp
    }

    private fun isInputEmpty():Boolean{
        resetError()
        var result = false
        with(fragmentApplyVisitGalleryBinding){
            if(textInputApplyVisitGalleryName.text.isNullOrBlank()){
                layoutVisitorName.error = "이름을 입력해주세요"
                result = true
            }
            if(textInputApplyVisitGalleryPhoneNumber.text.isNullOrBlank()){
                layoutVisitorPhoneNumber.error = "휴대폰 번호를 입력해주세요"
                result = true
            }
        }
        return result
    }

    private fun resetError(){
        with(fragmentApplyVisitGalleryBinding){
            layoutVisitorName.error = ""
            layoutVisitorName.isErrorEnabled = false
            layoutVisitorPhoneNumber.error = ""
            layoutVisitorPhoneNumber.isErrorEnabled = false
        }
    }

    private fun showSnackBar(msg:String){
        Snackbar.make(requireView(), msg, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(requireActivity(), R.color.first))
            .setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
            .show()
    }

    // 프래그먼트 제거 메서드
    private fun removeFragment(){
        parentFragmentManager.popBackStack(VisitGalleryFragmentName.APPLY_VISIT_GALLERY_FRAGMENT.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

}