package kr.co.lion.unipiece.ui.payment

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.FragmentOrderMainBinding
import kr.co.lion.unipiece.model.DeliveryData
import kr.co.lion.unipiece.ui.payment.adapter.OrderMainAdapter
import kr.co.lion.unipiece.ui.payment.viewmodel.DeliveryViewModel
import kr.co.lion.unipiece.util.setImage
import org.checkerframework.checker.units.qual.K


class OrderMainFragment : Fragment() {

    lateinit var binding: FragmentOrderMainBinding
    private val viewModel : DeliveryViewModel by viewModels()
    val userIdx = UniPieceApplication.prefs.getUserIdx("userIdx",0)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentOrderMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()



    }

    fun initView() {
        // 바인딩
        with(binding) {

            // 툴바
            toolbarOrderMain.apply {
                // 타이틀
                title = "주문하기"
                isTitleCentered = true

                // 뒤로가기 아이콘
                setNavigationIcon(R.drawable.back_icon)
                // 클릭 시 동작
                setNavigationOnClickListener {
                    requireActivity().finish()
                }
            }



            // 처음 들어왔을 때 기본 배송지 세팅
            viewModel.getBasicDeliveryData.observe(viewLifecycleOwner) {
                lifecycleScope.launch {
                    if(it.size!=0){
                        val deliveryName = it[0].deliveryName
                        val deliveryPhone = it[0].deliveryPhone
                        val deliveryAddress = it[0].deliveryAddress
                        val deliveryAddressDetail = it[0].deliveryAddressDetail
                        val basicMemo = it[0].deliveryMemo

                        // 받는 이
                        textViewOrderPersonName.text = deliveryName
                        // 연락처
                        textViewOrderPhone.text = deliveryPhone
                        // 주소 (주소 + 상세주소)
                        textViewOrderAddress.text = "${deliveryAddress} ${deliveryAddressDetail}"

                        // 배송 메모 세팅
                        with(spinnerOrderMainDeliveryMemo){

                            val items = arrayOf(basicMemo, "문 앞", "경비실", "택배함", "선택 안함")
                            val adapter =
                                ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, items)
                            val spinner: Spinner = findViewById(R.id.spinnerOrderMainDeliveryMemo)
                            spinner.adapter = adapter

                        }
                    }
                }
            }

            // 배송지 변경 버튼 클릭
            with(buttonOrderDeliveryChange) {
                setOnClickListener {
                    // DeliveryActivity를 실행한다.
                    val deliveryIntent = Intent(requireActivity(), DeliveryActivity::class.java)
                    startActivity(deliveryIntent)
                }
            }


            // 결제하기 버튼 클릭
            with(buttonOrderPaymentSubmit) {
                setOnClickListener {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.containerOrder, OrderSuccessFragment())
                        .commit()
                    // 추후 수정
                }
            }

            // 주문 상세 설정

            // 추가 배송비 합계
            with(textViewOrderMainAddDeliveryPrice) {
                text = "${"추가 배송비 합계"} 원"
            }

            // 작품 가격 합계
            with(textViewOrderMainPiecePrice) {
                text = "${"작품 가격 합계"} 원"
            }


            // 주문하기 화면 RecyclerView
            with(recyclerViewOrderList) {


                // 어뎁터
                adapter = OrderMainAdapter()
                // 레이아웃 매니저
                layoutManager = LinearLayoutManager(requireActivity())

                // 마지막 리사이클러뷰 항목의 디바이더 삭제
                addItemDecoration(
                    MaterialDividerItemDecoration(
                        requireActivity(),
                        (layoutManager as LinearLayoutManager).orientation
                    ).apply {
                        isLastItemDecorated = false
                        setDividerColorResource(requireActivity(), R.color.lightgray)
                        dividerInsetEnd = 16.dp
                        dividerInsetStart = 16.dp
                    }
                )
            }
        }
    }

    // dp값으로 변환하는 확장함수 ex) 16dp로 적용시 16.dp 로 작성 //////////////////////////////////
    inline val Int.dp: Int
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics
        ).toInt()

    inline val Float.dp: Float
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics
        )
}

