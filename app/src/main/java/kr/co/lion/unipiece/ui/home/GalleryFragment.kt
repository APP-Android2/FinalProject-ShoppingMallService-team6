package kr.co.lion.unipiece.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentGalleryBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.infomation.InfoOneActivity

class GalleryFragment(val imgRes : Int) : Fragment() {

    lateinit var fragmentGalleryBinding: FragmentGalleryBinding

    lateinit var mainActivity:MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentGalleryBinding = FragmentGalleryBinding.inflate(layoutInflater)
        fragmentGalleryBinding.imageGallery.setImageResource(imgRes)
        mainActivity = activity as MainActivity
        settingEvent()
        return fragmentGalleryBinding.root
    }

    //이벤트 설정
    private fun settingEvent(){
        fragmentGalleryBinding.apply {
            imageGallery.setOnClickListener {
                startActivity(Intent(mainActivity, InfoOneActivity::class.java))
            }
        }
    }
}