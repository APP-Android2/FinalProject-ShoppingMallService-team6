package kr.co.lion.unipiece.ui.author

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.firebase.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentAuthorReviewBottomSheetBinding
import kr.co.lion.unipiece.model.AuthorReviewData
import kr.co.lion.unipiece.ui.author.adapter.AuthorReviewAdapter
import kr.co.lion.unipiece.ui.author.viewmodel.AuthorReviewViewModel
import kr.co.lion.unipiece.util.hideSoftInput

class AuthorReviewBottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var fragmentAuthorReviewBottomSheetBinding: FragmentAuthorReviewBottomSheetBinding
    private val authorReviewViewModel:AuthorReviewViewModel by viewModels()

    val userIdx by lazy {
        requireArguments().getInt("userIdx")
    }
    val authorIdx by lazy {
        requireArguments().getInt("authorIdx")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentAuthorReviewBottomSheetBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_author_review_bottom_sheet, container, false)
        fragmentAuthorReviewBottomSheetBinding.authorReviewViewModel = authorReviewViewModel
        fragmentAuthorReviewBottomSheetBinding.lifecycleOwner = this

        settingButtonAuthorReviewAdd()

        return fragmentAuthorReviewBottomSheetBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingRecyclerView()
    }


    // 리사이클러 뷰 셋팅
    private fun settingRecyclerView(){
        lifecycleScope.launch(Dispatchers.Main) {
            authorReviewViewModel.getReviewList(authorIdx)
            authorReviewViewModel.authorReviewList.observe(viewLifecycleOwner) { value ->
                val reviewAdapter =
                    AuthorReviewAdapter(userIdx, value, deleteListener = { reviewIdx ->
                        lifecycleScope.launch(Dispatchers.IO){
                            // 리뷰 삭제
                            authorReviewViewModel.deleteReview(reviewIdx)
                            // 시퀀스 값 업데이트
                            val reviewSequence = authorReviewViewModel.getReviewSequence() -1
                            authorReviewViewModel.updateReviewSequence(reviewSequence)
                            // 리뷰 정보 다시 불러오기
                            authorReviewViewModel.getReviewList(authorIdx)
                        }
                    })

                // 리사이클러뷰 셋팅
                fragmentAuthorReviewBottomSheetBinding.recyclerViewAuthorReview.apply {
                    // 어댑터
                    adapter = reviewAdapter
                    // 레이아웃 매니저, 가로 방향 셋팅
                    layoutManager = LinearLayoutManager(requireActivity())
                    // 데코레이션
                    val deco = MaterialDividerItemDecoration(requireActivity(), MaterialDividerItemDecoration.VERTICAL)
                    addItemDecoration(deco)
                }
            }
        }
    }



    // 확인 버튼
    private fun settingButtonAuthorReviewAdd(){
        fragmentAuthorReviewBottomSheetBinding.buttonAuthorReviewAdd.setOnClickListener {
            addReview()
            requireActivity().hideSoftInput()
        }

        fragmentAuthorReviewBottomSheetBinding.textInputAuthorReview.setOnEditorActionListener { textView, i, keyEvent ->
            addReview()
            false
        }

    }

    private fun addReview(){
        val reviewContent = authorReviewViewModel.authorReviewContent.value
        if(reviewContent!!.isNotEmpty()){
            lifecycleScope.launch {
                val reviewSequence = authorReviewViewModel.getReviewSequence() + 1

                val userIdx = userIdx
                val authorIdx = authorIdx
                val reviewTime = Timestamp.now()
                val reviewData = AuthorReviewData(reviewSequence, userIdx, authorIdx, reviewContent, reviewTime)

                // 작성 내용 저장
                authorReviewViewModel.insertReviewData(reviewData)
                // 시퀀스 값 업데이트
                authorReviewViewModel.updateReviewSequence(reviewSequence)
                // 리뷰 정보 다시 가져오기
                authorReviewViewModel.getReviewList(authorIdx)
                // 입력칸 초기화
                authorReviewViewModel.authorReviewContent.value = ""
                Toast.makeText(requireActivity(), "작성 완료", Toast.LENGTH_SHORT).show()
            }
        }
    }
}