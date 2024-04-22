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
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.databinding.FragmentGalleryBinding
import kr.co.lion.unipiece.ui.home.viewModel.HomeViewModel
import kr.co.lion.unipiece.ui.infomation.InfoOneActivity
import kr.co.lion.unipiece.util.gettingImageName
import kr.co.lion.unipiece.util.setImage

class GalleryFragment(val imgRes : String) : Fragment() {

    lateinit var fragmentGalleryBinding: FragmentGalleryBinding

    val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentGalleryBinding = FragmentGalleryBinding.inflate(layoutInflater)

        requireActivity().setImage(fragmentGalleryBinding.imageGallery, imgRes)

        settingEvent()
        return fragmentGalleryBinding.root
    }

    //이벤트 설정
    private fun settingEvent(){
        fragmentGalleryBinding.apply {
            imageGallery.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    val imageName = requireActivity().gettingImageName(imgRes)
                    //Log.d("test1234", imageName)
                    val galleryImg = viewModel.getGalleryInfoByImg(imageName)

                    val newIntent = Intent(requireActivity(), InfoOneActivity::class.java)
                    newIntent.putExtra("galleryInfoImg", galleryImg?.galleryInfoImg)
                    startActivity(newIntent)
                }
            }
        }
    }
}