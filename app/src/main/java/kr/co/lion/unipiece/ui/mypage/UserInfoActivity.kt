package kr.co.lion.unipiece.ui.mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.transition.MaterialSharedAxis
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivityUserInfoBinding
import kr.co.lion.unipiece.util.UserInfoFragmentName

class UserInfoActivity : AppCompatActivity() {

    lateinit var activityUserInfoBinding: ActivityUserInfoBinding

    // 프래그먼트 객체를 담을 변수
    var oldFragment: Fragment? = null
    var newFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityUserInfoBinding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(activityUserInfoBinding.root)

        // 추후 전달할 데이터는 여기에 담기
        val userInfoBundle = Bundle()

        replaceFragment(UserInfoFragmentName.USER_INFO_FRAGMENT, false, userInfoBundle)
    }

    // 프래그먼트 교체 코드
    fun replaceFragment(name: UserInfoFragmentName, addToBackStack:Boolean, data:Bundle?){
        SystemClock.sleep(200)

        // Fragment를 교체할 수 있는 객체를 추출
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        // oldFragment에 newFragment가 가지고 있는 Fragment 객체를 담아준다.
        if(newFragment != null){
            oldFragment = newFragment
        }

        // 이름으로 분기한다.
        // Fragment의 객체를 생성하여 변수에 담아준다.
        when(name){
            UserInfoFragmentName.USER_INFO_FRAGMENT -> {
                newFragment = UserInfoFragment()
            }
            UserInfoFragmentName.MODIFY_USER_INFO_FRAGMENT -> {
                newFragment = ModifyUserInfoFragment()
            }
        }

        // 새로운 Fragment에 전달할 Bundle 객체가 있다면 arguments 프로퍼티에 넣어준다.
        if(data != null){
            newFragment?.arguments = data
        }

        if(newFragment != null){

            // Fragment를 교체한다.(이전 Fragment가 없으면 새롭게 추가하는 역할을 수행한다)
            fragmentTransaction.replace(R.id.fragmentContainerViewUserInfo, newFragment!!)

            // addToBackStack 변수의 값이 true면 새롭게 보여질 Fragment를 BackStack에 포함시켜 준다.
            if(addToBackStack == true){
                // BackStack에 포함시킬 때 이름을 지정해주면 원하는 Fragment를 BackStack에서 제거할 수 있다.
                fragmentTransaction.addToBackStack(name.str)
            }

            // Fragment 교체를 확정한다.
            fragmentTransaction.commit()
        }
    }

    // BackStack에서 Fragment를 제거한다.
    fun removeFragment(name:UserInfoFragmentName){
        SystemClock.sleep(200)

        // 지정한 이름으로 있는 Fragment를 BackStack에서 제거한다.
        supportFragmentManager.popBackStack(name.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}