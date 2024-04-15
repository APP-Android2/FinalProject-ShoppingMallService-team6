package kr.co.lion.unipiece.ui.payment.delivery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentDeliveryManagerBinding
import kr.co.lion.unipiece.databinding.RowDeliveryBinding
import kr.co.lion.unipiece.ui.payment.adapter.DeliveryAdapter
import kr.co.lion.unipiece.util.CustomDialog

class DeliveryManagerFragment : Fragment() {

    private lateinit var binding: FragmentDeliveryManagerBinding
    private val deliveryViewModel: DeliveryViewModel by viewModels()

    val deliveryIdx by lazy {
        requireArguments().getInt("deliveryIdx")
    }
    val userIdx by lazy {
        requireArguments().getInt("userIdx")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDeliveryManagerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        clickButtonDeliveryNewAdd()
        setRecyclerViewDelivery()

        lifecycleScope.launch(Dispatchers.Main) {
            fetchData()
            // 여기서 fetchData 후 UI 업데이트가 필요한 경우 추가 작업을 수행하세요.
        }
    }

    ///////////////////////////////////////////기능 구현/////////////////////////////////////////////
    private suspend fun fetchData() {
        try {
            // 배송지 정보 불러오기
            deliveryViewModel.getDeliveryData(userIdx)
            // 필요한 경우 데이터 로딩 상태를 관리하거나, 성공/실패에 따른 UI 업데이트를 수행하세요.
        } catch (e: Exception) {
            // 오류 처리 (예: Toast 메시지 표시, 로깅 등)
            Toast.makeText(requireContext(), "데이터를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }



    // 툴바 셋팅
    fun setToolbar() {
        binding.apply {
            toolbarDeliveryManager.apply {

                // 타이틀
                setTitle("배송지 관리")
                isTitleCentered = true

                // 뒤로가기 버튼 아이콘
                setNavigationIcon(R.drawable.back_icon)

                // 뒤로가기 버튼 클릭 시
                setNavigationOnClickListener {
                    requireActivity().finish()
                }
            }
        }
    }

    // 신규 배송지 등록 버튼 클릭 시
    fun clickButtonDeliveryNewAdd() {
        binding.apply {
            buttonDeliveryMainNewAdd.apply {
                setOnClickListener {

                    // 커스텀 다이얼로그 (풀스크린)
                    CustomFullDialogMaker.apply {

                        // 다이얼로그 호출
                        getDialog(
                            requireActivity(),
                            "신규 배송지 등록",
                            "저장하기",
                            object : CustomFullDialogListener {

                                // 클릭한 이후 동작

                                // 저장하기 버튼 클릭 후 동작
                                override fun onClickSaveButton() {
                                    Toast.makeText(requireActivity(), "저장했다!", Toast.LENGTH_SHORT)
                                        .show()
                                }

                                // 뒤로가기 버튼 클릭 후 동작
                                override fun onClickCancelButton() {
                                    Toast.makeText(requireActivity(), "뒤로갔다!", Toast.LENGTH_SHORT)
                                        .show()


                                }
                            }
                        )
                    }
                }
            }
        }
    }


    /////////////////////////////////////////// 리사이클러뷰 ////////////////////////////////////////
    // 배송지 화면의 RecyclerView 설정
    fun setRecyclerViewDelivery() {
        binding.apply {
            recyclerViewDeliveryList.apply {
                // 어뎁터
                adapter = DeliveryAdapter()
                // 레이아웃 매니저
                layoutManager = LinearLayoutManager(requireActivity())

            }
        }
    }
}