package kr.co.lion.unipiece.util

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kr.co.lion.unipiece.databinding.DialogCustomBinding


class CustomDialog(val title:String, val message:String) : DialogFragment() {

    lateinit var binding: DialogCustomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DialogCustomBinding.inflate(layoutInflater)

        //레이아웃 배경을 투명하게 해줌
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.textTitle.text = title
        binding.textMessage.text = message

        binding.buttonOK.setOnClickListener {
            buttonCilckListener.okButtonClick()
            dismiss()
        }

        binding.buttonNo.setOnClickListener {
            buttonCilckListener.noButtonClick()
            dismiss()
        }



        return binding.root
    }

    interface OnButtonClickListener{
        fun okButtonClick()
        fun noButtonClick()
    }


    //클릭 이벤트 설정
    fun setButtonClickListener(buttonClickListener: OnButtonClickListener){
        this.buttonCilckListener = buttonClickListener
    }


    //클릭 이벤트 설정
    private lateinit var buttonCilckListener: OnButtonClickListener
}