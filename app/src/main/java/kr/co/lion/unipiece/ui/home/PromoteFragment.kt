package kr.co.lion.unipiece.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.databinding.FragmentPromoteBinding
import kr.co.lion.unipiece.ui.home.viewModel.HomeViewModel
import kr.co.lion.unipiece.ui.infomation.InfoOneActivity
import kr.co.lion.unipiece.util.gettingImageName
import kr.co.lion.unipiece.util.setImage

class PromoteFragment(val imgRes: String) : Fragment() {

    lateinit var fragmentPromoteBinding: FragmentPromoteBinding

    val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentPromoteBinding = FragmentPromoteBinding.inflate(layoutInflater)

        requireActivity().setImage(fragmentPromoteBinding.imagePromote, imgRes)

        settingEvent()
        return fragmentPromoteBinding.root
    }


    //이벤트 설정
    private fun settingEvent() {
        fragmentPromoteBinding.apply {
            imagePromote.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    val imageName = requireActivity().gettingImageName(imgRes)
                    val imageInfo = viewModel.getPromoteInfoByImage(imageName)

                    val newIntent = Intent(requireActivity(), InfoOneActivity::class.java)
                    newIntent.putExtra("promoteImg", imageInfo?.promoteImg)
                    startActivity(newIntent)
                }
            }
        }
    }
}