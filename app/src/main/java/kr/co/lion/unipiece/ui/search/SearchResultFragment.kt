package kr.co.lion.unipiece.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentSearchResultBinding
import kr.co.lion.unipiece.util.setMenuIconColor

class SearchResultFragment : Fragment() {

    lateinit var binding: FragmentSearchResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingToolbarSearchResult()
    }

    fun settingToolbarSearchResult(){
        with(binding.toolbarSearchResult) {
            setNavigationOnClickListener {
                activity?.supportFragmentManager?.popBackStack()
                activity?.supportFragmentManager?.popBackStack()
            }

            inflateMenu(R.menu.menu_cart)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_cart -> {

                        true
                    }
                    else -> false
                }
            }
            requireContext().setMenuIconColor(menu, R.id.menu_cart, R.color.second)
        }
    }

}