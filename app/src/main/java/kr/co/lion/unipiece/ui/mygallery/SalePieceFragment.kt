package kr.co.lion.unipiece.ui.mygallery

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentSalePieceBinding
import kr.co.lion.unipiece.ui.author.AddAuthorActivity
import kr.co.lion.unipiece.ui.buy.BuyDetailActivity
import kr.co.lion.unipiece.ui.mygallery.adapter.SalePieceAdapter
import kr.co.lion.unipiece.ui.mygallery.viewmodel.PieceAddInfoViewModel

class SalePieceFragment : Fragment() {

    lateinit var binding: FragmentSalePieceBinding

    private val viewModel: PieceAddInfoViewModel by viewModels()

    var isArtist = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSalePieceBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    fun initView() {
        binding.apply {
            if(isArtist) {
                layoutNotArtist.isVisible = false

                settingButtonSalePieceAddPiece()

                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.pieceAddInfoList.observe(viewLifecycleOwner, Observer { value ->
                            if(viewModel.pieceAddInfoList.value.isNullOrEmpty()) {
                                recyclerViewSalePiece.isVisible = false
                            } else {
                                binding.layoutNotExistPiece.isVisible = false
                                val salePieceAdapter = SalePieceAdapter(value) { position ->
                                    val pieceInfo = value[position]
                                    if (pieceInfo.addPieceState == "판매 완료" || pieceInfo.addPieceState == "판매 중") {
                                        val intent = Intent(requireActivity(), BuyDetailActivity::class.java)
                                        startActivity(intent)
                                    } else if(pieceInfo.addPieceState == "판매 승인 거절") {
                                        Snackbar.make(requireView(), "판매 승인이 거절된 작품입니다.", Snackbar.LENGTH_LONG).show()
                                    } else {
                                        Snackbar.make(requireView(), "판매 승인이 완료될 때까지 기다려주세요.", Snackbar.LENGTH_LONG).show()
                                    }
                                }

                                with(binding){
                                    recyclerViewSalePiece.adapter = salePieceAdapter
                                    recyclerViewSalePiece.layoutManager = LinearLayoutManager(requireActivity())
                                    val deco = MaterialDividerItemDecoration(requireActivity(), MaterialDividerItemDecoration.VERTICAL)
                                    deco.dividerInsetStart = 50
                                    deco.dividerInsetEnd = 50
                                    deco.dividerColor = ContextCompat.getColor(requireActivity(), R.color.lightgray)
                                    recyclerViewSalePiece.addItemDecoration(deco)
                                }
                            }
                        })
                    }
                }
            } else {
                layoutArtist.isVisible = false
                settingButtonSalePieceAddArtist()
            }
        }
    }

    fun settingButtonSalePieceAddPiece() {
        binding.apply {
            buttonSalePieceAddPiece.setOnClickListener {
                val intent = Intent(requireActivity(), SalesApplicationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun settingButtonSalePieceAddArtist() {
        binding.apply {
            buttonSalePieceAddArtist.setOnClickListener {
                val intent = Intent(requireActivity(), AddAuthorActivity::class.java)
                startActivity(intent)
            }
        }
    }
}