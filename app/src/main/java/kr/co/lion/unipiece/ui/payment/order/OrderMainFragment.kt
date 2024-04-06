package kr.co.lion.unipiece.ui.payment.order

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentOrderMainBinding
import kr.co.lion.unipiece.databinding.RowCartBinding
import kr.co.lion.unipiece.ui.payment.cart.CartActivity

import kr.co.lion.unipiece.util.OrderFragmentName

import kr.co.lion.unipiece.ui.payment.order.OrderActivity



class OrderMainFragment : Fragment() {

    lateinit var fragmentOrderMainBinding: FragmentOrderMainBinding
    lateinit var orderActivity: OrderActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentOrderMainBinding = FragmentOrderMainBinding.inflate(layoutInflater)
        orderActivity = activity as OrderActivity
        setToolbar()
        clickButtonDeliveryChange()
        setRecyclerViewCart()

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
                    val cartIntent = Intent(orderActivity, CartActivity::class.java)
                    startActivity(cartIntent)
                    orderActivity.finish()
                }
            }
        }
    }

    // 배송지 변경 버튼 클릭
    fun clickButtonDeliveryChange(){
        fragmentOrderMainBinding.apply {
            buttonOrderDeliveryChange.setOnClickListener {
//                // DeliveryActivity를 실행한다.
//                val deliveryIntent = Intent(orderActivity, DeliveryActivity::class.java)
//                startActivity(deliveryIntent)
//                // OrderActivity를 종료한다.
//                orderActivity.finish()
            }
        }
    }

    // 결제하기 버튼 클릭
    fun clickButtonPayment(){
        fragmentOrderMainBinding.apply {
            buttonOrderPaymentSubmit.apply {
                setOnClickListener {
                    orderActivity.replaceFragment(OrderFragmentName.ORDER_SUCCESS_FRAGMENT,true,true,null)
                }
            }
        }
    }

    ///////////////////////////////// 리사이클러뷰 ///////////////////////////////////////
    // 주문하기 화면의 RecyclerView 설정
    fun setRecyclerViewCart(){
        fragmentOrderMainBinding.apply {
            recyclerViewOrderList.apply {
                // 어뎁터
                adapter = OrderRecyclerViewAdapter()
                // 레이아웃 매니저
                layoutManager = LinearLayoutManager(orderActivity)

            }
        }
    }

    // 주문하기 화면의 RecyclerView의 어뎁터
    inner class OrderRecyclerViewAdapter :
        RecyclerView.Adapter<OrderRecyclerViewAdapter.OrderViewHolder>() {
        inner class OrderViewHolder(rowCartBinding: RowCartBinding) :
            RecyclerView.ViewHolder(rowCartBinding.root) {
            val rowCartBinding: RowCartBinding

            init {
                this.rowCartBinding = rowCartBinding
                this.rowCartBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
            val rowCartBinding = RowCartBinding.inflate(layoutInflater)
            val orderViewHolder = OrderViewHolder(rowCartBinding)
            return orderViewHolder
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
            holder.rowCartBinding.textViewRowCart.text = "이거슨 테스트"
        }
    }
}