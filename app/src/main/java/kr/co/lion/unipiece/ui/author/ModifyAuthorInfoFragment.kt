package kr.co.lion.unipiece.ui.author

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentModifyAuthorInfoBinding
import kr.co.lion.unipiece.util.AuthorInfoFragmentName

class ModifyAuthorInfoFragment : Fragment() {

    lateinit var fragmentModifyAuthorInfoBinding: FragmentModifyAuthorInfoBinding
    lateinit var authorInfoActivity: AuthorInfoActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentModifyAuthorInfoBinding = FragmentModifyAuthorInfoBinding.inflate(inflater)
        authorInfoActivity = activity as AuthorInfoActivity

        settingToolbar()
        settingImageViewEvent()
        settingButtonUpdateAuthor()
        settingButtonModifyAuthorInfo()

        return fragmentModifyAuthorInfoBinding.root
    }

    // 툴바 셋팅
    private fun settingToolbar() {
        fragmentModifyAuthorInfoBinding.apply {
            toolbarModifyAuthorInfo.apply {

                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    authorInfoActivity.removeFragment(AuthorInfoFragmentName.MODIFY_AUTHOR_INFO_FRAGMENT)
                }
            }
        }
    }

    // 이미지 뷰 클릭 시 사진 변경
    private fun settingImageViewEvent(){
        fragmentModifyAuthorInfoBinding.imageViewModifyAuthor.setOnClickListener {
            // 추후 수정
            authorInfoActivity.removeFragment(AuthorInfoFragmentName.MODIFY_AUTHOR_INFO_FRAGMENT)
        }
    }

    // 작가 갱신 버튼
    private fun settingButtonUpdateAuthor(){
        fragmentModifyAuthorInfoBinding.buttonModifyAuthorUpdateAuthor.setOnClickListener {
            // 작가 갱신 액티비티로 이동
            val updateAuthorIntent = Intent(requireActivity(), UpdateAuthorActivity::class.java)
            startActivity(updateAuthorIntent)
        }
    }

    // 수정 버튼
    private fun settingButtonModifyAuthorInfo(){
        fragmentModifyAuthorInfoBinding.buttonModifyAuthorInfoConfirm.setOnClickListener {
            // 추후 수정
            authorInfoActivity.removeFragment(AuthorInfoFragmentName.MODIFY_AUTHOR_INFO_FRAGMENT)
        }
    }

}