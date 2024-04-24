package kr.co.lion.unipiece.ui.mygallery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
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

    var isArtist = false

    val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            lifecycleScope.launch {
                viewModel.getPieceAddInfo()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSalePieceBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBarSalePiece.isVisible = true
            } else {
                binding.progressBarSalePiece.isVisible = false
            }
        }

        initView()
    }

    fun initView() {
        binding.apply {
            layoutArtist.isVisible = false
            layoutNotExistPiece.isVisible= false
            layoutNotArtist.isVisible = false

            viewModel.isAuthor.observe(viewLifecycleOwner) { isAuthor ->
                isArtist = isAuthor

                if(isArtist) {
                    layoutArtist.isVisible = true

                    settingButtonSalePieceAddPiece()

                    viewLifecycleOwner.lifecycleScope.launch {
                        repeatOnLifecycle(Lifecycle.State.STARTED) {
                            viewModel.pieceAddInfoList.observe(viewLifecycleOwner, Observer { value ->
                                if(viewModel.pieceAddInfoList.value.isNullOrEmpty()) {
                                    recyclerViewSalePiece.isVisible = false
                                    layoutNotExistPiece.isVisible = true
                                } else {
                                    val salePieceAdapter = SalePieceAdapter(value, activityResultLauncher) { position ->
                                        val addPieceInfo = value[position]
                                        if (addPieceInfo.addPieceState == "판매 완료" || addPieceInfo.addPieceState == "판매 중") {
                                            val intent = Intent(requireActivity(), BuyDetailActivity::class.java)
                                            intent.putExtra("pieceIdx", addPieceInfo.pieceIdx)
                                            intent.putExtra("authorIdx", addPieceInfo.authorIdx)
                                            startActivity(intent)
                                        } else if(addPieceInfo.addPieceState == "판매 승인 거절") {
                                            createSnackBar("판매 승인이 거절된 작품입니다.", Snackbar.LENGTH_SHORT).apply {
                                                val layoutParams = this.view.layoutParams as CoordinatorLayout.LayoutParams
                                                layoutParams.setMargins(20, 0, 20, 50)
                                                this.view.layoutParams = layoutParams
                                            }.show()
                                        } else {
                                            createSnackBar("판매 승인이 완료될 때까지 기다려주세요.", Snackbar.LENGTH_SHORT).apply {
                                                val layoutParams = this.view.layoutParams as CoordinatorLayout.LayoutParams
                                                layoutParams.setMargins(20, 0, 20, 50)
                                                this.view.layoutParams = layoutParams
                                            }.show()
                                        }
                                    }

                                    with(binding){
                                        recyclerViewSalePiece.adapter = salePieceAdapter
                                        recyclerViewSalePiece.layoutManager = LinearLayoutManager(requireActivity())
                                    }
                                }
                            })
                        }
                    }
                } else {
                    progressBarSalePiece.isVisible = false
                    layoutNotArtist.isVisible = true
                    settingButtonSalePieceAddArtist()
                }

            }
        }
    }

    private fun createSnackBar(message: String, duration: Int): Snackbar {
        return Snackbar.make(requireView(), message, duration)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.second))
    }

    fun settingButtonSalePieceAddPiece() {
        binding.apply {
            buttonSalePieceAddPiece.setOnClickListener {
                val intent = Intent(requireActivity(), SalesApplicationActivity::class.java)
                intent.putExtra("isModify", false)
                activityResultLauncher.launch(intent)
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