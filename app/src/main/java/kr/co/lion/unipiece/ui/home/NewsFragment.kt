package kr.co.lion.unipiece.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.databinding.FragmentNewsBinding
import kr.co.lion.unipiece.ui.home.viewModel.NewsInfoViewModel
import kr.co.lion.unipiece.ui.infomation.InfoOneActivity
import kr.co.lion.unipiece.util.gettingImageName
import kr.co.lion.unipiece.util.setImage

class NewsFragment(val imgRes: String) : Fragment() {

    lateinit var fragmentNewsBinding: FragmentNewsBinding

    val viewModel: NewsInfoViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentNewsBinding = FragmentNewsBinding.inflate(layoutInflater)

        requireActivity().setImage(fragmentNewsBinding.imageNews, imgRes)

        settingEvent()
        return fragmentNewsBinding.root
    }

    //이벤트 설정
    private fun settingEvent(){
        fragmentNewsBinding.apply {
            imageNews.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    val imgName = requireActivity().gettingImageName(imgRes)
                    val newsImage = viewModel.getNewsInfoByImage(imgName)
                    Log.d("test1234", "${imgRes}")

                    val newIntent = Intent(requireActivity(), InfoOneActivity::class.java)
                    newIntent.putExtra("newsImg", newsImage?.newsImg)
                    startActivity(newIntent)
                }
            }
        }
    }
}