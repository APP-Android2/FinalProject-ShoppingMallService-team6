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
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.FragmentInterestingPieceBinding
import kr.co.lion.unipiece.ui.buy.BuyDetailActivity
import kr.co.lion.unipiece.ui.mygallery.adapter.InterestingPieceAdapter
import kr.co.lion.unipiece.ui.mygallery.viewmodel.InterestingPieceViewModel

class InterestingPieceFragment : Fragment() {

    lateinit var binding: FragmentInterestingPieceBinding
    private val viewModel: InterestingPieceViewModel by viewModels()

    val userIdxPref = UniPieceApplication.prefs.getUserIdx("userIdx", 0)

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_CANCELED) {
            lifecycleScope.launch {
                viewModel.getUserLikedPieceInfo(userIdxPref)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInterestingPieceBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBarInterestingPiece.isVisible = true
            } else {
                binding.progressBarInterestingPiece.isVisible = false
            }
        }

        initView()
    }

    fun initView() {
        binding.recyclerViewInterestingPiece.isVisible = false
        binding.layoutNotExistLiked.isVisible = false

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.likePieceInfoList.observe(viewLifecycleOwner, Observer { value ->
                if (viewModel.likePieceInfoList.value.isNullOrEmpty()) {
                    binding.layoutNotExistLiked.isVisible = true
                } else {
                    binding.recyclerViewInterestingPiece.isVisible = true

                    val interestingPieceAdapter = InterestingPieceAdapter(value,
                        onItemClick = { position ->
                            val likePieceInfo = value[position]
                            val intent = Intent(requireActivity(), BuyDetailActivity::class.java)
                            intent.putExtra("MyGalleryFragment", true)
                            intent.putExtra("pieceIdx", likePieceInfo.pieceIdx)
                            intent.putExtra("authorIdx", likePieceInfo.authorIdx)
                            activityResultLauncher.launch(intent)
                        },
                        onLikeClick = { position ->
                            val likePieceInfo = value[position]
                            lifecycleScope.launch {
                                viewModel.cancelLikePiece(likePieceInfo.pieceIdx, userIdxPref)

                                viewModel.likePieceInfoList.observe(viewLifecycleOwner) { likePieceList ->
                                    if (likePieceList.isNullOrEmpty()) {
                                        binding.recyclerViewInterestingPiece.isVisible = false
                                    }
                                }

                                createSnackBar("관심 작품에서 삭제되었습니다.", Snackbar.LENGTH_SHORT).apply {
                                    val layoutParams = this.view.layoutParams as CoordinatorLayout.LayoutParams
                                    layoutParams.setMargins(20, 0, 20, 50)
                                    this.view.layoutParams = layoutParams
                                }.show()
                            }
                        })

                    with(binding) {
                        recyclerViewInterestingPiece.adapter = interestingPieceAdapter
                        recyclerViewInterestingPiece.layoutManager = LinearLayoutManager(requireActivity())
                    }
                }
            })
        }
    }

    private fun createSnackBar(message: String, duration: Int): Snackbar {
        return Snackbar.make(requireView(), message, duration)
    }
}