package kr.co.lion.unipiece.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentSearchBinding
import kr.co.lion.unipiece.ui.payment.cart.CartActivity
import kr.co.lion.unipiece.util.hideSoftInput
import kr.co.lion.unipiece.util.setMenuIconColor

class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingToolbarSearch()
        search()
    }

    fun settingToolbarSearch(){
        with(binding.toolbarSearch) {

            setNavigationOnClickListener {
                activity?.onBackPressed()
                activity?.onBackPressed()
            }

            inflateMenu(R.menu.menu_cart)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_cart -> {
                        val intent = Intent(requireActivity(), CartActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
            requireContext().setMenuIconColor(menu, R.id.menu_cart, R.color.second)
        }
    }

    fun search(){
        with(binding){
            var handled = false

            searchBar.setOnEditorActionListener { textView, action, Event ->
                if(action == EditorInfo.IME_ACTION_SEARCH){
                    val fragmentManager = activity?.supportFragmentManager?.beginTransaction()
                    fragmentManager?.replace(R.id.fl_container, SearchResultFragment())?.addToBackStack("SearchFragment")?.commit()
                    handled = true
                    requireActivity().hideSoftInput()
                }
                handled
            }

            searchBar.setOnTouchListener { view, event ->
                if(event.action == MotionEvent.ACTION_UP) {
                    val touchArea = searchBar.right - searchBar.compoundDrawables[2].bounds.width() - 50
                    if(event.rawX >= touchArea) {
                        val fragmentManager = activity?.supportFragmentManager?.beginTransaction()
                        fragmentManager?.replace(R.id.fl_container, SearchResultFragment())?.addToBackStack("SearchFragment")?.commit()
                        requireActivity().hideSoftInput()
                        handled = true
                    }
                }
                handled
            }
        }
    }
}