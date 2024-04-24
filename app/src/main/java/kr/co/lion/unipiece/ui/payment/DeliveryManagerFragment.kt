package kr.co.lion.unipiece.ui.payment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.FragmentDeliveryManagerBinding
import kr.co.lion.unipiece.ui.payment.adapter.DeliveryAdapter
import kr.co.lion.unipiece.ui.payment.viewmodel.DeliveryViewModel
import kr.co.lion.unipiece.util.DeliveryFragmentName

class DeliveryManagerFragment : Fragment() {

    private lateinit var binding: FragmentDeliveryManagerBinding
    private val viewModel: DeliveryViewModel by activityViewModels()
    val userIdx = UniPieceApplication.prefs.getUserIdx("userIdx", 0)

    val deliveryAdapter: DeliveryAdapter = DeliveryAdapter(
        emptyList(),

        // 항목을 클릭 시 (deliveryIdx를 받아옴)
        rowClickListener = { deliveryIdx ->
            Log.d("테스트 rowClickListener deliveryIdx", deliveryIdx.toString())
            viewLifecycleOwner.lifecycleScope.launch {
                // 배송지 관리 화면 내부에서
                val intent = Intent(context, OrderActivity::class.java).apply {

                    // FLAG_ACTIVITY_CLEAR_TOP 및 FLAG_ACTIVITY_SINGLE_TOP 플래그 추가
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

                    // 필요한 데이터를 인텐트에 추가
                    putExtra("deliveryIdx", deliveryIdx)
                }
                startActivity(intent)
            }

        },

        // 항목 수정버튼 클릭 시 (deliveryIdx를 받아옴)
        updateButtonClickListener = { deliveryIdx ->
            Log.d("테스트 updateButtonClickListener deliveryIdx", deliveryIdx.toString())
            viewLifecycleOwner.lifecycleScope.launch {
                val deliveryUpdateFragment = DeliveryUpdateFragment()
                val bundle = Bundle()
                bundle.putInt("deliveryIdx", deliveryIdx)
                deliveryUpdateFragment.arguments = bundle
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.containerDelivery, deliveryUpdateFragment)
                    .addToBackStack(DeliveryFragmentName.DELIVERY_UPDATE_FRAGMENT.str).commit()
            }


        },

        // 항목별 삭제 버튼 클릭 시
        deleteButtonClickListener = { deliveryIdx ->
            Log.d("테스트 deleteButtonClickListener deliveryIdx", deliveryIdx.toString())
            viewLifecycleOwner.lifecycleScope.launch {
                // 삭제 다이얼로그 호출
                val dialog = CustomDeleteDialog("배송지 삭제", "이 배송지를 삭제하시겠습니까?")
                dialog.setButtonClickListener(object :
                    CustomDeleteDialog.OnButtonClickListener {
                    // 확인 버튼 클릭 시
                    override fun okButtonClick() {
                        lifecycleScope.launch {
                            // 삭제 처리
                            viewModel.deleteDeliveryData(deliveryIdx)
                            viewModel.deleteDataLoading.observe(viewLifecycleOwner) {
                                if (it == true) {
                                    viewModel.setDeleteData()
                                    viewModel.getDeliveryDataByUserIdx(userIdx)
                                    dialog.dismiss()
                                }
                            }
                        }
                    }

                    // 취소 버튼 클릭 시
                    override fun noButtonClick() {

                    }

                })
                dialog.show(parentFragmentManager, "deleteDialog")


            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDeliveryManagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initView()
        viewModel.getDeliveryDataByUserIdx(userIdx)
        observeData()
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

            // 신규 배송지 등록 버튼
            with(buttonDeliveryMainNewAdd) {
                // 버튼 클릭 시
                setOnClickListener {
                    val supportFragmentManager = parentFragmentManager.beginTransaction()
                    supportFragmentManager.replace(R.id.containerDelivery, DeliveryAddFragment())
                        .addToBackStack(DeliveryFragmentName.DELIVERY_ADD_FRAGMENT.str)
                        .commit()


                }
            }

            // 리사이클러뷰
            with(recyclerViewDeliveryList) {
                // 리사이클러뷰 어답터


                adapter = deliveryAdapter

                // 리사이클러뷰 레이아웃
                layoutManager = LinearLayoutManager(requireActivity())
            }
        }
    }

    fun observeData() {
        // 데이터 변경 관찰
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userIdxDeliveryDataList.observe(viewLifecycleOwner) { deliveryDataList ->
                deliveryAdapter.updateData(deliveryDataList)
            }
        }
    }
}
