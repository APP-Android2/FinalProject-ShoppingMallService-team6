package kr.co.lion.unipiece.ui.search

import android.content.Intent
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
import kr.co.lion.unipiece.ui.author.AuthorInfoActivity
import kr.co.lion.unipiece.ui.buy.BuyDetailActivity
import kr.co.lion.unipiece.ui.payment.cart.CartActivity
import kr.co.lion.unipiece.ui.search.adapter.SearchAuthorData
import kr.co.lion.unipiece.ui.search.adapter.SearchPieceData
import kr.co.lion.unipiece.ui.search.adapter.SearchResultAdapter
import kr.co.lion.unipiece.ui.search.adapter.SearchResultData
import kr.co.lion.unipiece.ui.search.adapter.SearchResultViewType
import kr.co.lion.unipiece.ui.search.adapter.SearchResultViewType.*
import kr.co.lion.unipiece.util.setMenuIconColor

class SearchResultFragment : Fragment() {

    lateinit var binding: FragmentSearchResultBinding

    val testList: ArrayList<SearchResultData> = arrayListOf()

    val searchAdpater: SearchResultAdapter by lazy {
        SearchResultAdapter(
            itemClickListener = {data ->
                moveToPage(data.viewType)
            }
        )
    }

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
        setTestList(testList)
        setRecyclerView()

    }

    fun settingToolbarSearchResult(){
        with(binding.toolbarSearchResult) {
            setNavigationOnClickListener {

                val supportFragmentManager = activity?.supportFragmentManager

                var isSearchFragmentOnTop = false
                val count = supportFragmentManager?.backStackEntryCount ?: 0
                if (count > 0) {
                    // 백 스택의 마지막 항목의 이름을 가져옵니다.
                    val lastFragmentName = activity?.supportFragmentManager?.getBackStackEntryAt(count - 1)?.name
                    // 마지막 항목의 이름이 "SearchFragment"와 일치하는지 확인합니다.
                    isSearchFragmentOnTop = "SearchFragment" == lastFragmentName
                } else {
                    // 백 스택이 비어있으면, SearchFragment가 최상단에 있을 수 없습니다.
                    isSearchFragmentOnTop = false
                }

                if(isSearchFragmentOnTop) {
                    activity?.onBackPressed()
                    activity?.onBackPressed()
                } else {
                    activity?.onBackPressed()
                }

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

    fun setRecyclerView(){
        with(binding) {
            searchResultRV.adapter = searchAdpater
            searchResultRV.layoutManager = GridLayoutManager(activity, 2)
            searchAdpater.setData(testList)
        }
    }

    fun setTestList(testList: ArrayList<SearchResultData>) {
        testList.add(SearchResultData(SearchAuthorData(), SearchPieceData(), AUTHOR_TITLE))
        testList.add(SearchResultData(SearchAuthorData(), SearchPieceData(), AUTHOR_TITLE, false))
        for(i in 0 until 4){
            testList.add(SearchResultData(SearchAuthorData(), SearchPieceData(), AUTHOR_CONTENT))
        }

        if(testList.filter { it.viewType == SearchResultViewType.AUTHOR_CONTENT }.count() % 2 == 0){
            testList.add(SearchResultData(SearchAuthorData(), SearchPieceData(), PIECE_TITLE))
            testList.add(SearchResultData(SearchAuthorData(), SearchPieceData(), PIECE_TITLE, false))
        } else {
            testList.add(SearchResultData(SearchAuthorData(), SearchPieceData(), PIECE_TITLE, false))
            testList.add(SearchResultData(SearchAuthorData(), SearchPieceData(), PIECE_TITLE))
            testList.add(SearchResultData(SearchAuthorData(), SearchPieceData(), PIECE_TITLE, false))
        }

        for(i in 0 until 10){
            testList.add(SearchResultData(SearchAuthorData(), SearchPieceData(), PIECE_CONTENT))
        }
    }

    fun moveToPage(viewType: SearchResultViewType) {
        if(viewType == PIECE_CONTENT){
            val intent = Intent(requireActivity(), BuyDetailActivity::class.java)
            startActivity(intent)
        }
        if(viewType == AUTHOR_CONTENT){
            val intent = Intent(requireActivity(), AuthorInfoActivity::class.java)
            startActivity(intent)
        }
    }
}