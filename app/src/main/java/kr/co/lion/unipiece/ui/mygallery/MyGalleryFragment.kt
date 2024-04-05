package kr.co.lion.unipiece.ui.mygallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.google.android.material.tabs.TabLayoutMediator
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentMyGalleryBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.mygallery.adapter.MyGalleryViewPagerAdapter
import kr.co.lion.unipiece.util.setMenuIconColor

class MyGalleryFragment : Fragment() {

    lateinit var binding: FragmentMyGalleryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMyGalleryBinding.inflate(inflater, container, false)

        settingToolbar()
        initView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewPager의 높이를 동적으로 계산하여 설정
        binding.viewpagerMyGallery.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val viewPager = binding.viewpagerMyGallery
                val screenHeight = resources.displayMetrics.heightPixels

                // ViewPager의 높이 계산
                val viewpagerLayoutParams = viewPager.layoutParams
                val density = resources.displayMetrics.density
                val additionalHeightInPixel = (80 * density).toInt()
                // 나머지 화면 높이 계산
                viewpagerLayoutParams.height = screenHeight - viewPager.top - additionalHeightInPixel
                viewPager.layoutParams = viewpagerLayoutParams

                // OnGlobalLayoutListener 제거
                viewPager.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
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

        viewPager.adapter = MyGalleryViewPagerAdapter(fragmentList, requireActivity())

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