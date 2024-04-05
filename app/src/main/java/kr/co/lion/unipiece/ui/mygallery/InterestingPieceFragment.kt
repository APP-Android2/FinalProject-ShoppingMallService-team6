package kr.co.lion.unipiece.ui.mygallery

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentInterestingPieceBinding
import kr.co.lion.unipiece.databinding.RowInterestingPieceBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.buy.BuyDetailActivity
import kr.co.lion.unipiece.ui.mygallery.adapter.InterestingPieceAdapter

class InterestingPieceFragment : Fragment() {

    lateinit var binding: FragmentInterestingPieceBinding

    val interestingPieceAdapter: InterestingPieceAdapter by lazy {
        InterestingPieceAdapter { position ->
            startActivity(Intent(requireActivity(), BuyDetailActivity::class.java))
        }
    }

    var isLikedPieceExist = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInterestingPieceBinding.inflate(inflater, container, false)

        settingRecyclerView()

        return binding.root
    }

    fun settingRecyclerView() {
        binding.apply {
            recyclerViewInterestingPiece.apply {
                adapter = interestingPieceAdapter
                layoutManager = LinearLayoutManager(requireActivity())
                val deco = MaterialDividerItemDecoration(
                    requireActivity(),
                    MaterialDividerItemDecoration.VERTICAL
                )
                deco.dividerInsetStart = 50
                deco.dividerInsetEnd = 50
                deco.dividerColor = ContextCompat.getColor(requireActivity(), R.color.lightgray)
                addItemDecoration(deco)
            }
        }
    }
}