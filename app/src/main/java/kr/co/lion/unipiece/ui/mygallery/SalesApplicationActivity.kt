package kr.co.lion.unipiece.ui.mygallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivitySalesApplicationBinding
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

            textFieldSalesApplicationDate.setOnClickListener {
                showDatePickerDialog()
            }

            textFieldSalesApplicationPieceName.apply {
                setOnEditorActionListener { v, actionId, event ->
                    ToolUtil.hideSoftInput(this@SalesApplicationActivity)
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
}