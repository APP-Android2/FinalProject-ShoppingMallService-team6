package kr.co.lion.unipiece.ui.mygallery

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.isVisible
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivitySalesApplicationBinding
import kr.co.lion.unipiece.databinding.CategoryDialogBinding
import kr.co.lion.unipiece.databinding.DialogCustomBinding
import kr.co.lion.unipiece.util.CustomDialog
import kr.co.lion.unipiece.util.hideSoftInput
import kr.co.lion.unipiece.util.isKeyboardVisible
import kr.co.lion.unipiece.util.showSoftInput

class SalesApplicationActivity : AppCompatActivity() {

    lateinit var binding: ActivitySalesApplicationBinding
    lateinit var dialogCustomBinding: DialogCustomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalesApplicationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingToolbar()
        settingView()
    }

    fun settingToolbar() {
        binding.apply {
            toolbarSalesApplication.apply {
                title = "작품 등록 신청"

                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    finish()
                }
            }
        }
    }

    fun settingView() {
        binding.apply {
            imageViewSalesApplication.isVisible = false

            textFieldSalesApplicationCategory.setOnClickListener {
                showCategoryDialog()
            }

            textFieldSalesApplicationDate.setOnClickListener {
                showDatePickerDialog()
            }

            textFieldSalesApplicationPieceName.apply {
                setOnEditorActionListener { v, actionId, event ->
                    this@SalesApplicationActivity.hideSoftInput()
                    showCategoryDialog()
                    true
                }
            }

            textFieldSalesApplicationPrice.apply {
                setOnEditorActionListener { v, actionId, event ->
                    this@SalesApplicationActivity.hideSoftInput()
                    showDatePickerDialog()
                    true
                }
            }

            buttonSalesApplicationSubmit.setOnClickListener {
                MaterialAlertDialogBuilder(this@SalesApplicationActivity, R.style.Theme_Category_App_MaterialAlertDialog).apply {
                    setTitle("작품 등록 신청 완료")
                    setMessage("작품 등록 신청이 완료되었습니다.\n작품 등록 완료 시까지 1~2일 정도 소요됩니다.")

                    setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                        finish()
                    }

                    show()
                }
            }
        }
    }

    fun showDatePickerDialog() {
        val today = MaterialDatePicker.todayInUtcMilliseconds()

        val constraintsBuilder = CalendarConstraints.Builder()
            .setEnd(today)

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraintsBuilder.build())
            .setTheme(R.style.Theme_App_DatePicker)
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            binding.textFieldSalesApplicationDate.setText(datePicker.headerText)

            if(binding.textInputLayoutSalesApplicationDate.isHelperTextEnabled) {
                binding.textInputLayoutSalesApplicationDate.isHelperTextEnabled = false
            }

            this@SalesApplicationActivity.showSoftInput(binding.textFieldSalesApplicationDescription)
        }

        datePicker.show(supportFragmentManager, datePicker.toString())
    }

    fun showCategoryDialog() {
        MaterialAlertDialogBuilder(this@SalesApplicationActivity, R.style.Theme_Category_App_MaterialAlertDialog).apply {
            setTitle("카테고리")

            if(this@SalesApplicationActivity.isKeyboardVisible()) {
                this@SalesApplicationActivity.hideSoftInput()
            }

            val categoryDialogBinding = CategoryDialogBinding.inflate(layoutInflater)
            setView(categoryDialogBinding.root)

            categoryDialogBinding.apply {
                radioGroupArt.isVisible = false
                radioGroupHumanities.isVisible = false
                radioGroupEngineering.isVisible = false

                cardViewCategoryDialogArt.setOnClickListener {
                    toggleRadioGroupVisibility(radioGroupArt, imageViewArt, !radioGroupArt.isVisible)
                    hideOtherRadioGroups(radioGroupHumanities, radioGroupEngineering)
                    updateImageViewVisibility(imageViewHumanities, imageViewEngineering)
                    clearCheckedRadioButtons(radioGroupHumanities, radioGroupEngineering)
                }

                cardViewCategoryDialogHumanities.setOnClickListener {
                    toggleRadioGroupVisibility(radioGroupHumanities, imageViewHumanities, !radioGroupHumanities.isVisible)
                    hideOtherRadioGroups(radioGroupArt, radioGroupEngineering)
                    updateImageViewVisibility(imageViewArt, imageViewEngineering)
                    clearCheckedRadioButtons(radioGroupArt, radioGroupEngineering)
                }

                cardViewCategoryDialogEngineering.setOnClickListener {
                    toggleRadioGroupVisibility(radioGroupEngineering, imageViewEngineering, !radioGroupEngineering.isVisible)
                    hideOtherRadioGroups(radioGroupArt, radioGroupHumanities)
                    updateImageViewVisibility(imageViewArt, imageViewHumanities)
                    clearCheckedRadioButtons(radioGroupArt, radioGroupHumanities)
                }
            }

            setNegativeButton("취소", null)
            setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                val selectedRadioButton = when {
                    categoryDialogBinding.radioGroupArt.checkedRadioButtonId != -1
                    -> categoryDialogBinding.radioGroupArt.findViewById<RadioButton>(categoryDialogBinding.radioGroupArt.checkedRadioButtonId)

                    categoryDialogBinding.radioGroupHumanities.checkedRadioButtonId != -1
                    -> categoryDialogBinding.radioGroupHumanities.findViewById<RadioButton>(categoryDialogBinding.radioGroupHumanities.checkedRadioButtonId)

                    else -> categoryDialogBinding.radioGroupEngineering.findViewById<RadioButton>(categoryDialogBinding.radioGroupEngineering.checkedRadioButtonId)
                }
                binding.textFieldSalesApplicationCategory.setText(selectedRadioButton?.text ?: "")

                this@SalesApplicationActivity.showSoftInput(binding.textFieldSalesApplicationPrice)
            }

            show()
        }
    }

    fun toggleRadioGroupVisibility(radioGroup: RadioGroup, imageView: ImageView, show: Boolean) {
        radioGroup.isVisible = !radioGroup.isVisible
        imageView.setImageResource(if (show) R.drawable.arrowdropup_icon else R.drawable.arrowdropdown_icon)
    }

    fun hideOtherRadioGroups(vararg radioGroups: RadioGroup) {
        radioGroups.forEach { it.isVisible = false }
    }

    fun updateImageViewVisibility(imageView1: ImageView, imageView2: ImageView) {
        imageView1.setImageResource(R.drawable.arrowdropdown_icon)
        imageView2.setImageResource(R.drawable.arrowdropdown_icon)
    }

    fun clearCheckedRadioButtons(vararg radioGroups: RadioGroup) {
        radioGroups.forEach { it.clearCheck() }
    }
}