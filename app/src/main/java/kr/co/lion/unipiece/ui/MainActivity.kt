package kr.co.lion.unipiece.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
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
        setBuyNaviDrawer()
        initView()
    }

    fun initView() {
        binding.bottomNavigationView.selectedItemId = R.id.fragment_home
    }

    fun setBuyNaviDrawer(){
        with(binding){
            with(navigationDrawer){
                val headerDrawerBinding = HeaderBuyDrawerBinding.inflate(layoutInflater)
                addHeaderView(headerDrawerBinding.root)

                setNavigationItemSelectedListener {
                    SystemClock.sleep(200)

                    when(it.itemId){
                        R.id.menuArtUni -> {
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
                        R.id.menuHumUni -> {
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
                        R.id.menuEngUni -> {
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

    fun bottomNaviClick() {

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

    fun replaceFragment(name: MainFragmentName, addToBackStack:Boolean){

        SystemClock.sleep(200)

        // Fragment를 교체할 수 있는 객체를 추출한다.
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        // 이름으로 분기한다.
        // Fragment의 객체를 생성하여 변수에 담아준다.
        when(name){
            HOME_FRAGMENT -> fragmentTransaction.replace(R.id.fl_container, HomeFragment())
            BUY_FRAGMENT -> fragmentTransaction.replace(R.id.fl_container, BuyFragment())
            RANK_FRAGMENT -> fragmentTransaction.replace(R.id.fl_container, RankFragment())
            MY_GALLERY_FRAGMENT -> fragmentTransaction.replace(R.id.fl_container, MyGalleryFragment())
            MY_PAGE_FRAGMENT -> fragmentTransaction.replace(R.id.fl_container, MyPageFragment())
        }



        // addToBackStack 변수의 값이 true면 새롭게 보여질 Fragment를 BackStack에 포함시켜 준다.
        if(addToBackStack == true){
            // BackStack 포함 시킬때 이름을 지정해주면 원하는 Fragment를 BackStack에서 제거할 수 있다.
            fragmentTransaction.addToBackStack(name.str)
        }
        // Fragment 교체를 확정한다.
        fragmentTransaction.commit()
    }

    fun removeFragment(name: MainFragmentName){
        SystemClock.sleep(200)

        // 지정한 이름으로 있는 Fragment를 BackStack에서 제거한다.
        supportFragmentManager.popBackStack(name.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}