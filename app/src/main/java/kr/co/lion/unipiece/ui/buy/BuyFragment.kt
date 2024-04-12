package kr.co.lion.unipiece.ui.buy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentBuyBinding
import kr.co.lion.unipiece.databinding.HeaderBuyDrawerBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.search.SearchFragment
import kr.co.lion.unipiece.ui.buy.adapter.BuyAdapter
import kr.co.lion.unipiece.ui.payment.cart.CartActivity
import kr.co.lion.unipiece.util.setMenuIconColor

class BuyFragment : Fragment() {

    lateinit var binding: FragmentBuyBinding

    private lateinit var callback: OnBackPressedCallback

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

    override fun onAttach(context: Context) {
        super.onAttach(context)

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

    override fun onDetach() {
        super.onDetach()
        // OnBackPressedCallback 해제
        callback.remove()
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
                    it.isChecked = true

                    when(it.itemId){
                        R.id.menuAll -> {

                        }
                        R.id.menuArtAll -> {

                        }
                        R.id.menuArtWest -> {

                        }
                        R.id.menuArtOri -> {

                        }
                        R.id.menuArtCalli -> {

                        }
                        R.id.menuArtSculp -> {

                        }
                        R.id.menuArtPrint -> {

                        }
                        R.id.menuArtWood -> {

                        }
                        R.id.menuArtGlass -> {

                        }
                        R.id.menuArtFabric -> {

                        }
                        R.id.menuArtMetal -> {

                        }
                        R.id.menuArtComic -> {

                        }
                        R.id.menuArtAni -> {

                        }
                        R.id.menuHumAll -> {

                        }
                        R.id.menuHumFiction -> {

                        }
                        R.id.menuHumPoem -> {

                        }
                        R.id.menuHumScript -> {

                        }
                        R.id.menuEngAll -> {

                        }
                        R.id.menuEngSoft -> {

                        }
                        R.id.menuEngHard -> {

                        }
                    }

                    true
                }
            }
        }
    }
    fun initViewPager() {

        val titles = listOf("인기 순", "신규 순")
        val vpAdapter = BuyAdapter(requireActivity())
        vpAdapter.addFragment(BuyPopFragment())
        vpAdapter.addFragment(BuyNewFragment())

        with(binding){
            buyVP.adapter = vpAdapter

            buyVP.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
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