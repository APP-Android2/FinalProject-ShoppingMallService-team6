package kr.co.lion.unipiece.ui.buy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.core.view.forEach
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentBuyBinding
import kr.co.lion.unipiece.databinding.HeaderBuyDrawerBinding
import kr.co.lion.unipiece.ui.search.SearchFragment
import kr.co.lion.unipiece.ui.buy.adapter.BuyAdapter
import kr.co.lion.unipiece.ui.buy.viewmodel.BuyViewModel
import kr.co.lion.unipiece.ui.payment.cart.CartActivity
import kr.co.lion.unipiece.util.setMenuIconColor

class BuyFragment : Fragment() {

    lateinit var binding: FragmentBuyBinding

    private lateinit var callback: OnBackPressedCallback

    private val viewModel: BuyViewModel by viewModels()

    var currentFragment : Int = -1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callBack()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBuyBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingToolbarBuy()
        initViewPager()
        setBuyNaviDrawer()
    }

    override fun onDetach() {
        super.onDetach()
        // OnBackPressedCallback 해제
        callback.remove()
    }

    fun callBack(){
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 뒤로가기 클릭시 동작하는 로직
                with(binding){
                    if(drawerBuyLayout.isDrawerOpen(GravityCompat.START)){
                        drawerBuyLayout.close()
                    } else {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }


    fun settingToolbarBuy(){

        with(binding) {
            toolbarBuy.apply {

                setNavigationIcon(R.drawable.menu_icon)
                setNavigationOnClickListener {
                    binding.drawerBuyLayout.open()
                }


                inflateMenu(R.menu.menu_search_cart)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_search -> {
                            val fragmentManager = parentFragmentManager.beginTransaction()
                            fragmentManager.replace(R.id.fl_container, SearchFragment()).addToBackStack("SearchFragment").commit()
                            true
                        }
                        R.id.menu_cart -> {
                            val intent = Intent(requireActivity(), CartActivity::class.java)
                            startActivity(intent)
                            true
                        }
                        else -> false
                    }
                }


                requireContext().setMenuIconColor(menu, R.id.menu_search, R.color.third)
                requireContext().setMenuIconColor(menu, R.id.menu_cart, R.color.second)

            }
        }
    }

    fun setBuyNaviDrawer(){

        with(binding){
            if(drawerBuyLayout.isDrawerOpen(GravityCompat.START)){
                drawerBuyLayout.close()
            }

            with(navigationDrawer){

                val headerDrawerBinding = HeaderBuyDrawerBinding.inflate(layoutInflater)
                addHeaderView(headerDrawerBinding.root)

                headerDrawerBinding.backBtn.setOnClickListener {
                    drawerBuyLayout.close()
                }

                setNavigationItemSelectedListener {

                    drawerBuyLayout.close()

                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.popLoading(true)
                        viewModel.newLoading(true)
                    }

                    it.setChecked(true)

                    when(it.itemId){
                        R.id.menuAll -> {
                            when(currentFragment){
                                0 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getPopPieceInfo()
                                    viewModel.popLoading(false)
                                }
                                1 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getNewPieceInfo()
                                    viewModel.newLoading(false)
                                }
                            }
                        }
                        R.id.menuArtAll -> {
                            when(currentFragment){
                                0 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getPopPieceSort("예술 대학")
                                    viewModel.popLoading(false)
                                }
                                1 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getNewPieceSort("예술 대학")
                                    viewModel.newLoading(false)
                                }
                            }
                        }
                        R.id.menuArtWest -> {
                            when(currentFragment){
                                0 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getPopPieceDetailSort("서양화")
                                    viewModel.popLoading(false)
                                }
                                1 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getNewPieceDetailSort("서양화")
                                    viewModel.newLoading(false)
                                }
                            }
                        }
                        R.id.menuArtOri -> {
                            when(currentFragment){
                                0 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getPopPieceDetailSort("동양화")
                                    viewModel.popLoading(false)
                                }
                                1 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getNewPieceDetailSort("동양화")
                                    viewModel.newLoading(false)
                                }
                            }
                        }
                        R.id.menuArtCalli -> {
                            when(currentFragment){
                                0 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getPopPieceDetailSort("서예")
                                    viewModel.popLoading(false)
                                }
                                1 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getNewPieceDetailSort("서예")
                                    viewModel.newLoading(false)
                                }
                            }
                        }
                        R.id.menuArtSculp -> {
                            when(currentFragment){
                                0 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getPopPieceDetailSort("조소")
                                    viewModel.popLoading(false)
                                }
                                1 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getNewPieceDetailSort("조소")
                                    viewModel.newLoading(false)
                                }
                            }
                        }
                        R.id.menuArtPrint -> {
                            when(currentFragment){
                                0 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getPopPieceDetailSort("판화")
                                    viewModel.popLoading(false)
                                }
                                1 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getNewPieceDetailSort("판화")
                                    viewModel.newLoading(false)
                                }
                            }
                        }
                        R.id.menuArtWood -> {
                            when(currentFragment){
                                0 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getPopPieceDetailSort("가구/목재")
                                    viewModel.popLoading(false)
                                }
                                1 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getNewPieceDetailSort("가구/목재")
                                    viewModel.newLoading(false)
                                }
                            }
                        }
                        R.id.menuArtGlass -> {
                            when(currentFragment){
                                0 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getPopPieceDetailSort("도자기/유리")
                                    viewModel.popLoading(false)
                                }
                                1 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getNewPieceDetailSort("도자기/유리")
                                    viewModel.newLoading(false)
                                }
                            }
                        }
                        R.id.menuArtFabric -> {
                            when(currentFragment){
                                0 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getPopPieceDetailSort("섬유/염색")
                                    viewModel.popLoading(false)
                                }
                                1 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getNewPieceDetailSort("섬유/염색")
                                    viewModel.newLoading(false)
                                }
                            }
                        }
                        R.id.menuArtMetal -> {
                            when(currentFragment){
                                0 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getPopPieceDetailSort("금속")
                                    viewModel.popLoading(false)
                                }
                                1 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getNewPieceDetailSort("금속")
                                    viewModel.newLoading(false)
                                }
                            }
                        }
                        R.id.menuArtComic -> {
                            when(currentFragment){
                                0 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getPopPieceDetailSort("만화")
                                    viewModel.popLoading(false)
                                }
                                1 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getNewPieceDetailSort("만화")
                                    viewModel.newLoading(false)
                                }
                            }
                        }
                        R.id.menuArtAni -> {
                            when(currentFragment){
                                0 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getPopPieceDetailSort("애니메이션")
                                    viewModel.popLoading(false)
                                }
                                1 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getNewPieceDetailSort("애니메이션")
                                    viewModel.newLoading(false)
                                }
                            }
                        }
                        R.id.menuHumAll -> {
                            when(currentFragment){
                                0 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getPopPieceSort("인문 대학")
                                    viewModel.popLoading(false)
                                }
                                1 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getNewPieceSort("인문 대학")
                                    viewModel.newLoading(false)
                                }
                            }
                        }
                        R.id.menuHumFiction -> {
                            when(currentFragment){
                                0 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getPopPieceDetailSort("소설")
                                    viewModel.popLoading(false)
                                }
                                1 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getNewPieceDetailSort("소설")
                                    viewModel.newLoading(false)
                                }
                            }
                        }
                        R.id.menuHumPoem -> {
                            when(currentFragment){
                                0 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getPopPieceDetailSort("시")
                                    viewModel.popLoading(false)
                                }
                                1 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getNewPieceDetailSort("시")
                                    viewModel.newLoading(false)
                                }
                            }
                        }
                        R.id.menuHumScript -> {
                            when(currentFragment){
                                0 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getPopPieceDetailSort("극본")
                                    viewModel.popLoading(false)
                                }
                                1 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getNewPieceDetailSort("극본")
                                    viewModel.newLoading(false)
                                }
                            }
                        }
                        R.id.menuEngAll -> {
                            when(currentFragment){
                                0 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getPopPieceSort("공과 대학")
                                    viewModel.popLoading(false)
                                }
                                1 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getNewPieceSort("공과 대학")
                                    viewModel.newLoading(false)
                                }
                            }
                        }
                        R.id.menuEngSoft -> {
                            when(currentFragment){
                                0 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getPopPieceDetailSort("소프트웨어")
                                    viewModel.popLoading(false)
                                }
                                1 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getNewPieceDetailSort("소프트웨어")
                                    viewModel.newLoading(false)
                                }
                            }
                        }
                        R.id.menuEngHard -> {
                            when(currentFragment){
                                0 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getPopPieceDetailSort("하드웨어")
                                    viewModel.popLoading(false)
                                }
                                1 -> viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getNewPieceDetailSort("하드웨어")
                                    viewModel.newLoading(false)
                                }
                            }
                        }
                    }
                    true
                }
            }
        }
    }

    fun initViewPager() {

        val titles = listOf("인기 순", "신규 순")
        val vpAdapter = BuyAdapter(this)
        vpAdapter.addFragment(BuyPopFragment())
        vpAdapter.addFragment(BuyNewFragment())

        with(binding){
            buyVP.adapter = vpAdapter

            buyVP.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    currentFragment = position

                    when(currentFragment){
                        0 -> viewLifecycleOwner.lifecycleScope.launch {
                            viewModel.getPopPieceInfo()
                        }
                        1 -> viewLifecycleOwner.lifecycleScope.launch {
                            viewModel.getNewPieceInfo()
                        }
                    }
                    navigationDrawer.menu.findItem(R.id.menuAll).isChecked = true

                    super.onPageSelected(position)
                }
            })

            // ViewPager TabLayout 연결
            TabLayoutMediator(tabLayout, buyVP) { tab, position ->
                tab.text = titles[position]
            }.attach()

            // 각 탭에 OnClickListener 설정
            for (i in 0 until tabLayout.tabCount) {
                val tab = tabLayout.getTabAt(i)
                tab?.view?.setOnClickListener {
                    buyVP.currentItem = i
                }
            }

        }
    }

}