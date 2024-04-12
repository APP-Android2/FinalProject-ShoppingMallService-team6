package kr.co.lion.unipiece.ui.author

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentAuthorReviewBottomSheetBinding
import kr.co.lion.unipiece.databinding.RowAuthorPiecesBinding
import kr.co.lion.unipiece.databinding.RowAuthorReviewBottomSheetBinding
import kr.co.lion.unipiece.ui.author.adapter.AuthorPiecesAdapter
import kr.co.lion.unipiece.ui.author.adapter.AuthorReviewAdapter

class AuthorReviewBottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var fragmentAuthorReviewBottomSheetBinding: FragmentAuthorReviewBottomSheetBinding
    lateinit var reviewAdapter: AuthorReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentAuthorReviewBottomSheetBinding = FragmentAuthorReviewBottomSheetBinding.inflate(inflater)

        settingButtonAuthorReviewAdd()
        
        return fragmentAuthorReviewBottomSheetBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingRecyclerView()
    }


    // 리사이클러 뷰 셋팅
    private fun settingRecyclerView(){
        // 테스트 데이터
        val reviewList = arrayListOf<Any>(
            "test","test","test","test","test","test","test","test","test","test"
        )

        // 리사이클러뷰 어댑터
        reviewAdapter = AuthorReviewAdapter(reviewList)

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



    // 확인 버튼
    private fun settingButtonAuthorReviewAdd(){
        // 작성 내용 체크
        
        // 작성 내용 저장
    }
}