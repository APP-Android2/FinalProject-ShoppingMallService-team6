package kr.co.lion.unipiece.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentHomeBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.author.AuthorInfoActivity
import kr.co.lion.unipiece.ui.home.viewModel.HomeViewModel
import kr.co.lion.unipiece.ui.infomation.InfoAllActivity
import kr.co.lion.unipiece.ui.mypage.VisitGalleryActivity
import kr.co.lion.unipiece.ui.payment.CartActivity
import kr.co.lion.unipiece.ui.search.SearchFragment
import kr.co.lion.unipiece.util.setMenuIconColor
import java.util.Timer
import java.util.TimerTask

class HomeFragment : Fragment() {

    lateinit var fragmentHomeBinding: FragmentHomeBinding

    lateinit var mainActivity: MainActivity

    val viewModel: HomeViewModel by viewModels()


    val timer = Timer()
    val handler = Handler(Looper.getMainLooper())

    val authorAdapter:AuthorAdapter by lazy {
        var adapter = AuthorAdapter(emptyList())
        adapter.setRecyclerviewClickListener(object : AuthorAdapter.AuthorOnClickListener{
            override fun authorItemClickListener(authorIdx:Int) {
                val newIntent = Intent(requireActivity(), AuthorInfoActivity::class.java)
                newIntent.putExtra("authorIdx", authorIdx)
                startActivity(newIntent)
            }
        })
        adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        initView()
        settingToolBar()
        settingEvent()
        connectAdapterPromote()
        connectAdapterNews()
        connectAdapterGallery()
        settingAdapter()

        return fragmentHomeBinding.root
    }

    //툴바 설정
    private fun settingToolBar(){
        fragmentHomeBinding.apply {
            toolBarHome.apply {

                setNavigationIcon(R.drawable.logo_toolbar_2)

                inflateMenu(R.menu.menu_search_cart)

                requireContext().setMenuIconColor(menu, R.id.menu_search, R.color.third)
                requireContext().setMenuIconColor(menu, R.id.menu_cart, R.color.second)
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_search -> {
                            val fragmentManager = activity?.supportFragmentManager?.beginTransaction()
                            fragmentManager?.replace(R.id.fl_container, SearchFragment())?.addToBackStack(null)?.commit()
                            true
                        }

                        R.id.menu_cart -> {
                            startActivity(Intent(mainActivity, CartActivity::class.java))
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
                lifecycleScope.launch {
                    var promoteInfo = viewModel.getPromoteDataByDate()
                    val newIntent = Intent(requireActivity(), InfoAllActivity::class.java)
                    //Log.d("seonguk1234", "${promoteInfo}")
                    newIntent.putExtra("promoteInfo", promoteInfo.toString())
                    startActivity(newIntent)
                }
        }

            buttonAllNews.setOnClickListener {
                startActivity(Intent(mainActivity, InfoAllActivity::class.java))
            }

            buttonAllGallery.setOnClickListener {
                lifecycleScope.launch {
                    val galleryInfo = viewModel.getGalleryDataByDate()
                    //Log.d("seonguk1234", "${galleryInfo}")
                    val newIntent = Intent(requireActivity(), InfoAllActivity::class.java)
                    newIntent.putExtra("galleryInfo", galleryInfo.toString())
                    startActivity(newIntent)
                }
            }

            buttonHomeVisitGallery.setOnClickListener {
                startActivity(Intent(mainActivity, VisitGalleryActivity::class.java))
            }
        }
    }

    //전시홍보 ViewPager 연결
    private fun connectAdapterPromote(){
        fragmentHomeBinding.apply {
            val bannerVPAdapter = BannerVPAdapter(this@HomeFragment)
            viewPagerHomePromote.adapter = bannerVPAdapter
            viewPagerHomePromote.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            progressBar2.visibility = View.VISIBLE

            viewModel.promoteInfo.observe(viewLifecycleOwner, Observer { imageUrls ->
                bannerVPAdapter.fragmentList.clear()
                progressBar2.visibility = View.GONE
                imageUrls.forEach { imageUrl ->
                    val promoteFragment = PromoteFragment(imageUrl)
                    bannerVPAdapter.addFragment(promoteFragment)
                }
                bannerVPAdapter.notifyDataSetChanged()
            })
            viewModel.getPromoteImages()

            autoSlidePromote(bannerVPAdapter)

        }
    }

    //소식 ViewPager 연결
    private fun connectAdapterNews(){
        fragmentHomeBinding.apply {
            val newsVPAdapter = BannerVPAdapter(this@HomeFragment)
            viewPagerHomeNews.adapter = newsVPAdapter
            viewPagerHomeNews.orientation = ViewPager2.ORIENTATION_HORIZONTAL

            viewModel.newsInfo.observe(viewLifecycleOwner, Observer { imageUrls ->
                newsVPAdapter.fragmentList.clear()
                imageUrls.forEach {
                    val newsFragment = NewsFragment(it)
                    newsVPAdapter.addFragment(newsFragment)
                }
                newsVPAdapter.notifyDataSetChanged()
            })
            viewModel.getNewsImages()


            autoSlideNews(newsVPAdapter)

        }
    }

    //전시실 사진 ViewPager연결
    private fun connectAdapterGallery(){
        fragmentHomeBinding.apply {
            val galleryVPAdapter = BannerVPAdapter(this@HomeFragment)
            viewPagerHomeGallery.adapter = galleryVPAdapter
            viewPagerHomeGallery.orientation = ViewPager2.ORIENTATION_HORIZONTAL


            viewModel.galleryInfoList.observe(viewLifecycleOwner) { imageUrl ->
                galleryVPAdapter.fragmentList.clear()
                imageUrl.forEach {
                    val galleryFragment = GalleryFragment(it)
                    galleryVPAdapter.addFragment(galleryFragment)
                }
                galleryVPAdapter.notifyDataSetChanged()
            }
            viewModel.getGalleryImg()

            autoSlideGallery(galleryVPAdapter)

        }
    }

    //작가 Recyclerview
    private fun initView(){
        fragmentHomeBinding.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.authorInfoDataList.observe(viewLifecycleOwner) { value ->
                    authorAdapter.updateData(value)
                }
                viewModel.getAuthorInfoAll()
            }
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

    private fun settingAdapter() {
        fragmentHomeBinding.apply {
            recyclerViewAuthor.apply {
                adapter = authorAdapter
                layoutManager =
                    LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false)

            }
        }
    }

}