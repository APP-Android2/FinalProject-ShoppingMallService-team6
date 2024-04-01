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
import kr.co.lion.unipiece.util.ToolUtil

class SalesApplicationActivity : AppCompatActivity() {

    lateinit var binding: ActivitySalesApplicationBinding

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
                title = "작품 판매 신청"

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
                    ToolUtil.hideSoftInput(this@SalesApplicationActivity)
                    showCategoryDialog()
                    true
                }
            }

            textFieldSalesApplicationPrice.apply {
                setOnEditorActionListener { v, actionId, event ->
                    ToolUtil.hideSoftInput(this@SalesApplicationActivity)
                    showDatePickerDialog()
                    true
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
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            binding.textFieldSalesApplicationDate.setText(datePicker.headerText)

            if(binding.textInputLayoutSalesApplicationDate.isHelperTextEnabled) {
                binding.textInputLayoutSalesApplicationDate.isHelperTextEnabled = false
            }

            ToolUtil.showSoftInput(this@SalesApplicationActivity, binding.textFieldSalesApplicationDescription)
        }

        datePicker.show(supportFragmentManager, datePicker.toString())
    }

    fun showCategoryDialog() {
        MaterialAlertDialogBuilder(this@SalesApplicationActivity).apply {
            setTitle("카테고리")

            val categoryDialogBinding = CategoryDialogBinding.inflate(layoutInflater)
            setView(categoryDialogBinding.root)

            categoryDialogBinding.apply {
                radioGroup1.isVisible = false
                radioGroup2.isVisible = false
                radioGroup3.isVisible = false

                cardViewCategoryDialogArt.setOnClickListener {
                    toggleRadioGroupVisibility(radioGroup1, imageViewCategoryDialogArt, !radioGroup1.isVisible)
                }

                cardViewCategoryDialogHumanities.setOnClickListener {
                    toggleRadioGroupVisibility(radioGroup2, imageViewCategoryDialogHumanities, !radioGroup2.isVisible)
                }

                cardViewCategoryDialogEngineering.setOnClickListener {
                    toggleRadioGroupVisibility(radioGroup3, imageViewCategoryDialogEngineering, !radioGroup3.isVisible)
                }
            }

            setNegativeButton("취소", null)
            setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                val selectedRadioButton = when {
                    categoryDialogBinding.radioGroup1.checkedRadioButtonId != -1 -> categoryDialogBinding.radioGroup1.findViewById<RadioButton>(categoryDialogBinding.radioGroup1.checkedRadioButtonId)
                    categoryDialogBinding.radioGroup2.checkedRadioButtonId != -1 -> categoryDialogBinding.radioGroup2.findViewById<RadioButton>(categoryDialogBinding.radioGroup2.checkedRadioButtonId)
                    else -> categoryDialogBinding.radioGroup3.findViewById<RadioButton>(categoryDialogBinding.radioGroup3.checkedRadioButtonId)
                }
                binding.textFieldSalesApplicationCategory.setText(selectedRadioButton?.text ?: "")

                ToolUtil.showSoftInput(this@SalesApplicationActivity, binding.textFieldSalesApplicationPrice)
            }

            show()
        }
    }

    fun toggleRadioGroupVisibility(radioGroup: RadioGroup, imageView: ImageView, show: Boolean) {
        radioGroup.isVisible = !radioGroup.isVisible
        imageView.setImageResource(if (show) R.drawable.arrowdropup_icon else R.drawable.arrowdropdown_icon)
    }
}