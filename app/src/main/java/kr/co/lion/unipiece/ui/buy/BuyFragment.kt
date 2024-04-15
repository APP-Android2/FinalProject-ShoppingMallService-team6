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
import androidx.fragment.app.activityViewModels
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

    private val viewModel: BuyViewModel by activityViewModels()

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
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewModel.getPopPieceInfo()
                            }
                        }
                        R.id.menuArtAll -> {
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewModel.getPopPieceSort("예술 대학")
                            }
                        }
                        R.id.menuArtWest -> {
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewModel.getPopPieceDetailSort("서양화")
                            }
                        }
                        R.id.menuArtOri -> {
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewModel.getPopPieceDetailSort("동양화")
                            }
                        }
                        R.id.menuArtCalli -> {
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewModel.getPopPieceDetailSort("서예")
                            }
                        }
                        R.id.menuArtSculp -> {
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewModel.getPopPieceDetailSort("조소")
                            }
                        }
                        R.id.menuArtPrint -> {
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewModel.getPopPieceDetailSort("판화")
                            }
                        }
                        R.id.menuArtWood -> {
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewModel.getPopPieceDetailSort("가구/목재")
                            }
                        }
                        R.id.menuArtGlass -> {
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewModel.getPopPieceDetailSort("도자기/유리")
                            }
                        }
                        R.id.menuArtFabric -> {
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewModel.getPopPieceDetailSort("섬유/염색")
                            }
                        }
                        R.id.menuArtMetal -> {
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewModel.getPopPieceDetailSort("금속")
                            }
                        }
                        R.id.menuArtComic -> {
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewModel.getPopPieceDetailSort("만화")
                            }
                        }
                        R.id.menuArtAni -> {
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewModel.getPopPieceDetailSort("애니메이션")
                            }
                        }
                        R.id.menuHumAll -> {
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewModel.getPopPieceSort("인문 대학")
                            }
                        }
                        R.id.menuHumFiction -> {
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewModel.getPopPieceDetailSort("소설")
                            }
                        }
                        R.id.menuHumPoem -> {
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewModel.getPopPieceDetailSort("시")
                            }
                        }
                        R.id.menuHumScript -> {
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewModel.getPopPieceDetailSort("극본")
                            }
                        }
                        R.id.menuEngAll -> {
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewModel.getPopPieceSort("공과 대학")
                            }
                        }
                        R.id.menuEngSoft -> {
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewModel.getPopPieceDetailSort("소프트웨어")
                            }
                        }
                        R.id.menuEngHard -> {
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewModel.getPopPieceDetailSort("하드웨어")
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
        val vpAdapter = BuyAdapter(requireActivity())
        vpAdapter.addFragment(BuyPopFragment())
        vpAdapter.addFragment(BuyNewFragment())

        with(binding){
            buyVP.adapter = vpAdapter

            buyVP.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
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