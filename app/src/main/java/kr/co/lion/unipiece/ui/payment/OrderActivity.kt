package kr.co.lion.unipiece.ui.payment

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivityOrderBinding
import kr.co.lion.unipiece.util.OrderFragmentName

class OrderActivity : AppCompatActivity() {

    lateinit var activityOrderBinding: ActivityOrderBinding

    var oldFragment: Fragment? = null
    var newFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityOrderBinding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(activityOrderBinding.root)

        lifecycleScope.launch {
            // 배송지관리에서 배송지 번호를 들고온다. 없다면 0
            val deliveryIdx = intent.getIntExtra("deliveryIdx", 0)
            // 배송지 관리에서 받은 배송지 번호를 주문하기 화면에 전달한다.
            if(deliveryIdx != 0){
                val orderMainFragment = OrderMainFragment()
                val bundle = Bundle()
                bundle.putInt("deliveryIdx",deliveryIdx)
                orderMainFragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.containerOrder,orderMainFragment).commit()
            }else{
                // 다른 접근이라면 null 값을 전달하며 주문하기 화면을 띄운다.
                replaceFragment(OrderFragmentName.ORDER_MAIN_FRAGMENT, false, null)
            }
        }
    }

    // 지정한 Fragment를 보여주는 메서드
    fun replaceFragment(name: OrderFragmentName, addToBackStack: Boolean, data:Bundle?) {
        SystemClock.sleep(200)
        // Fragment를 교체할 수 있는 객체를 추출한다.
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        // oldFragment에 newFragment가 가지고 있는 Fragment 객체를 담아준다.
        if (newFragment != null) {
            oldFragment = newFragment
        }

        // 새로운 Fragment에 전달할 객체가 있다면 arguments 프로퍼티에 넣어준다.
        if(data != null){
            newFragment?.arguments = data
        }

        // 이름으로 분기한다.
        when (name) {
            OrderFragmentName.ORDER_MAIN_FRAGMENT -> {
                newFragment = OrderMainFragment()
            }

            OrderFragmentName.ORDER_SUCCESS_FRAGMENT -> {
                newFragment = OrderSuccessFragment()
            }
        }
        if (newFragment != null) {
            fragmentTransaction.replace(R.id.containerOrder, newFragment!!)
            // addToBackStack 변수의 값이 true면 새롭게 보여질 Fragment를 BackStack에 포함시켜 준다.
            if (addToBackStack == true) {
                // BackStack 포함 시킬때 이름을 지정해주면 원하는 Fragment를 BackStack에서 제거할 수 있다.
                fragmentTransaction.addToBackStack(name.str)
            }
            // Fragment 교체를 확정한다.
            fragmentTransaction.commit()
        }
    }

    // BackStack에서 Fragment를 제거한다.
    fun removeFragment(name: OrderFragmentName) {
        SystemClock.sleep(200)
        // 지정한 이름으로 있는 Fragment를 BackStack에서 제거한다.
        supportFragmentManager.popBackStack(name.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}