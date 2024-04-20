package kr.co.lion.unipiece.ui.payment

import android.os.Bundle
import android.os.SystemClock
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivityDeliveryBinding
import kr.co.lion.unipiece.ui.payment.viewmodel.DeliveryViewModel
import kr.co.lion.unipiece.util.DeliveryFragmentName

class DeliveryActivity : AppCompatActivity() {

    lateinit var activityDeliveryBinding: ActivityDeliveryBinding

    var oldFragment: Fragment? = null
    var newFragment: Fragment? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityDeliveryBinding = ActivityDeliveryBinding.inflate(layoutInflater)
        setContentView(activityDeliveryBinding.root)

        replaceFragment(DeliveryFragmentName.DELIVERY_MANAGER_FRAGMENT, false)

    }

    fun replaceFragment(name: DeliveryFragmentName, addToBackStack: Boolean) {
        SystemClock.sleep(200)
        // Fragment를 교체할 수 있는 객체를 추출한다.
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        // oldFragment에 newFragment가 가지고 있는 Fragment 객체를 담아준다.
        if (newFragment != null) {
            oldFragment = newFragment
        }
        // 이름으로 분기한다.
        when (name) {
            DeliveryFragmentName.DELIVERY_MANAGER_FRAGMENT -> {
                newFragment = DeliveryManagerFragment()
            }

            DeliveryFragmentName.DELIVERY_ADD_FRAGMENT -> {
                newFragment = DeliveryAddFragment()
            }
            DeliveryFragmentName.DELIVERY_UPDATE_FRAGMENT -> {
                newFragment = DeliveryUpdateFragment()
            }

        }
        if (newFragment != null) {
            fragmentTransaction.replace(R.id.containerDelivery, newFragment!!)
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
    fun removeFragment(name: DeliveryManagerFragment) {
        SystemClock.sleep(200)
        // 지정한 이름으로 있는 Fragment를 BackStack에서 제거한다.
        // supportFragmentManager.popBackStack(name.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

}