package kr.co.lion.unipiece.ui.author

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.FragmentManager
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivityAddAythorBinding
import kr.co.lion.unipiece.util.AddAuthorFragmentName

class AddAuthorActivity : AppCompatActivity() {

    lateinit var activityAddAuthorBinding: ActivityAddAythorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAddAuthorBinding = ActivityAddAythorBinding.inflate(layoutInflater)
        setContentView(activityAddAuthorBinding.root)
        replaceFragment(AddAuthorFragmentName.GUIDE_LINE_FRAGMENT, false)
    }
    fun replaceFragment(name: AddAuthorFragmentName, addToBackStack:Boolean){

        SystemClock.sleep(200)

        // Fragment를 교체할 수 있는 객체를 추출한다.
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        // 이름으로 분기한다.
        // Fragment의 객체를 생성하여 변수에 담아준다.
        when(name) {
            AddAuthorFragmentName.GUIDE_LINE_FRAGMENT -> fragmentTransaction.replace(R.id.containerAuthor, GuideLineFragment())
            AddAuthorFragmentName.REGISTER_AUTHOR_FRAGMENT -> fragmentTransaction.replace(R.id.containerAuthor, RegisterAuthorFragment())
        }



        // addToBackStack 변수의 값이 true면 새롭게 보여질 Fragment를 BackStack에 포함시켜 준다.
        if(addToBackStack == true){
            // BackStack 포함 시킬때 이름을 지정해주면 원하는 Fragment를 BackStack에서 제거할 수 있다.
            fragmentTransaction.addToBackStack(name.str)
        }
        // Fragment 교체를 확정한다.
        fragmentTransaction.commit()
    }

    fun removeFragment(name: AddAuthorFragmentName){
        SystemClock.sleep(200)

        // 지정한 이름으로 있는 Fragment를 BackStack에서 제거한다.
        supportFragmentManager.popBackStack(name.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

}