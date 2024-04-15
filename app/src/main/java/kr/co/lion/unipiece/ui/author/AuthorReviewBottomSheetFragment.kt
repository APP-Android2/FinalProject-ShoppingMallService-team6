package kr.co.lion.unipiece.ui.author

import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
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

    // 다이얼로그가 만들어질 때 자동으로 호출되는 메서드
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // 다이얼로그를 받는다.
        val dialog = super.onCreateDialog(savedInstanceState)
        // 다이얼로그가 보일 때 동작하는 리스너
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            // 높이를 설정한다.
            setBottomSheetHeight(bottomSheetDialog)
        }

        return dialog
    }

    // BottomSheet의 높이를 설정해준다.
    fun setBottomSheetHeight(bottomSheetDialog: BottomSheetDialog){
        // BottomSheet의 기본 뷰 객체를 가져온다
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)!!
        // BottomSheet 높이를 설정할 수 있는 객체를 생성한다.
        val behavior = BottomSheetBehavior.from(bottomSheet)
        // 높이를 설정한다.
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = getBottomSheetDialogHeight()
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED

    }

    // BottomSheet의 높이를 구한다(화면 액정의 85% 크기)
    fun getBottomSheetDialogHeight() : Int{
        return (getWindowHeight() * 0.7).toInt()
    }

    // 사용자 단말기 액정의 길이를 구해 반환하는 메서드
    fun getWindowHeight() : Int {
        // 화면 크기 정보를 담을 배열 객체
        val displayMetrics = DisplayMetrics()
        // 액정의 가로/세로 길이 정보를 담아준다.
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        // 세로 길이를 반환해준다.
        return displayMetrics.heightPixels
    }
}