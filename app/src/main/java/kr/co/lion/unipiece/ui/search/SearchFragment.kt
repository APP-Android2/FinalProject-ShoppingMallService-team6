package kr.co.lion.unipiece.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentSearchBinding
import kr.co.lion.unipiece.model.SearchResultData
import kr.co.lion.unipiece.ui.author.AuthorInfoActivity
import kr.co.lion.unipiece.ui.buy.BuyDetailActivity
import kr.co.lion.unipiece.ui.payment.CartActivity
import kr.co.lion.unipiece.ui.search.adapter.SearchResultAdapter
import kr.co.lion.unipiece.ui.search.adapter.SearchResultViewType
import kr.co.lion.unipiece.ui.search.viewmodel.SearchViewModel
import kr.co.lion.unipiece.util.hideSoftInput
import kr.co.lion.unipiece.util.setMenuIconColor

class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingToolbarSearch()
        initView()
        search()
        setLoading()
    }

    fun initView() {

        viewModel.searchList.observe(viewLifecycleOwner){ value ->

            if(value.size > 0){
                val searchResultAdapter =
                    SearchResultAdapter(
                        value,
                        itemClickListener = {position ->
                            moveToPage(value[position].viewType, value[position])
                        }
                    )

                with(binding){
                    searchLoading.visibility = View.GONE
                    searchImageView.visibility = View.GONE
                    searchTextView.visibility = View.GONE
                    searchResultRV.visibility = View.VISIBLE

                    searchResultRV.adapter = searchResultAdapter
                    searchResultRV.layoutManager = GridLayoutManager(activity, 2)
                }

            } else {
                with(binding){
                    searchLoading.visibility = View.GONE
                    searchImageView.visibility = View.VISIBLE
                    searchTextView.visibility = View.VISIBLE
                    searchTextView.text = "검색 결과가 없습니다"
                    searchResultRV.visibility = View.GONE
                }
            }
            viewModel.setLoading(false)
        }
    }

    fun setLoading(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loading.observe(viewLifecycleOwner) { value ->
                if (value == true) {
                    binding.searchResultRV.scrollToPosition(0)
                    binding.searchLoading.visibility = View.VISIBLE
                    binding.searchImageView.visibility = View.GONE
                    binding.searchTextView.visibility = View.GONE
                    binding.searchResultRV.visibility = View.GONE

                } else {
                    binding.searchLoading.visibility = View.GONE
                }
            }
        }
    }

    fun moveToPage(viewType: SearchResultViewType, data: SearchResultData) {
        if(viewType == SearchResultViewType.PIECE_CONTENT){
            val intent = Intent(requireActivity(), BuyDetailActivity::class.java)
            val pieceIdx = data.searchPieceData.pieceIdx
            val authorIdx = data.searchPieceData.authorIdx
            intent.putExtra("pieceIdx", pieceIdx)
            intent.putExtra("authorIdx", authorIdx)
            startActivity(intent)
        }
        if(viewType == SearchResultViewType.AUTHOR_CONTENT){
            val intent = Intent(requireActivity(), AuthorInfoActivity::class.java)
            val idx = data.searchAuthorData.authorIdx
            intent.putExtra("authorIdx", idx)
            startActivity(intent)
        }
    }

    fun settingToolbarSearch(){
        with(binding.toolbarSearch) {

            setNavigationOnClickListener {
               requireActivity().onBackPressed()
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
            searchBar.setOnEditorActionListener { _, action, _ ->
                if (action == EditorInfo.IME_ACTION_SEARCH) {
                    startSearch()
                    true
                } else false
            }

            searchBar.setOnTouchListener { view, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    searchBar.compoundDrawables[2]?.let {
                        val touchArea = searchBar.right - it.bounds.width() - 50
                        if (event.rawX >= touchArea) {
                            startSearch()
                            return@setOnTouchListener true
                        }
                    }
                }
                false
            }
        }
    }

    fun startSearch() {
        requireActivity().hideSoftInput()
        viewModel.setLoading(true)
        viewModel.search(binding.searchBar.text.toString()) { success ->
            if (success) {
                viewModel.setLoading(false)
            }
        }
    }
}