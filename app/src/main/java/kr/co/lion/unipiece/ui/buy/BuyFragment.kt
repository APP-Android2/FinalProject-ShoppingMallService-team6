package kr.co.lion.unipiece.ui.buy

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentBuyBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.search.SearchFragment
import kr.co.lion.unipiece.ui.buy.adapter.BuyAdapter
import kr.co.lion.unipiece.ui.payment.cart.CartActivity
import kr.co.lion.unipiece.util.setMenuIconColor

class BuyFragment : Fragment() {

    lateinit var binding: FragmentBuyBinding

    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBuyBinding.inflate(inflater, container, false)

        mainActivity = activity as MainActivity

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingToolbarBuy()
        initViewPager()
    }

    fun settingToolbarBuy(){

        with(binding) {
            toolbarBuy.apply {

                setNavigationIcon(R.drawable.menu_icon)
                setNavigationOnClickListener {
                    mainActivity.binding.drawerBuyLayout.open()
                }


                inflateMenu(R.menu.menu_search_cart)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_search -> {
                            val fragmentManager = activity?.supportFragmentManager?.beginTransaction()
                            fragmentManager?.replace(R.id.fl_container, SearchFragment())?.addToBackStack(null)?.commit()
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