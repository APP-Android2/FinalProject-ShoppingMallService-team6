package kr.co.lion.unipiece.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Gravity
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivityMainBinding
import kr.co.lion.unipiece.databinding.HeaderBuyDrawerBinding
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

    lateinit var buyFragment: BuyFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNaviClick()
        initView()

        buyFragment = BuyFragment()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // 새로운 Intent를 현재 Intent로 설정
        setIntent(intent)

        // 새로운 intent로 새로운 fragment들로 화면 설정
        openSearchFragment()
        openHomeFragment()
    }


    fun printFragmentBackStack(name: String) {
        val fragmentManager = supportFragmentManager
        val count = fragmentManager.backStackEntryCount
        Log.d("$name BackStack", "백 스택에 있는 프래그먼트 수: $count")
        for (index in 0 until count) {
            val backStackEntry = fragmentManager.getBackStackEntryAt(index)
            Log.d("$name BackStack", "백 스택 #$index: ${backStackEntry.name}")
        }
    }


    fun initView() {
        updateBottomNavi()
        replaceFragment(HOME_FRAGMENT, true)
    }

    override fun onBackPressed() {
            super.onBackPressed()
            updateBottomNavi()

            var isSearchFragmentOnTop = false

            if (supportFragmentManager.backStackEntryCount > 0) {
                // 백 스택의 마지막 항목의 이름을 가져옵니다.
                val lastFragmentName = supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1).name
                // 마지막 항목의 이름이 "SearchFragment"와 일치하는지 확인합니다.
                isSearchFragmentOnTop = SEARCH_FRAGMENT.str == lastFragmentName || "SearchResultFragment" == lastFragmentName

            } else {
                // 백 스택이 비어있으면, SearchFragment가 최상단에 있을 수 없습니다.
                isSearchFragmentOnTop = false
            }

            if (isSearchFragmentOnTop) {
                // SearchFragment가 백 스택의 최상단에 존재합니다.
                // 안드로이드 뒤로가기 버튼 실행 -> searchresultfragment에서 뒤로갔을때 searchfragment로 가지 않고 그 전으로 가기
                super.onBackPressed()
            }

            // Fragment BackStack에 아무것도 남아있지 않을 때 activity 종료
            if(supportFragmentManager.backStackEntryCount == 0) {
                finish()
            }

        }


    fun updateBottomNavi(){
        // printFragmentBackStack("update")
        val fragment = supportFragmentManager.findFragmentById(R.id.fl_container)
        when(fragment) {
            is HomeFragment -> {
                binding.bottomNavigationView.selectedItemId = R.id.fragment_home
                printFragmentBackStack("update home")
            }
            is BuyFragment -> {
                binding.bottomNavigationView.selectedItemId = R.id.fragment_buy
                printFragmentBackStack("update buy")
            }
            is RankFragment -> {
                binding.bottomNavigationView.selectedItemId = R.id.fragment_rank
                printFragmentBackStack("update rank")
            }
            is MyGalleryFragment -> {
                binding.bottomNavigationView.selectedItemId = R.id.fragment_mygallery
                printFragmentBackStack("update gallery")
            }
            is MyPageFragment ->{
                binding.bottomNavigationView.selectedItemId = R.id.fragment_mypage
                printFragmentBackStack("update mypage")
            }
        }
    }
    fun bottomNaviClick() {
        // printFragmentBackStack("navi")
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.fragment_home -> {
                    replaceFragment(HOME_FRAGMENT, true)
                    printFragmentBackStack("navi home")
                    true
                }
                R.id.fragment_buy -> {
                    replaceFragment(BUY_FRAGMENT, true)
                    printFragmentBackStack("navi buy")
                    true
                }
                R.id.fragment_rank -> {
                    replaceFragment(RANK_FRAGMENT, true)
                    printFragmentBackStack("navi rank")
                    true
                }
                R.id.fragment_mygallery -> {
                    replaceFragment(MY_GALLERY_FRAGMENT, true)
                    printFragmentBackStack("navi gallery")
                    true
                }
                R.id.fragment_mypage -> {
                    replaceFragment(MY_PAGE_FRAGMENT, true)
                    printFragmentBackStack("navi mypage")
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

    fun openSearchFragment() {

        val isSearchFragment = intent.getBooleanExtra(SEARCH_FRAGMENT.str, false)
        // SearchFragment가 true인 경우 프래그먼트 변경 로직을 실행합니다.
        if (isSearchFragment) {
            replaceFragment(SEARCH_FRAGMENT, true)
        }
    }

    fun openHomeFragment() {
        val isHomeFragment = intent.getBooleanExtra(HOME_FRAGMENT.str, false)
        // HomeFragment가 true인 경우 프래그먼트 변경 로직을 실행합니다.
        if (isHomeFragment) {
            // backStack을 비워준다.
            supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

            // MainActivity가 이미 실행 중인 경우 해당 인스턴스를 재사용합니다.
            binding.bottomNavigationView.selectedItemId = R.id.fragment_home
        }
    }
}
