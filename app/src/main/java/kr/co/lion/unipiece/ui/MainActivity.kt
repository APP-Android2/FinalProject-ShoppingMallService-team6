package kr.co.lion.unipiece.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivityMainBinding
import kr.co.lion.unipiece.databinding.HeaderBuyDrawerBinding
import kr.co.lion.unipiece.ui.buy.BuyFragment
import kr.co.lion.unipiece.ui.home.HomeFragment
import kr.co.lion.unipiece.ui.mygallery.MyGalleryFragment
import kr.co.lion.unipiece.ui.mypage.MyPageFragment
import kr.co.lion.unipiece.ui.rank.RankFragment
import kr.co.lion.unipiece.util.MainFragmentName
import kr.co.lion.unipiece.util.MainFragmentName.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNaviClick()
        initView()
        setBuyNaviDrawer()
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
        replaceFragment(HOME_FRAGMENT, false)
    }

    fun setBuyNaviDrawer(){
        with(binding){
            with(navigationDrawer){
                val headerDrawerBinding = HeaderBuyDrawerBinding.inflate(layoutInflater)
                addHeaderView(headerDrawerBinding.root)

                headerDrawerBinding.backBtn.setOnClickListener {
                    drawerBuyLayout.close()
                }

                setNavigationItemSelectedListener {
                    SystemClock.sleep(200)

                    when(it.itemId){
                        R.id.menuAll -> {
                            drawerBuyLayout.close()
                        }
                        R.id.menuArtAll -> {
                            drawerBuyLayout.close()
                        }
                        R.id.menuArtWest -> {
                            drawerBuyLayout.close()
                        }
                        R.id.menuArtOri -> {
                            drawerBuyLayout.close()
                        }
                        R.id.menuArtCalli -> {
                            drawerBuyLayout.close()
                        }
                        R.id.menuArtSculp -> {
                            drawerBuyLayout.close()
                        }
                        R.id.menuArtPrint -> {
                            drawerBuyLayout.close()
                        }
                        R.id.menuArtWood -> {
                            drawerBuyLayout.close()
                        }
                        R.id.menuArtGlass -> {
                            drawerBuyLayout.close()
                        }
                        R.id.menuArtFabric -> {
                            drawerBuyLayout.close()
                        }
                        R.id.menuArtMetal -> {
                            drawerBuyLayout.close()
                        }
                        R.id.menuArtComic -> {
                            drawerBuyLayout.close()
                        }
                        R.id.menuArtAni -> {
                            drawerBuyLayout.close()
                        }
                        R.id.menuHumAll -> {
                            drawerBuyLayout.close()
                        }
                        R.id.menuHumFiction -> {
                            drawerBuyLayout.close()
                        }
                        R.id.menuHumPoem -> {
                            drawerBuyLayout.close()
                        }
                        R.id.menuHumScript -> {
                            drawerBuyLayout.close()
                        }
                        R.id.menuEngAll -> {
                            drawerBuyLayout.close()
                        }
                        R.id.menuEngSoft -> {
                            drawerBuyLayout.close()
                        }
                        R.id.menuEngHard -> {
                            drawerBuyLayout.close()
                        }
                    }

                    true
                }
            }
        }
    }

    override fun onBackPressed() {
        with(binding){
            if(drawerBuyLayout.isDrawerOpen(GravityCompat.START)){
                drawerBuyLayout.close()
            } else {
                // 안드로이드 뒤로가기 버튼 실행
                super.onBackPressed()

                // Fragment BackStack에 아무것도 남아있지 않을 때 activity 종료
                if(supportFragmentManager.backStackEntryCount == 0) {
                    finish()
                }
                updateBottomNavi()
                printFragmentBackStack("back")
            }
        }
    }

    fun updateBottomNavi(){
        printFragmentBackStack("update")
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
        printFragmentBackStack("navi")
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
                    fragmentTransaction.addToBackStack(name.str)
                }
            } else {
                // BackStack 포함 시킬때 이름을 지정해주면 원하는 Fragment를 BackStack에서 제거할 수 있다.
                fragmentTransaction.addToBackStack(name.str)
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
}