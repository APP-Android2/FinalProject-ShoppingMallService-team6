package kr.co.lion.unipiece.ui.rank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentRankBinding
import kr.co.lion.unipiece.ui.rank.viewmodel.RankViewModel
import kr.co.lion.unipiece.ui.search.SearchFragment
import kr.co.lion.unipiece.util.RankFragmentName
import kr.co.lion.unipiece.util.RankFragmentName.*
import kr.co.lion.unipiece.util.setMenuIconColor

class RankFragment : Fragment() {

    lateinit var binding: FragmentRankBinding

    private val viewModel: RankViewModel by viewModels()
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
        setLoading()
        setFragmentBtn()
    }

    private fun initView() {
        setFragment(RANK_PIECE_FRAGMENT)
    }

    fun setLoading(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loading.observe(viewLifecycleOwner) { value ->
                if (value) {
                    binding.rankProgressBar.visibility = View.VISIBLE
                } else {
                    binding.rankProgressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun settingToolbarRank(){

        with(binding) {
            toolbarRank.apply {

                inflateMenu(R.menu.menu_search)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_search -> {
                            val fragmentManager = parentFragmentManager.beginTransaction()
                            fragmentManager.replace(R.id.fl_container, SearchFragment()).addToBackStack("SearchFragment").commit()
                            true
                        }
                        else -> false
                    }
                }

                requireContext().setMenuIconColor(menu, R.id.menu_search, R.color.second)

            }
        }
    }

    private fun setFragmentBtn() {
        with(binding.menuBtn) {
            setOnClickListener {
                val popup = PopupMenu(requireContext(), it)
                popup.menuInflater.inflate(R.menu.menu_rank, popup.menu)
                popup.setOnMenuItemClickListener { menuItem ->

                    val selectedFragment = when(menuItem.itemId) {
                        R.id.menu_author -> RANK_AUTHOR_FRAGMENT
                        R.id.menu_piece -> RANK_PIECE_FRAGMENT
                        else -> null
                    }

                    selectedFragment?.let { fragment ->
                        setFragment(fragment)
                    }
                    return@setOnMenuItemClickListener true
                }
                popup.show()
            }
        }
    }

    private fun setFragment(name: RankFragmentName) {

        val fragmentMananger = childFragmentManager.beginTransaction()

        when (name) {
            RANK_PIECE_FRAGMENT -> {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.setLoading(true)
                    fragmentMananger.replace(R.id.rank_fragment, RankPieceFragment())
                    binding.rankTitle.text = "작품 랭킹"
                }
            }

            RANK_AUTHOR_FRAGMENT -> {
                fragmentMananger.replace(R.id.rank_fragment, RankAuthorFragment())
                binding.rankTitle.text = "작가 랭킹"
            }
        }
        fragmentMananger.commit()
    }


}