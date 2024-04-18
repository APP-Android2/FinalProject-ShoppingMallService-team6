package kr.co.lion.unipiece.ui.author

import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.firebase.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.FragmentAuthorReviewBottomSheetBinding
import kr.co.lion.unipiece.model.AuthorReviewData
import kr.co.lion.unipiece.repository.UserInfoRepository
import kr.co.lion.unipiece.ui.author.adapter.AuthorReviewAdapter
import kr.co.lion.unipiece.ui.author.viewmodel.AuthorReviewViewModel


class AuthorReviewBottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var fragmentAuthorReviewBottomSheetBinding: FragmentAuthorReviewBottomSheetBinding
    private val authorReviewViewModel:AuthorReviewViewModel by viewModels()

    val userIdx by lazy {
        UniPieceApplication.prefs.getUserIdx("userIdx",0)
    }

    val authorIdx by lazy {
        requireArguments().getInt("authorIdx")
    }

    val authorIsMe by lazy {
        requireArguments().getBoolean("authorIsMe")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentAuthorReviewBottomSheetBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_author_review_bottom_sheet, container, false)
        fragmentAuthorReviewBottomSheetBinding.authorReviewViewModel = authorReviewViewModel
        fragmentAuthorReviewBottomSheetBinding.lifecycleOwner = this

        fetchData()
        initView()
        settingButtonAuthorReviewAdd()

        return fragmentAuthorReviewBottomSheetBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingRecyclerView()
    }

    private fun fetchData(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 리뷰 데이터 가져오기
                authorReviewViewModel.getReviewList(authorIdx)
            }
        }
    }

    private fun initView(){
        // 작가 본인인 경우 댓글 입력창 없애기
        if(authorIsMe){
            fragmentAuthorReviewBottomSheetBinding.layoutInputReview.visibility = View.GONE
        }
    }

    // 리사이클러 뷰 셋팅
    private fun settingRecyclerView(){
        val reviewAdapter =
                AuthorReviewAdapter(userIdx, emptyList(), deleteListener = { reviewIdx ->
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

        // 리뷰 데이터가 바뀌는 시점에 업데이트
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                authorReviewViewModel.authorReviewList.observe(viewLifecycleOwner) { value ->
                    reviewAdapter.updateList(value)
                }
            }
        }
    }



    // 확인 버튼
    private fun settingButtonAuthorReviewAdd(){
        fragmentAuthorReviewBottomSheetBinding.buttonAuthorReviewAdd.setOnClickListener {
            addReview()
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
                val userNickname = UserInfoRepository().getUserDataByIdx(userIdx)?.nickName.toString()
                val authorIdx = authorIdx
                val reviewTime = Timestamp.now()
                val reviewData = AuthorReviewData(reviewSequence, userIdx, userNickname, authorIdx, reviewContent, reviewTime)

                // 작성 내용 저장
                authorReviewViewModel.insertReviewData(reviewData)
                // 시퀀스 값 업데이트
                authorReviewViewModel.updateReviewSequence(reviewSequence)
                // 리뷰 정보 다시 가져오기
                authorReviewViewModel.getReviewList(authorIdx)
                // 입력칸 초기화
                authorReviewViewModel.authorReviewContent.value = ""
                // 키보드 내리기
                rHideSoftInput()
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
    private fun setBottomSheetHeight(bottomSheetDialog: BottomSheetDialog){
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
    private fun getBottomSheetDialogHeight() : Int{
        return (getWindowHeight() * 0.7).toInt()
    }

    // 사용자 단말기 액정의 길이를 구해 반환하는 메서드
    private fun getWindowHeight() : Int {
        // 화면 크기 정보를 담을 배열 객체
        val displayMetrics = DisplayMetrics()
        // 액정의 가로/세로 길이 정보를 담아준다.
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        // 세로 길이를 반환해준다.
        return displayMetrics.heightPixels
    }

    private fun rHideSoftInput(){
        // KeyBoardUtil.kt의 hideSoftInput()는 BottomSheetDialog 에서 안되는 것 같음
        // requireActivity().window.currentFocus 값이 null로 나옴
        // https://stackoverflow.com/questions/66219617/how-to-hide-soft-key-in-bottom-sheet-dialog-in-android 참고함
        view?.clearFocus()
        val rInputMethodManager = requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        rInputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

}

