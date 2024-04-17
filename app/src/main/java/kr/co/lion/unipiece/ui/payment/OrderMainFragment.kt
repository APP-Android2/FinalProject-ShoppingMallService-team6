package kr.co.lion.unipiece.ui.payment

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentOrderMainBinding
import kr.co.lion.unipiece.ui.payment.adapter.OrderMainAdapter


class OrderMainFragment : Fragment() {

    lateinit var binding: FragmentOrderMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderMainBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    fun initView() {
        // 바인딩
        with(binding) {

            // 툴바 ////////////////////////////////////
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

            // 배송지 변경 버튼 클릭 /////////////////////////////////////////////////////////
            with(buttonOrderDeliveryChange) {
                setOnClickListener {
                    // DeliveryActivity를 실행한다.
                    val deliveryIntent = Intent(requireActivity(), DeliveryActivity::class.java)
                    startActivity(deliveryIntent)
                }
            }

            // 결제하기 버튼 클릭 //////////////////////////////////////////////
            with(buttonOrderPaymentSubmit) {
                setOnClickListener {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.containerOrder, OrderSuccessFragment())
                        .commit()
                    // 추후 수정
                }
            }

            // 주문 상세 설정 /////////////////////////

            // 추가 배송비 합계
            with(textViewOrderMainAddDeliveryPrice) {
                text = "${"추가 배송비 합계"} 원"
            }

            // 작품 가격 합계
            with(textViewOrderMainPiecePrice) {
                text = "${"작품 가격 합계"} 원"
            }


            // 주문하기 화면 RecyclerView ///////////////////////////////////////////////////////////
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

