package kr.co.lion.unipiece.ui.payment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.DialogDeliveryAddBinding
import kr.co.lion.unipiece.databinding.FragmentDeliveryManagerBinding
import kr.co.lion.unipiece.model.DeliveryData
import kr.co.lion.unipiece.ui.payment.adapter.DeliveryAdapter
import kr.co.lion.unipiece.ui.payment.viewmodel.DeliveryViewModel

class DeliveryManagerFragment : Fragment() {

    private lateinit var binding: FragmentDeliveryManagerBinding
    private lateinit var insertBinding: DialogDeliveryAddBinding
    private val viewModel: DeliveryViewModel by viewModels()
    val userIdx = UniPieceApplication.prefs.getUserIdx("userIdx", 0)

    val deliveryAdapter: DeliveryAdapter by lazy {

        DeliveryAdapter(
            emptyList(),
            itemClickListener = { deliveryIdx ->
                Log.d("테스트 deliveryIdx", deliveryIdx.toString())
                requireActivity().finish()
            }
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDeliveryManagerBinding.inflate(inflater, container, false)
        insertBinding = DialogDeliveryAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initView()

    }


    fun initView() {


        // 바인딩
        with(binding) {


            // 툴바 //////////////////////////////////////////
            with(toolbarDeliveryManager) {
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
            // 신규 배송지 등록 /////////////////////////////////////
            with(buttonDeliveryMainNewAdd) {
                // 버튼 클릭 시
                setOnClickListener {
                    // 커스텀 다이얼로그 (풀스크린)
                    with(CustomFullDialogMaker) {

                        // 다이얼로그 호출
                        getDialog(
                            requireContext(),
                            "신규 배송지 등록",
                            "등록하기",
                            object : CustomFullDialogListener {

                                // 등록하기 버튼 클릭 후 동작
                                override fun onClickSaveButton(deliveryData: DeliveryData) {
                                    viewLifecycleOwner.lifecycleScope.launch {
                                        repeatOnLifecycle(Lifecycle.State.STARTED) {
                                            with(viewModel) {
                                                insertDeliveryData(deliveryData)
                                                deliveryDataList.observe(
                                                    viewLifecycleOwner,
                                                    Observer { value ->
                                                        deliveryAdapter.updateData(value)
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }

                                // 뒤로가기 버튼 클릭 후 동작
                                override fun onClickCancelButton() {
                                    Toast.makeText(
                                        requireActivity(),
                                        "뒤로갔다!",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            },
                            "",
                            "",
                            "()",
                            "",
                            "",
                            false,
                            userIdx,
                            // 신규 배송지 등록에서는 deliveryIdx 값을 0으로 세팅해서 반환한다.
                            0,
                        )
                    }
                }
            }
            // 리사이클러뷰 //////////////////////////////////////////////////////////////////////
            with(recyclerViewDeliveryList) {
                // 리사이클러뷰 어답터
                adapter = deliveryAdapter

                // 리사이클러뷰 레이아웃
                layoutManager = LinearLayoutManager(requireActivity())
            }
            // 리사이클러뷰 변경 시 업데이트
            with(viewLifecycleOwner) {
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.deliveryDataList.observe(viewLifecycleOwner, Observer { value ->
                            deliveryAdapter.updateData(value)
                        })
                    }
                }
            }
        }
    }
}