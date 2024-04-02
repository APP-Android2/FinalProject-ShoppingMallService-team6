package kr.co.lion.unipiece.ui.mygallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentMyGalleryBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.util.setMenuIconColor

class MyGalleryFragment : Fragment() {

    lateinit var binding: FragmentMyGalleryBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMyGalleryBinding.inflate(inflater, container, false)
        mainActivity = activity as MainActivity

        settingToolbar()
        initView()

        return binding.root
    }

    fun settingToolbar() {
        binding.apply {
            toolbarMyGallery.apply {
                inflateMenu(R.menu.menu_cart)

                requireContext().setMenuIconColor(menu, R.id.menu_cart, R.color.second)
            }
        }
    }

    fun initView() {
        val viewPager = binding.viewpagerMyGallery
        val tabLayout = binding.tabLayoutMyGallery

        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(InterestingPieceFragment())
        fragmentList.add(PurchasedPieceFragment())
        fragmentList.add(SalePieceFragment())

        viewPager.adapter = MyGalleryViewPagerAdapter(fragmentList, mainActivity)

        val tabTextList = ArrayList<String?>()
        tabTextList.add("관심 작품")
        tabTextList.add("구매한 작품")
        tabTextList.add("판매 작품")

        // ViewPager2에 Fragment가 추가된 후에 TabLayoutMediator 설정
        viewPager.post {
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabTextList[position]
            }.attach()
        }
    }
}