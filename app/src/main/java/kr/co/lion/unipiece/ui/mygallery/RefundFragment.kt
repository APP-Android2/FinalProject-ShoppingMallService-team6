package kr.co.lion.unipiece.ui.mygallery

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentRefundBinding
import kr.co.lion.unipiece.model.PieceBuyInfoData
import kr.co.lion.unipiece.ui.mygallery.viewmodel.RefundViewModel
import kr.co.lion.unipiece.util.CustomDialog
import kr.co.lion.unipiece.util.PurchasedPieceDetailFragmentName
import kr.co.lion.unipiece.util.getDegree
import kr.co.lion.unipiece.util.resize
import kr.co.lion.unipiece.util.rotate
import kr.co.lion.unipiece.util.setImage
import java.text.DecimalFormat

class RefundFragment : Fragment() {

    lateinit var binding: FragmentRefundBinding
    private val viewModel: RefundViewModel by viewModels()

    lateinit var albumLauncher: ActivityResultLauncher<Intent>

    private var pieceIdx = 0
    private var pieceBuyIdx = 0


    var refundReasonDialogData = arrayOf(
        "작품에 문제가 있어요", "주문하지 않은 작품이 왔어요", "작품을 받지 못했어요"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRefundBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pieceIdx = arguments?.getInt("pieceIdx", -1) ?: -1
        pieceBuyIdx = arguments?.getInt("pieceBuyIdx", -1) ?: -1

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBarRefund.isVisible = true
            } else {
                binding.progressBarRefund.isVisible = false
            }
        }

        initData()
        initView()
        settingToolbar()
        settingTextFieldRefundReason()
        settingButtonRefund()
    }

    private fun initData() {
        lifecycleScope.launch {
            viewModel.getPieceBuyInfoByPieceBuyIdx(pieceIdx, pieceBuyIdx)
        }
    }

    fun settingToolbar() {
        binding.apply {
            toolbarRefund.apply {
                title = "반품 접수"

                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    parentFragmentManager.popBackStack(PurchasedPieceDetailFragmentName.REFUND_FRAGMENT.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                }
            }
        }
    }

    private fun initView() {
        binding.textViewRefundImageError.isVisible = false
        binding.imageViewRefundReason.isVisible = false

        val priceFormat = DecimalFormat("###,###")

        viewModel.pieceBuyInfoData.observe(viewLifecycleOwner) { pair ->
            binding.textViewRefundPieceName.text = pair.second?.pieceName
            binding.textViewRefundArtistName.text = pair.second?.authorName
            val piecePrice = priceFormat.format(pair.second?.piecePrice)
            binding.textViewRefundPiecePrice.text = "${piecePrice}원"
            val pieceBuyTotalPrice = priceFormat.format(pair.first?.pieceBuyTotalPrice)
            binding.textViewRefundTotalPrice.text = "${pieceBuyTotalPrice}원"
            val pieceBuyPrice = priceFormat.format(pair.first?.pieceBuyPrice)
            binding.textViewRefundPiecePrice2.text = "${pieceBuyPrice}원"
            val pieceBuySendPrice = priceFormat.format(pair.first?.pieceBuySendPrice)
            binding.textViewRefundDeliveryCharge.text = "${pieceBuySendPrice}원"
            binding.textViewRefundDiscount.text = "0원"
            binding.textViewRefundMethod.text = pair.first?.pieceBuyMethod
            requireActivity().setImage(binding.imageViewRefundPiece, pair.second?.pieceImg)
        }
    }

    fun settingTextFieldRefundReason() {
        binding.apply {
            textFieldRefundReason.setOnClickListener {
                showRefundReasonDialog()
            }
        }
    }

    fun showRefundReasonDialog() {
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireActivity(), R.style.Theme_Category_App_MaterialAlertDialog)
        materialAlertDialogBuilder.setTitle("반품 사유")
        materialAlertDialogBuilder.setNegativeButton("취소", null)
        materialAlertDialogBuilder.setItems(refundReasonDialogData) { dialogInterface: DialogInterface, i: Int ->
            binding.textFieldRefundReason.setText(refundReasonDialogData[i])

            if(binding.textInputLayoutRefundReason.isHelperTextEnabled) {
                binding.textInputLayoutRefundReason.isHelperTextEnabled = false
            }
        }
        materialAlertDialogBuilder.show()
    }

    fun settingButtonRefund() {
        binding.apply {
            buttonRefund.setOnClickListener {


            }
        }
    }
}