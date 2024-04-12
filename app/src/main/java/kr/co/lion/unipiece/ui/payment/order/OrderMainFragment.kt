package kr.co.lion.unipiece.ui.payment.order

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentOrderMainBinding
import kr.co.lion.unipiece.databinding.RowCartBinding
import kr.co.lion.unipiece.databinding.RowOrderMainBinding
import kr.co.lion.unipiece.ui.payment.cart.CartActivity
import kr.co.lion.unipiece.ui.payment.delivery.DeliveryActivity

import kr.co.lion.unipiece.util.OrderFragmentName

import kr.co.lion.unipiece.ui.payment.order.OrderActivity



class OrderMainFragment : Fragment() {

    lateinit var fragmentOrderMainBinding: FragmentOrderMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentOrderMainBinding = FragmentOrderMainBinding.inflate(layoutInflater)
        setToolbar()
        clickButtonDeliveryChange()
        setRecyclerViewOrderMain()

        clickButtonPayment()

        return fragmentOrderMainBinding.root
    }

    // 툴바 설정
    fun setToolbar(){
        fragmentOrderMainBinding.apply {
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
        }
    }

    // 배송지 변경 버튼 클릭
    fun clickButtonDeliveryChange(){
        fragmentOrderMainBinding.apply {
            buttonOrderDeliveryChange.setOnClickListener {
                // DeliveryActivity를 실행한다.
                val deliveryIntent = Intent(requireActivity(), DeliveryActivity::class.java)
                startActivity(deliveryIntent)
            }
        }
    }

    // 결제하기 버튼 클릭
    fun clickButtonPayment(){
        fragmentOrderMainBinding.apply {
            buttonOrderPaymentSubmit.apply {
                setOnClickListener {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.containerOrder, OrderSuccessFragment())
                        .commit()
                    // 추후 수정
                }
            }
        }
    }

    ///////////////////////////////// 리사이클러뷰 ///////////////////////////////////////
    // 주문하기 화면의 RecyclerView 설정
    fun setRecyclerViewOrderMain(){
        fragmentOrderMainBinding.apply {
            recyclerViewOrderList.apply {
                // 어뎁터
                adapter = OrderMainRecyclerViewAdapter()
                // 레이아웃 매니저
                layoutManager = LinearLayoutManager(requireActivity())

                // 마지막 리사이클러뷰 항목의 디바이더 삭제
                addItemDecoration(
                    MaterialDividerItemDecoration(requireActivity(),
                        (layoutManager as LinearLayoutManager).orientation
                    ).apply {
                        isLastItemDecorated = false
                        setDividerColorResource(requireActivity(),R.color.lightgray)
                        dividerInsetEnd = 16.dp
                        dividerInsetStart = 16.dp
                    }

                )
            }
        }
    }

    // 주문하기 화면의 RecyclerView의 어뎁터
    inner class OrderMainRecyclerViewAdapter :
        RecyclerView.Adapter<OrderMainRecyclerViewAdapter.OrderMainViewHolder>() {
        inner class OrderMainViewHolder(rowOrderMainBinding: RowOrderMainBinding) :
            RecyclerView.ViewHolder(rowOrderMainBinding.root) {
            val rowOrderMainBinding: RowOrderMainBinding

            init {
                this.rowOrderMainBinding = rowOrderMainBinding
                this.rowOrderMainBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderMainViewHolder {
            val rowOrderMainBinding = RowOrderMainBinding.inflate(layoutInflater)
            val orderMainViewHolder = OrderMainViewHolder(rowOrderMainBinding)
            return orderMainViewHolder
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: OrderMainViewHolder, position: Int) {
            holder.rowOrderMainBinding.textViewRowOrderMain.text = "테스트라고"
        }
    }
}

// dp값으로 변환하는 확장함수
inline val Int.dp: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics
    ).toInt()

inline val Float.dp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics
    )