package kr.co.lion.unipiece.ui.payment

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
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

        lifecycleScope.launch {
            // 인텐트에서 "deliveryIdx"로 넘어온 값을 가져옴. 값이 없으면 기본값으로 0을 사용
            val deliveryIdx = intent.getIntExtra("deliveryIdx", 0)
            // 인텐트에서 "pieceIdxList"로 넘어온 ArrayList<Integer> 타입의 값을 가져옴. 값이 없으면 null을 반환
            val pieceIdxList = intent.getIntegerArrayListExtra("pieceIdxList")
            // 인텐트에서 "pieceIdx"로 넘어온 Int 타입의 값을 가져옴. 값이 없으면 0을 반환
            val pieceIdx = intent.getIntExtra("pieceIdx",0)
            Log.d("pieceIdx orderActivity",pieceIdx.toString())
            val bundle = Bundle()

            // 장바구니에서 선택된 상품 목록이 있다면 Bundle에 이 목록을 추가
            pieceIdxList?.let {
                bundle.putIntegerArrayList("pieceIdxList", it)
            }

            // 작품 상세에서 넘어온 작품번호가 있다면 Bundle에 이 값을 추가
            if (pieceIdx != 0) {
                bundle.putInt("pieceIdx", pieceIdx)
                Log.d("pieceIdx bundle",bundle.toString())
            }

            // pieceIdxList 또는 pieceIdx 중 하나라도 존재한다면
            if (!bundle.isEmpty) {
                // OrderMainFragment를 생성하고, Bundle을 이 Fragment의 arguments로 설정
                val deliveryManagerFragment = DeliveryManagerFragment().apply {
                    arguments = bundle
                }
                // FragmentManager를 사용하여 containerOrder 레이아웃에 orderMainFragment 표시
                supportFragmentManager.beginTransaction().replace(R.id.containerDelivery, deliveryManagerFragment).commit()
            } else {
                // Bundle이 비어있다면, 즉 전달할 데이터가 없다면 기본 화면을 표시하는 로직 실행
                replaceFragment(DeliveryFragmentName.DELIVERY_MANAGER_FRAGMENT, false, null)
            }
        }
    }

    fun replaceFragment(name: DeliveryFragmentName, addToBackStack: Boolean, data:Bundle?) {
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

        // 새로운 Fragment에 전달할 객체가 있다면 arguments 프로퍼티에 넣어준다.
        if(data != null){
            newFragment?.arguments = data
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