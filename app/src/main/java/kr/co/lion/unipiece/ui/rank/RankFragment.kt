package kr.co.lion.unipiece.ui.rank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentRankBinding
import kr.co.lion.unipiece.ui.search.SearchFragment
import kr.co.lion.unipiece.util.RankFragmentName
import kr.co.lion.unipiece.util.RankFragmentName.*
import kr.co.lion.unipiece.util.setMenuIconColor

class RankFragment : Fragment() {

    lateinit var binding: FragmentRankBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRankBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingToolbarRank()
        initView()
    }

    private fun initView() {
        setFragment(RANK_PIECE_FRAGMENT)
    }

    private fun settingToolbarRank(){

        with(binding) {
            toolbarRank.apply {

                inflateMenu(R.menu.menu_search)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_search -> {
                            val fragmentManager = activity?.supportFragmentManager?.beginTransaction()
                            fragmentManager?.replace(R.id.fl_container, SearchFragment())?.addToBackStack("BuyFragment")?.commit()
                            true
                        }
                        else -> false
                    }
                }

                requireContext().setMenuIconColor(menu, R.id.menu_search, R.color.second)

            }
        }
    }

    private fun setFragment(name: RankFragmentName) {

        val fragmentMananger = childFragmentManager.beginTransaction()

            when(name) {
                RANK_PIECE_FRAGMENT -> {
                    fragmentMananger.replace(R.id.rank_fragment, RankPieceFragment())
                    binding.rankTitle.text = "작품 랭킹"
                }
            }

        fragmentMananger.setReorderingAllowed(true)
        fragmentMananger.addToBackStack(name.str)
        fragmentMananger.commit()
    }


}