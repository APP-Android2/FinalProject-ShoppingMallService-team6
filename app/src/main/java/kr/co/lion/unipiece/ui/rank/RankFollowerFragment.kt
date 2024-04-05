package kr.co.lion.unipiece.ui.rank

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentRankFollowerBinding
import kr.co.lion.unipiece.ui.buy.BuyDetailActivity
import kr.co.lion.unipiece.ui.rank.adapter.RankFollowerAdapter
import kr.co.lion.unipiece.ui.rank.adapter.RankPieceAdapter

class RankFollowerFragment : Fragment() {

    lateinit var binding: FragmentRankFollowerBinding

    val testAuthorList = arrayListOf(R.drawable.mypage_icon, R.drawable.icon, R.drawable.mypage_icon,
        R.drawable.mypage_icon, R.drawable.test_piece_img, R.drawable.icon,
        R.drawable.logo, R.drawable.test_piece_img, R.drawable.mypage_icon)

    val adapter: RankFollowerAdapter by lazy {
        RankFollowerAdapter(testAuthorList,
            itemClickListener = { testId ->
                Log.d("test", testId.toString())
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRankFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
    }

    private fun setRecyclerView() {
        with (binding){
            rankFollowerRV.adapter = adapter
            rankFollowerRV.layoutManager = GridLayoutManager(requireContext(), 2)
            adapter.notifyDataSetChanged()
        }
    }
}