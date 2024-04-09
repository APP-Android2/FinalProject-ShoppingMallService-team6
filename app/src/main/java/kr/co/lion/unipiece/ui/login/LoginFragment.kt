package kr.co.lion.unipiece.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentLoginBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.util.CustomDialog
import kr.co.lion.unipiece.util.LoginFragmentName


class LoginFragment : Fragment() {

    lateinit var fragmentLoginBinding: FragmentLoginBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentLoginBinding = FragmentLoginBinding.inflate(layoutInflater)
        settingEvent()
        return fragmentLoginBinding.root
    }

    //버튼 클릭
    private fun settingEvent(){
        fragmentLoginBinding.apply {
            buttonLoginGoJoin.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.loginContainer, JoinFragment())
                    .addToBackStack(LoginFragmentName.JOIN_FRAGMENT.str)
                    .commit()
            }
            buttonLogin.setOnClickListener {
                val newIntent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(newIntent)
                requireActivity().finish()
            }
            imageKaKao.setOnClickListener {
                val dialog = CustomDialog("안녕", "반가워")
                dialog.show(parentFragmentManager, "CustomDialog")
            }
        }
    }
}