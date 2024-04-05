package kr.co.lion.unipiece.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentPromoteBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.infomation.InfoOneActivity

class PromoteFragment(val imgRes : Int) : Fragment() {

    lateinit var fragmentPromoteBinding: FragmentPromoteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentPromoteBinding = FragmentPromoteBinding.inflate(layoutInflater)
        fragmentPromoteBinding.imagePromote.setImageResource(imgRes)
        settingEvent()
        return fragmentPromoteBinding.root
    }


    //이벤트 설정
    private fun settingEvent(){
        fragmentPromoteBinding.apply {
            imagePromote.setOnClickListener {
                startActivity(Intent(requireActivity(), InfoOneActivity::class.java))
            }
        }
    }

}