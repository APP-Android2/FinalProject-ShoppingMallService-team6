package kr.co.lion.unipiece.ui.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentVisitGalleryHistoryBinding
import kr.co.lion.unipiece.databinding.RowVisitGalleryHistoryBinding
import kr.co.lion.unipiece.util.VisitGalleryFragmentName

class VisitGalleryHistoryFragment : Fragment() {

    lateinit var fragmentVisitGalleryHistoryBinding: FragmentVisitGalleryHistoryBinding
    lateinit var visitGalleryActivity: VisitGalleryActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentVisitGalleryHistoryBinding = FragmentVisitGalleryHistoryBinding.inflate(inflater)
        visitGalleryActivity = activity as VisitGalleryActivity

        settingToolbar()
        settingFabApplyVisitGallery()
        settingRecyclerView()

        return fragmentVisitGalleryHistoryBinding.root
    }

    // 툴바 셋팅
    private fun settingToolbar(){
        fragmentVisitGalleryHistoryBinding.apply {
            toolbarVisitGalleryHistory.apply {
                title = "전시실 방문 신청 목록"

                inflateMenu(R.menu.menu_home)

                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    visitGalleryActivity.finish()
                }
            }
        }
    }

    // 방문 신청 버튼
    private fun settingFabApplyVisitGallery(){
        fragmentVisitGalleryHistoryBinding.fabApplyVisitGallery.apply {
            setOnClickListener {
                // 추후 전달할 데이터는 여기에 담기
                val applyBundle = Bundle()
                // 전시실 방문 신청 이동
                visitGalleryActivity.replaceFragment(VisitGalleryFragmentName.APPLY_VISIT_GALLERY_FRAGMENT,true, applyBundle)
            }
        }
    }

    // 리사이클러 뷰 셋팅
    private fun settingRecyclerView(){
        fragmentVisitGalleryHistoryBinding.recyclerViewVisitGalleryHistory.apply {
            // 어댑터
            adapter = RecyclerViewAdapter()
            // 레이아웃 매니저
            layoutManager = LinearLayoutManager(visitGalleryActivity)
            // 데코레이션
            val deco = MaterialDividerItemDecoration(visitGalleryActivity, MaterialDividerItemDecoration.VERTICAL)
            addItemDecoration(deco)
        }
    }

    // 리사이클러 뷰 어댑터 셋팅
    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){
        inner class ViewHolder(rowVisitGalleryHistoryBinding: RowVisitGalleryHistoryBinding):RecyclerView.ViewHolder(rowVisitGalleryHistoryBinding.root){
            val rowVisitGalleryHistoryBinding:RowVisitGalleryHistoryBinding

            init {
                this.rowVisitGalleryHistoryBinding = rowVisitGalleryHistoryBinding

                this.rowVisitGalleryHistoryBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val rowVisitGalleryHistoryBinding = RowVisitGalleryHistoryBinding.inflate(layoutInflater)
            val viewHolder = ViewHolder(rowVisitGalleryHistoryBinding)
            return viewHolder
        }

        override fun getItemCount(): Int {
            return 20
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // 날짜
            holder.rowVisitGalleryHistoryBinding.textViewRowVisitListDate.text="2024.04.01"
            // 이름
            holder.rowVisitGalleryHistoryBinding.textViewRowVisitListName.text="김길동 $position"
            // 연락처
            holder.rowVisitGalleryHistoryBinding.textViewRowVisitListPhoneNumber.text="010-1234-5678"
            // 방문인원
            holder.rowVisitGalleryHistoryBinding.textViewRowVisitListMemberCount.text="1명"
            // 승인상태
            holder.rowVisitGalleryHistoryBinding.textViewRowVisitListStatus.text="승인 대기중"
            // 신청 수정 버튼 여부
            holder.rowVisitGalleryHistoryBinding.buttonRowVisitListModify.isVisible = true
        }
    }

}