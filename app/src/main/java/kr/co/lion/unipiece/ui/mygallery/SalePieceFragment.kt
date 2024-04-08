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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentSalePieceBinding
import kr.co.lion.unipiece.databinding.RowSalePieceBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.buy.BuyDetailActivity
import kr.co.lion.unipiece.ui.mygallery.adapter.SalePieceAdapter

class SalePieceFragment : Fragment() {

    lateinit var binding: FragmentSalePieceBinding

    val salePieceAdapter: SalePieceAdapter by lazy {
        SalePieceAdapter { position ->
            val intent = Intent(requireActivity(), BuyDetailActivity::class.java)
            intent.putExtra("MyGalleryFragment", true)
            startActivity(intent)
        }
    }

    var isArtist = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSalePieceBinding.inflate(inflater, container, false)

        initView()
        settingButtonSalePieceAddPiece()

        return binding.root
    }

    fun initView() {
        binding.apply {
            if(isArtist) {
                layoutNotArtist.isVisible = false
                settingRecyclerView()
            } else {
                layoutArtist.isVisible = false
            }
        }
    }

    fun settingButtonSalePieceAddPiece() {
        binding.apply {
            buttonSalePieceAddPiece.setOnClickListener {
                startActivity(Intent(requireActivity(), SalesApplicationActivity::class.java))
            }
        }
    }

    fun settingRecyclerView() {
        binding.apply {
            recyclerViewSalePiece.apply {
                adapter = salePieceAdapter
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