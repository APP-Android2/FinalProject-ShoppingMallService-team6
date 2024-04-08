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
import kr.co.lion.unipiece.databinding.FragmentRankSaleBinding
import kr.co.lion.unipiece.ui.author.AuthorInfoActivity
import kr.co.lion.unipiece.ui.rank.adapter.RankSaleAdapter

class RankSaleFragment : Fragment() {

    lateinit var binding: FragmentRankSaleBinding

    val testAuthorList = arrayListOf(R.drawable.mypage_icon, R.drawable.icon, R.drawable.mypage_icon,
        R.drawable.mypage_icon, R.drawable.test_piece_img, R.drawable.icon,
        R.drawable.logo, R.drawable.test_piece_img, R.drawable.mypage_icon)

    val adapter: RankSaleAdapter by lazy {
        RankSaleAdapter(testAuthorList,
            itemClickListener = {testId ->
                val intent = Intent(requireActivity(), AuthorInfoActivity::class.java)
                startActivity(intent)
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRankSaleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
    }

    private fun setRecyclerView() {
        with(binding) {
            rankSaleRV.adapter = adapter
            rankSaleRV.layoutManager = GridLayoutManager(requireContext(), 2)
            adapter.notifyDataSetChanged()
        }
    }

}