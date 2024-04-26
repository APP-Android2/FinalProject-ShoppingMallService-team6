package kr.co.lion.unipiece.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivityMainBinding
import kr.co.lion.unipiece.ui.buy.BuyFragment
import kr.co.lion.unipiece.ui.home.HomeFragment
import kr.co.lion.unipiece.ui.mygallery.MyGalleryFragment
import kr.co.lion.unipiece.ui.mypage.MyPageFragment
import kr.co.lion.unipiece.ui.rank.RankFragment
import kr.co.lion.unipiece.ui.search.SearchFragment
import kr.co.lion.unipiece.util.MainFragmentName
import kr.co.lion.unipiece.util.MainFragmentName.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var backPressedTime: Long = 0
    private var backSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNaviClick()
        initView()

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // 새로운 Intent를 현재 Intent로 설정
        setIntent(intent)

        // 새로운 intent로 새로운 fragment들로 화면 설정
        openFragment()
    }

    fun initView() {
        updateBottomNavi()
        replaceFragment(HOME_FRAGMENT, true)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

        // fragment가 1개 남아있을때
        if (supportFragmentManager.backStackEntryCount == 1) {
            // 2초 안에 두번 누르면 뒤로가기
            if (System.currentTimeMillis() - backPressedTime >=2000) {
                backPressedTime = System.currentTimeMillis()
                backSnackbar = showSnackbar("뒤로가기를 한 번 더 누르면 종료됩니다.")
            } else {
                backSnackbar?.dismiss() // 스낵바 메시지를 제거
                finish()
            }
        }
        // fragment 여러개 남아있을때
        else {
            super.onBackPressed()
            updateBottomNavi()
        }
    }


    private fun showSnackbar(message: String): Snackbar {
        return Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setAnchorView(binding.bottomNavigationView)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.second))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .also { it.show() }
    }


    fun updateBottomNavi(){
        val fragment = supportFragmentManager.findFragmentById(R.id.fl_container)
        when(fragment) {
            is HomeFragment -> {
                binding.bottomNavigationView.selectedItemId = R.id.fragment_home
            }
            is BuyFragment -> {
                binding.bottomNavigationView.selectedItemId = R.id.fragment_buy
            }
            is RankFragment -> {
                binding.bottomNavigationView.selectedItemId = R.id.fragment_rank
            }
            is MyGalleryFragment -> {
                binding.bottomNavigationView.selectedItemId = R.id.fragment_mygallery
            }
            is MyPageFragment ->{
                binding.bottomNavigationView.selectedItemId = R.id.fragment_mypage
            }
        }
    }
    fun bottomNaviClick() {
        // printFragmentBackStack("navi")
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.fragment_home -> {
                    replaceFragment(HOME_FRAGMENT, true)
                    true
                }
                R.id.fragment_buy -> {
                    replaceFragment(BUY_FRAGMENT, true)
                    true
                }
                R.id.fragment_rank -> {
                    replaceFragment(RANK_FRAGMENT, true)
                    true
                }
                R.id.fragment_mygallery -> {
                    replaceFragment(MY_GALLERY_FRAGMENT, true)
                    true
                }
                R.id.fragment_mypage -> {
                    replaceFragment(MY_PAGE_FRAGMENT, true)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    fun replaceFragment(name: MainFragmentName, addToBackStack:Boolean) {

        SystemClock.sleep(200)

        // Fragment를 교체할 수 있는 객체를 추출한다.
        val fragmentManager = supportFragmentManager.beginTransaction()

        // 이름으로 분기한다.
        // Fragment의 객체를 생성하여 변수에 담아준다.
        when(name){
            HOME_FRAGMENT -> fragmentManager.replace(R.id.fl_container, HomeFragment())
            BUY_FRAGMENT -> fragmentManager.replace(R.id.fl_container, BuyFragment())
            RANK_FRAGMENT -> fragmentManager.replace(R.id.fl_container, RankFragment())
            MY_GALLERY_FRAGMENT -> fragmentManager.replace(R.id.fl_container, MyGalleryFragment())
            MY_PAGE_FRAGMENT -> fragmentManager.replace(R.id.fl_container, MyPageFragment())
            SEARCH_FRAGMENT -> fragmentManager.replace(R.id.fl_container, SearchFragment())
        }

        // addToBackStack 변수의 값이 true면 새롭게 보여질 Fragment를 BackStack에 포함시켜 준다.
        if(addToBackStack){
            // BackStack에 Fragment가 있을 경우에 실행한다.
            if(supportFragmentManager.backStackEntryCount > 0){
                // BackStack 최상단에 있는 fragment 이름을 가져온다.
                val lastFragmentName = supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1).name

                // BackStack 최상위 Fragment 값이 지정된 Fragment와 다를 경우에 새롭게 보여질 Fragment를 BackStack에 포함시켜 준다.
                if(lastFragmentName != name.str){
                    // BackStack 포함 시킬때 이름을 지정해주면 원하는 Fragment를 BackStack에서 제거할 수 있다.
                    fragmentManager.addToBackStack(name.str)
                }
            } else {
                // BackStack 포함 시킬때 이름을 지정해주면 원하는 Fragment를 BackStack에서 제거할 수 있다.
                fragmentManager.addToBackStack(name.str)
                }
            }
        
        // Fragment 교체를 확정한다.
        fragmentManager.commit()
    }

    fun removeFragment(name: MainFragmentName){
        SystemClock.sleep(200)

        // 지정한 이름으로 있는 Fragment를 BackStack에서 제거한다.
        supportFragmentManager.popBackStack(name.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun openFragment() {
        val fragmentId: Int? = when {
            intent.getBooleanExtra(MY_GALLERY_FRAGMENT.str, false) -> R.id.fragment_mygallery
            intent.getBooleanExtra(SEARCH_FRAGMENT.str, false) -> null // SearchFragment는 백스택 처리를 하지 않음
            intent.getBooleanExtra(BUY_FRAGMENT.str, false) -> R.id.fragment_buy
            intent.getBooleanExtra(HOME_FRAGMENT.str, false) -> R.id.fragment_home
            else -> null
        }

        // 백스택을 비워야 하는 경우 (SearchFragment가 아닐 때)
        if (fragmentId != null) {
            supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        // SearchFragment 처리
        if (fragmentId == null && intent.getBooleanExtra(SEARCH_FRAGMENT.str, false)) {
            replaceFragment(SEARCH_FRAGMENT, true)
        } else if (fragmentId != null) {
            // 다른 프래그먼트들 처리
            binding.bottomNavigationView.selectedItemId = fragmentId
        }
    }
}
