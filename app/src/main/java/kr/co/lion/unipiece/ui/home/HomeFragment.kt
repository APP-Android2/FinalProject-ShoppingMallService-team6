package kr.co.lion.unipiece.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentHomeBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.author.AddAuthorActivity
import kr.co.lion.unipiece.ui.infomation.InfoAllActivity

class HomeFragment : Fragment() {

    lateinit var fragmentHomeBinding: FragmentHomeBinding

    lateinit var mainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        settingEvent()
        return fragmentHomeBinding.root
    }

    private fun settingEvent(){
        fragmentHomeBinding.apply {
            button.setOnClickListener {
                startActivity(Intent(mainActivity, AddAuthorActivity::class.java))
            }
            button2.setOnClickListener {
                startActivity(Intent(mainActivity, InfoAllActivity::class.java))
            }
        }
    }
}