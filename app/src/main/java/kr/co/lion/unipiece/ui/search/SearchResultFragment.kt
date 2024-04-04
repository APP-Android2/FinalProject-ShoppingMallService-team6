package kr.co.lion.unipiece.ui.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentSearchResultBinding
import kr.co.lion.unipiece.ui.search.adapter.SearchAuthorAdapter
import kr.co.lion.unipiece.ui.search.adapter.SearchPieceAdapter
import kr.co.lion.unipiece.util.setMenuIconColor

class SearchResultFragment : Fragment() {

    lateinit var binding: FragmentSearchResultBinding

    val testAuthorList = arrayListOf(R.drawable.mypage_icon, R.drawable.icon, R.drawable.icon,
        R.drawable.icon, R.drawable.mypage_icon, R.drawable.icon,
        R.drawable.mypage_icon, R.drawable.icon, R.drawable.mypage_icon)

    val testPieceList = arrayListOf(R.drawable.logo, R.drawable.icon, R.drawable.logo,
        R.drawable.icon, R.drawable.logo, R.drawable.icon,
        R.drawable.logo, R.drawable.icon, R.drawable.logo)

    /*val authorAdapter: SearchAuthorAdapter by lazy {
        SearchAuthorAdapter(testAuthorList,
            itemClickListener = { testAuthorId ->
                Log.d("testAuthor", testAuthorId.toString())
            }
        )
    }

    val pieceAdpater: SearchPieceAdapter by lazy {
        SearchPieceAdapter(testPieceList,
            itemClickListener = {testPieceId ->
                Log.d("testPiece", testPieceId.toString())
            }
        )
    }*/

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

    /*fun setAuthorRecyclerView(){
        with(binding) {
            searchAuthorRV.adapter = authorAdapter
            searchAuthorRV.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            authorAdapter.notifyDataSetChanged()
        }
    }

    fun setPieceRecyclerView(){
        with(binding) {
            searchPieceRV.adapter = pieceAdpater
            searchPieceRV.layoutManager = GridLayoutManager(activity, 2)
            pieceAdpater.notifyDataSetChanged()
        }
    }*/

}