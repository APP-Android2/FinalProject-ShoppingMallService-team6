package kr.co.lion.unipiece.ui.rank

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentRankPieceBinding
import kr.co.lion.unipiece.ui.buy.BuyDetailActivity
import kr.co.lion.unipiece.ui.rank.adapter.RankPieceAdapter

class RankPieceFragment : Fragment() {

    lateinit var binding: FragmentRankPieceBinding

    val testPieceList = arrayListOf(R.drawable.logo, R.drawable.icon, R.drawable.test_piece_img,
        R.drawable.icon, R.drawable.test_piece_img, R.drawable.icon,
        R.drawable.logo, R.drawable.test_piece_img, R.drawable.logo)

    val adapter: RankPieceAdapter by lazy {
        RankPieceAdapter(testPieceList,
            itemClickListener = { testId ->
                startActivity(Intent(requireActivity(), BuyDetailActivity::class.java))
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRankPieceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
    }

    fun setRecyclerView() {
        with (binding){
            rankPieceRV.adapter = adapter
            rankPieceRV.layoutManager = GridLayoutManager(requireContext(), 2)
            adapter.notifyDataSetChanged()
        }
    }
}