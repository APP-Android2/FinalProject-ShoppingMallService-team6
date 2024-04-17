package kr.co.lion.unipiece.ui.payment

import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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

        replaceFragment(OrderFragmentName.ORDER_MAIN_FRAGMENT, false)
    }

    // 지정한 Fragment를 보여주는 메서드
    fun replaceFragment(name: OrderFragmentName, addToBackStack: Boolean) {
        SystemClock.sleep(200)
        // Fragment를 교체할 수 있는 객체를 추출한다.
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        // oldFragment에 newFragment가 가지고 있는 Fragment 객체를 담아준다.
        if (newFragment != null) {
            oldFragment = newFragment
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