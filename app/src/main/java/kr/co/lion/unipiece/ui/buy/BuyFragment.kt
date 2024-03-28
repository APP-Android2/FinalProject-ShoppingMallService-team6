package kr.co.lion.unipiece.ui.buy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentBuyBinding
import kr.co.lion.unipiece.util.setMenuIconColor

class BuyFragment : Fragment() {

    lateinit var binding: FragmentBuyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBuyBinding.inflate(inflater, container, false)

        settingToolbarBuy()

        return binding.root
    }

    fun settingToolbarBuy(){

        with(binding) {
            toolbarBuy.apply {

                setNavigationIcon(R.drawable.menu_icon)

                inflateMenu(R.menu.menu_search_cart)

                requireContext().setMenuIconColor(menu, R.id.menu_search, R.color.third)
                requireContext().setMenuIconColor(menu, R.id.menu_cart, R.color.second)

            }
        }
    }

}