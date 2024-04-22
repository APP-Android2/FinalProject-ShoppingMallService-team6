package kr.co.lion.unipiece.ui.rank

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.databinding.FragmentRankPieceBinding
import kr.co.lion.unipiece.ui.buy.BuyDetailActivity
import kr.co.lion.unipiece.ui.rank.adapter.RankPieceAdapter
import kr.co.lion.unipiece.ui.rank.viewmodel.RankViewModel

class RankPieceFragment : Fragment() {

    lateinit var binding: FragmentRankPieceBinding

    private val viewModel: RankViewModel by viewModels( ownerProducer = { requireParentFragment() } )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRankPieceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setLoading()
    }

    fun initView() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getPopPieceInfo()
            viewModel.setLoading(false)
        }

        viewModel.pieceRankList.observe(viewLifecycleOwner) { value ->

            val rankPieceAdapter =
                RankPieceAdapter(
                    value,
                    itemClickListener = { poisition ->
                        val intent = Intent(requireActivity(), BuyDetailActivity::class.java)
                        intent.putExtra("pieceIdx", value[poisition].pieceIdx)
                        intent.putExtra("authorIdx", value[poisition].authorIdx)
                        startActivity(intent)
                    }
                )

            with(binding){
                rankPieceRV.adapter = rankPieceAdapter
                rankPieceRV.layoutManager = GridLayoutManager(activity, 2)
            }


        }

    }

    fun setLoading(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loading.observe(viewLifecycleOwner) { value ->
                if (value) {
                    binding.rankPieceRV.scrollToPosition(0)
                }
            }
        }
    }
}