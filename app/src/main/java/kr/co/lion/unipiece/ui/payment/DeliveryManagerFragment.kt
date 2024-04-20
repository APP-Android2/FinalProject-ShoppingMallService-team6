package kr.co.lion.unipiece.ui.payment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.FragmentDeliveryManagerBinding
import kr.co.lion.unipiece.model.DeliveryData
import kr.co.lion.unipiece.ui.payment.adapter.DeliveryAdapter
import kr.co.lion.unipiece.ui.payment.viewmodel.DeliveryViewModel
import kr.co.lion.unipiece.util.DeliveryFragmentName

class DeliveryManagerFragment : Fragment() {

    private lateinit var binding: FragmentDeliveryManagerBinding
    private val viewModel: DeliveryViewModel by activityViewModels()
    val userIdx = UniPieceApplication.prefs.getUserIdx("userIdx",0)

    val deliveryAdapter: DeliveryAdapter = DeliveryAdapter(
        emptyList(),
        itemClickListener = { deliveryIdx ->
            Log.d("테스트 deliveryIdx", deliveryIdx.toString())
            requireActivity().finish()
        },
        updateButtonClickListener = { deliveryData ->
            Log.d("테스트 deliveryData", deliveryData.toString())
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.containerDelivery,DeliveryUpdateFragment()).addToBackStack(DeliveryFragmentName.DELIVERY_UPDATE_FRAGMENT.str).commit()
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
        viewModel.getDeliveryDataByIdx(userIdx)
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
            viewModel.deliveryDataList.observe(viewLifecycleOwner) { deliveryDataList ->
                deliveryAdapter.updateData(deliveryDataList)
            }
        }
    }
}
