package kr.co.lion.unipiece.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentNewsBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.infomation.InfoOneActivity

class NewsFragment(val imgRes : Int) : Fragment() {

    lateinit var fragmentNewsBinding: FragmentNewsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentNewsBinding = FragmentNewsBinding.inflate(layoutInflater)
        fragmentNewsBinding.imageNews.setImageResource(imgRes)
        settingEvent()
        return fragmentNewsBinding.root
    }

    //이벤트 설정
    private fun settingEvent(){
        fragmentNewsBinding.apply {
            imageNews.setOnClickListener {
                startActivity(Intent(requireActivity(), InfoOneActivity::class.java))
            }
        }
    }
}