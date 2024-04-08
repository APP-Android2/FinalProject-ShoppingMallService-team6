package kr.co.lion.unipiece.ui.payment.order


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.addCallback
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentOrderSuccessBinding
import kr.co.lion.unipiece.util.OrderFragmentName

class OrderSuccessFragment : Fragment() {

    lateinit var fragmentOrderSuccessBinding: FragmentOrderSuccessBinding
    lateinit var orderActivity: OrderActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        fragmentOrderSuccessBinding = FragmentOrderSuccessBinding.inflate(layoutInflater)
        orderActivity = activity as OrderActivity

        clickButtonPaymentDetail()
        clickButtonContinueShopping()

        return fragmentOrderSuccessBinding.root
    }

    // 결제 내역 보기 버튼 클릭 시
    fun clickButtonPaymentDetail(){
        fragmentOrderSuccessBinding.apply {
            buttonPaymentDetailSee.apply {
                setOnClickListener {

                }
            }
        }
    }

    // 계속 쇼핑하기 버튼 클릭 시
    fun clickButtonContinueShopping(){
        fragmentOrderSuccessBinding.apply {
            buttonContinueShopping.apply {
                setOnClickListener {

                }
            }
        }
    }
}