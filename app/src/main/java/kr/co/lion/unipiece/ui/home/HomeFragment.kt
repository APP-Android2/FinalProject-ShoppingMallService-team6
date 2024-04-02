package kr.co.lion.unipiece.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.viewpager2.widget.ViewPager2
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentHomeBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.infomation.InfoAllActivity
import kr.co.lion.unipiece.ui.login.LoginActivity
import java.util.Timer
import java.util.TimerTask

class HomeFragment : Fragment() {

    lateinit var fragmentHomeBinding: FragmentHomeBinding

    lateinit var mainActivity: MainActivity



    val timer = Timer()
    val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        settingToolBar()
        settingEvent()
        connectAdapterPromote()
        connectAdapterNews()
        connectAdapterGallery()

        return fragmentHomeBinding.root
    }

    //툴바 설정
    private fun settingToolBar(){
        fragmentHomeBinding.apply {
            toolBarHome.apply {

                setNavigationIcon(R.drawable.logo_toolbar)

                inflateMenu(R.menu.menu_search_cart)
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_search -> {

                        }

                        R.id.menu_cart -> {

                        }
                    }


                    true
                }
            }
        }
    }

    //이벤트 설정
    private fun settingEvent(){
        fragmentHomeBinding.apply {
            buttonAllPromote.setOnClickListener {
                startActivity(Intent(mainActivity, InfoAllActivity::class.java))
            }

            buttonAllNews.setOnClickListener {
                startActivity(Intent(mainActivity, InfoAllActivity::class.java))
            }

            buttonAllGallery.setOnClickListener {
                startActivity(Intent(mainActivity, InfoAllActivity::class.java))
            }

            buttonHomeVisitGallery.setOnClickListener {

            }
        }

        //backButton을 눌렀을 때
        mainActivity.onBackPressedDispatcher.addCallback {
            mainActivity.finish()
        }
    }

    //전시홍보 ViewPager 연결
    private fun connectAdapterPromote(){
        fragmentHomeBinding.apply {
            val bannerVPAdapter = BannerVPAdapter(this@HomeFragment)
            bannerVPAdapter.addFragment(PromoteFragment(R.drawable.logo_toolbar))
            bannerVPAdapter.addFragment(PromoteFragment(R.drawable.icon))
            viewPagerHomePromote.adapter = bannerVPAdapter
            viewPagerHomePromote.orientation = ViewPager2.ORIENTATION_HORIZONTAL

            autoSlidePromote(bannerVPAdapter)

        }
    }

    //소식 ViewPager 연결
    private fun connectAdapterNews(){
        fragmentHomeBinding.apply {
            val newsVPAdapter = BannerVPAdapter(this@HomeFragment)
            newsVPAdapter.addFragment(PromoteFragment(R.drawable.logo_toolbar))
            newsVPAdapter.addFragment(PromoteFragment(R.drawable.icon))
            viewPagerHomeNews.adapter = newsVPAdapter
            viewPagerHomeNews.orientation = ViewPager2.ORIENTATION_HORIZONTAL

            autoSlideNews(newsVPAdapter)

        }
    }

    //전시실 사진 ViewPager연결
    private fun connectAdapterGallery(){
        fragmentHomeBinding.apply {
            val galleryVPAdapter = BannerVPAdapter(this@HomeFragment)
            galleryVPAdapter.addFragment(PromoteFragment(R.drawable.logo_toolbar))
            galleryVPAdapter.addFragment(PromoteFragment(R.drawable.icon))
            viewPagerHomeGallery.adapter = galleryVPAdapter
            viewPagerHomeGallery.orientation = ViewPager2.ORIENTATION_HORIZONTAL

            autoSlideGallery(galleryVPAdapter)

        }
    }

    //viewPagerPromote 자동 넘김
    private fun autoSlidePromote(adapter : BannerVPAdapter){
        timer.scheduleAtFixedRate(object  : TimerTask(){
            override fun run() {
                handler.post {
                    val nextItem = fragmentHomeBinding.viewPagerHomePromote.currentItem + 1
                    if (nextItem < adapter.itemCount){
                        fragmentHomeBinding.viewPagerHomePromote.currentItem = nextItem
                    }else{
                        fragmentHomeBinding.viewPagerHomePromote.currentItem = 0
                    }
                }
            }
        }, 3000, 3000)
    }
    //viewPagerNews 자동 넘김
    private fun autoSlideNews(adapter : BannerVPAdapter){
        timer.scheduleAtFixedRate(object  : TimerTask(){
            override fun run() {
                handler.post {
                    val nextItem = fragmentHomeBinding.viewPagerHomeNews.currentItem + 1
                    if (nextItem < adapter.itemCount){
                        fragmentHomeBinding.viewPagerHomeNews.currentItem = nextItem
                    }else{
                        fragmentHomeBinding.viewPagerHomeNews.currentItem = 0
                    }
                }
            }
        }, 4000, 4000)
    }

    //viewPagerGallery 자동 넘김
    //viewPagerNews 자동 넘김
    private fun autoSlideGallery(adapter : BannerVPAdapter){
        timer.scheduleAtFixedRate(object  : TimerTask(){
            override fun run() {
                handler.post {
                    val nextItem = fragmentHomeBinding.viewPagerHomeGallery.currentItem + 1
                    if (nextItem < adapter.itemCount){
                        fragmentHomeBinding.viewPagerHomeGallery.currentItem = nextItem
                    }else{
                        fragmentHomeBinding.viewPagerHomeGallery.currentItem = 0
                    }
                }
            }
        }, 4000, 4000)
    }


}