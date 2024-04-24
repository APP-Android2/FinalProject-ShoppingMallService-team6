package kr.co.lion.unipiece.ui.mygallery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.FragmentPurchasedPieceBinding
import kr.co.lion.unipiece.ui.mygallery.adapter.PurchasedPieceAdapter
import kr.co.lion.unipiece.ui.mygallery.viewmodel.PurchasedPieceViewModel

class PurchasedPieceFragment : Fragment() {

    lateinit var binding: FragmentPurchasedPieceBinding
    private val viewModel: PurchasedPieceViewModel by viewModels()

    val userIdxPref = UniPieceApplication.prefs.getUserIdx("userIdx", 0)

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_CANCELED) {
            lifecycleScope.launch {
                viewModel.getPieceBuyInfo(userIdxPref)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPurchasedPieceBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBarPurchasedPiece.isVisible = true
            } else {
                binding.progressBarPurchasedPiece.isVisible = false
            }
        }

        initView()
    }

    fun initView() {
        binding.recyclerViewPurchasedPiece.isVisible = false
        binding.layoutNotExistPurchasedPiece.isVisible = false

        viewModel.pieceBuyInfoList.observe(viewLifecycleOwner, Observer { value ->
            if (viewModel.pieceBuyInfoList.value.isNullOrEmpty()) {
                binding.layoutNotExistPurchasedPiece.isVisible = true
            } else {
                binding.recyclerViewPurchasedPiece.isVisible = true

                val purchasedPieceAdapter = PurchasedPieceAdapter(value) { position ->
                    val (pieceBuyInfo, _) = value[position]
                    val intent = Intent(requireActivity(), PurchasedPieceDetailActivity::class.java)
                    intent.putExtra("pieceIdx", pieceBuyInfo.pieceIdx)
                    intent.putExtra("pieceBuyIdx", pieceBuyInfo.pieceBuyIdx)
                    intent.putExtra("pieceBuyState", pieceBuyInfo.pieceBuyState)
                    activityResultLauncher.launch(intent)
                }

                with(binding) {
                    recyclerViewPurchasedPiece.adapter = purchasedPieceAdapter
                    recyclerViewPurchasedPiece.layoutManager = LinearLayoutManager(requireActivity())
                }
            }
        })
    }
}