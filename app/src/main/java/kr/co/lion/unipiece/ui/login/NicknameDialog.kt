package kr.co.lion.unipiece.ui.login

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import kr.co.lion.unipiece.databinding.FragmentNicknameDialogBinding


class NicknameDialog(val title:String) : DialogFragment() {

    lateinit var binding : FragmentNicknameDialogBinding

    private lateinit var buttonClickListener: dialogButtonClickListener

    fun setNicknameButtonClickListener(buttonClickListener: dialogButtonClickListener){
        this.buttonClickListener = buttonClickListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentNicknameDialogBinding.inflate(layoutInflater)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.dialogNickname.text = title

        binding.buttonNicknameOk.setOnClickListener {
            buttonClickListener.nicknameOkButton()
            dismiss()

        }

        binding.buttonNicknameNo.setOnClickListener {
            buttonClickListener.nicknameNoButton()
            dismiss()
        }

        return binding.root
    }

    interface dialogButtonClickListener{
        fun nicknameOkButton()
        fun nicknameNoButton()
    }

}