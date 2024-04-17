package kr.co.lion.unipiece.ui.author

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.FragmentModifyAuthorInfoBinding
import kr.co.lion.unipiece.ui.author.viewmodel.ModifyAuthorInfoViewModel
import kr.co.lion.unipiece.util.AuthorInfoFragmentName
import kr.co.lion.unipiece.util.setImage

class ModifyAuthorInfoFragment : Fragment() {

    lateinit var fragmentModifyAuthorInfoBinding: FragmentModifyAuthorInfoBinding
    private val modifyAuthorInfoViewModel: ModifyAuthorInfoViewModel by viewModels()

    val authorIdx by lazy {
        requireArguments().getInt("authorIdx")
    }

    val userIdx by lazy {
        UniPieceApplication.prefs.getUserIdx("userIdx", 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentModifyAuthorInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_modify_author_info, container, false)
        fragmentModifyAuthorInfoBinding.modifyAuthorInfoViewModel = modifyAuthorInfoViewModel
        fragmentModifyAuthorInfoBinding.lifecycleOwner = this



        lifecycleScope.launch {
            fetchData(authorIdx)
            settingToolbar()
            settingImageViewEvent()
            settingButtonUpdateAuthor()
            settingButtonModifyAuthorInfo()
        }


        return fragmentModifyAuthorInfoBinding.root
    }



    // 작가 정보 불러오기
    private fun fetchData(authorIdx:Int){
        lifecycleScope.launch {
            modifyAuthorInfoViewModel.getAuthorInfoData(authorIdx)

            // 작가 이미지 셋팅
            val authorImg = modifyAuthorInfoViewModel.authorInfoData.value?.authorImg
            val imageUrl = modifyAuthorInfoViewModel.getAuthorInfoImg(authorImg!!)
            requireActivity().setImage(fragmentModifyAuthorInfoBinding.imageViewModifyAuthor, imageUrl)
        }
    }

    // 툴바 셋팅
    private fun settingToolbar() {
        fragmentModifyAuthorInfoBinding.apply {
            toolbarModifyAuthorInfo.apply {

                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    removeFragment()
                }
            }
        }
    }

    // 이미지 뷰 클릭 시 사진 변경
    private fun settingImageViewEvent(){
        fragmentModifyAuthorInfoBinding.imageViewModifyAuthor.setOnClickListener {
            // 추후 수정
            removeFragment()
        }
    }

    // 작가 갱신 버튼
    private fun settingButtonUpdateAuthor(){
        fragmentModifyAuthorInfoBinding.buttonModifyAuthorUpdateAuthor.setOnClickListener {
            // 작가 갱신 액티비티로 이동
            val updateAuthorIntent = Intent(requireActivity(), UpdateAuthorActivity::class.java)
            updateAuthorIntent.putExtra("authorIdx", authorIdx)
            startActivity(updateAuthorIntent)
        }
    }

    // 수정 버튼
    private fun settingButtonModifyAuthorInfo(){
        fragmentModifyAuthorInfoBinding.buttonModifyAuthorInfoConfirm.setOnClickListener {
            // 추후 수정
            lifecycleScope.launch {
                modifyAuthorInfoViewModel.updateAuthorInfo()
                removeFragment()
            }

        }
    }

    private fun removeFragment(){
        parentFragmentManager.popBackStack(AuthorInfoFragmentName.MODIFY_AUTHOR_INFO_FRAGMENT.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}