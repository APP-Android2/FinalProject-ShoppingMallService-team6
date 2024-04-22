package kr.co.lion.unipiece.ui.rank

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentRankFollowerBinding
import kr.co.lion.unipiece.ui.author.AuthorInfoActivity
import kr.co.lion.unipiece.ui.buy.BuyDetailActivity
import kr.co.lion.unipiece.ui.rank.adapter.RankFollowerAdapter
import kr.co.lion.unipiece.ui.rank.adapter.RankPieceAdapter
import kr.co.lion.unipiece.ui.rank.adapter.RankSaleAdapter
import kr.co.lion.unipiece.ui.rank.viewmodel.RankViewModel

class RankFollowerFragment : Fragment() {

    lateinit var binding: FragmentRankFollowerBinding

    private val viewModel: RankViewModel by viewModels( ownerProducer = { requireParentFragment().requireParentFragment() } )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRankFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setLoading()
    }

    override fun onStart() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.setLoading(true)
            viewModel.getAuthorInfoFollow()
        }
        super.onStart()
    }
    fun initView() {

        viewModel.authorFollowList.observe(viewLifecycleOwner) { value ->

            val rankFollowerAdapter =
                RankFollowerAdapter(
                    value,
                    itemClickListener = { poisition ->
                        val intent = Intent(requireActivity(), AuthorInfoActivity::class.java)
                        intent.putExtra("authorIdx", value[poisition].authorIdx)
                        startActivity(intent)
                    }
                )

            with(binding){
                rankFollowerRV.adapter = rankFollowerAdapter
                rankFollowerRV.layoutManager = GridLayoutManager(activity, 2)
            }

            viewModel.setLoading(false)
        }

    }

    fun setLoading(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loading.observe(viewLifecycleOwner) { value ->
                if (value) {
                    binding.rankFollowerRV.scrollToPosition(0)
                }
            }
        }
    }
}