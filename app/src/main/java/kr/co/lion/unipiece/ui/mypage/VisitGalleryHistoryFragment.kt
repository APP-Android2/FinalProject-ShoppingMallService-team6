package kr.co.lion.unipiece.ui.mypage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.FragmentVisitGalleryHistoryBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.mypage.adapter.VisitGalleryAdapter
import kr.co.lion.unipiece.ui.mypage.viewmodel.VisitGalleryViewModel
import kr.co.lion.unipiece.util.VisitGalleryFragmentName
import kr.co.lion.unipiece.util.setMenuIconColor

class VisitGalleryHistoryFragment : Fragment() {

    private lateinit var fragmentVisitGalleryHistoryBinding: FragmentVisitGalleryHistoryBinding
    private lateinit var visitAdapter: VisitGalleryAdapter
    private val visitGalleryViewModel:VisitGalleryViewModel by viewModels()

    val userIdx by lazy {
        UniPieceApplication.prefs.getUserIdx("userIdx", -1)
    }

    val applyResult by lazy {
        arguments?.getString("applyResult","")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentVisitGalleryHistoryBinding = FragmentVisitGalleryHistoryBinding.inflate(inflater)

        fetchData()
        settingToolbar()
        settingFabApplyVisitGallery()

        return fragmentVisitGalleryHistoryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingRecyclerView()

        if(applyResult != ""){
            Snackbar.make(view, applyResult!!, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(ContextCompat.getColor(requireActivity(), R.color.first))
                .setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                .show()
        }
    }

    private fun fetchData(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                visitGalleryViewModel.getVisitAddList(userIdx)
            }
        }
    }

    // 툴바 셋팅
    private fun settingToolbar(){
        fragmentVisitGalleryHistoryBinding.apply {
            toolbarVisitGalleryHistory.apply {
                title = "전시실 방문 신청 목록"

                inflateMenu(R.menu.menu_home)
                requireContext().setMenuIconColor(menu, R.id.menu_home, R.color.second)

                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_home -> {
                            val intent = Intent(requireActivity(), MainActivity::class.java)
                                .apply{ // MainActivity가 이미 실행 중인 경우 해당 인스턴스를 재사용합니다.
                                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                }

                            intent.putExtra("HomeFragment", true)
                            requireActivity().finish()
                            startActivity(intent)
                        }
                    }
                    true
                }

                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    requireActivity().finish()
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
                replaceFragment(applyBundle)
            }
        }
    }

    // 리사이클러 뷰 셋팅
    private fun settingRecyclerView(){
        // 리사이클러뷰 어댑터
        visitAdapter = VisitGalleryAdapter(emptyList()) { visitIdx ->
            val modifyBundle = Bundle()
            modifyBundle.putBoolean("isModify", true)
            modifyBundle.putInt("visitIdx", visitIdx)
            replaceFragment(modifyBundle)
        }

        // 리사이클러뷰 셋팅
        fragmentVisitGalleryHistoryBinding.recyclerViewVisitGalleryHistory.apply {
            // 어댑터
            adapter = visitAdapter
            // 레이아웃 매니저
            layoutManager = LinearLayoutManager(requireActivity())
            // 데코레이션
            val deco = MaterialDividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
            deco.dividerColor = ContextCompat.getColor(requireActivity(), R.color.lightgray)
            addItemDecoration(deco)
        }

        // 리뷰 데이터가 바뀌는 시점에 업데이트
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                visitGalleryViewModel.visitGalleryList.observe(viewLifecycleOwner) { value ->
                    visitAdapter.updateList(value)
                    fragmentVisitGalleryHistoryBinding.progressBarVisitGallery.visibility = View.GONE
                }
            }
        }
    }

    // 프래그먼트 교체 메서드
    private fun replaceFragment(bundle: Bundle){
        val supportFragmentManager = parentFragmentManager.beginTransaction()
        val newFragment = ApplyVisitGalleryFragment()
        newFragment.arguments = bundle
        supportFragmentManager.replace(R.id.fragmentContainerViewVisitGallery, newFragment)
            .addToBackStack(VisitGalleryFragmentName.APPLY_VISIT_GALLERY_FRAGMENT.str)
            .commit()
    }

}