package kr.co.lion.unipiece.ui.mygallery

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentPurchasedPieceBinding
import kr.co.lion.unipiece.ui.buy.BuyDetailActivity
import kr.co.lion.unipiece.ui.mygallery.adapter.PurchasedPieceAdapter

class PurchasedPieceFragment : Fragment() {

    lateinit var binding: FragmentPurchasedPieceBinding

    val purchasedPieceAdapter: PurchasedPieceAdapter by lazy {
        PurchasedPieceAdapter { position ->
            startActivity(Intent(requireActivity(), PurchasedPieceDetailActivity::class.java))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPurchasedPieceBinding.inflate(inflater, container, false)

        settingRecyclerView()

        return binding.root
    }

    fun settingRecyclerView() {
        binding.apply {
            recyclerViewPurchasedPiece.apply {
                adapter = purchasedPieceAdapter
                layoutManager = LinearLayoutManager(requireActivity())
                val deco = MaterialDividerItemDecoration(requireActivity(), MaterialDividerItemDecoration.VERTICAL)
                deco.dividerInsetStart = 50
                deco.dividerInsetEnd = 50
                deco.dividerColor = ContextCompat.getColor(requireActivity(), R.color.lightgray)
                addItemDecoration(deco)
            }
        }
    }
}