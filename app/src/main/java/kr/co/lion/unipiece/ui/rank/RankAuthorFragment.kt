package kr.co.lion.unipiece.ui.rank

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentRankAuthorBinding
import kr.co.lion.unipiece.ui.buy.BuyNewFragment
import kr.co.lion.unipiece.ui.buy.BuyPopFragment
import kr.co.lion.unipiece.ui.buy.adapter.BuyAdapter
import kr.co.lion.unipiece.ui.rank.adapter.RankAuthorVPAdapter
import kr.co.lion.unipiece.ui.rank.viewmodel.RankViewModel

class RankAuthorFragment : Fragment() {

    lateinit var binding: FragmentRankAuthorBinding

    val vpAdapter: RankAuthorVPAdapter by lazy {
        RankAuthorVPAdapter(this)
    }

    private val viewModel: RankViewModel by viewModels( ownerProducer = { requireParentFragment() } )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRankAuthorBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
    }

    fun initViewPager() {

        val titles = listOf("팔로워 순", "판매횟수 순")
        vpAdapter.addFragment(RankFollowerFragment())
        vpAdapter.addFragment(RankSaleFragment())

        with(binding){
            rankAuthorVP.adapter = vpAdapter

            rankAuthorVP.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })

            // ViewPager TabLayout 연결
            TabLayoutMediator(tabLayout, rankAuthorVP) { tab, position ->
                tab.text = titles[position]
            }.attach()

            // 각 탭에 OnClickListener 설정
            for (i in 0 until tabLayout.tabCount) {
                val tab = tabLayout.getTabAt(i)
                tab?.view?.setOnClickListener {
                    rankAuthorVP.currentItem = i
                }
            }

        }
    }
}