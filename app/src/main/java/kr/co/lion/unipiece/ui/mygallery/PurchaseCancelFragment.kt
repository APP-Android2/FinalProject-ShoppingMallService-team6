package kr.co.lion.unipiece.ui.mygallery

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentPurchaseCancelBinding
import kr.co.lion.unipiece.ui.mygallery.viewmodel.PurchaseCancelViewModel
import kr.co.lion.unipiece.util.CustomDialog
import kr.co.lion.unipiece.util.PurchasedPieceDetailFragmentName
import kr.co.lion.unipiece.util.setImage
import java.text.DecimalFormat

class PurchaseCancelFragment : Fragment() {

    lateinit var binding: FragmentPurchaseCancelBinding
    private val viewModel: PurchaseCancelViewModel by viewModels()

    private var pieceIdx = 0
    private var pieceBuyIdx = 0

    private var cancelReasonDialogData = arrayOf(
        "작품이 마음에 들지 않아요", "다른 작품으로 변경하고 싶어요", "배송지를 변경하고 싶어요", "주문을 잘못했어요"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPurchaseCancelBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        pieceIdx = bundle?.getInt("pieceIdx") ?:0
        pieceBuyIdx = bundle?.getInt("pieceBuyIdx") ?:0

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBarPurchaseCancel.isVisible = true
            } else {
                binding.progressBarPurchaseCancel.isVisible = false
            }
        }

        initData()
        initView()
        settingToolbar()
        settingTextFieldPurchaseCancelReason()
        settingButtonPurchaseCancel()
    }

    private fun initData() {
        lifecycleScope.launch {
            viewModel.getPieceBuyInfoByPieceBuyIdx(pieceIdx, pieceBuyIdx)
        }
    }

    private fun settingToolbar() {
        binding.apply {
            toolbarPurchaseCancel.apply {
                title = "주문 취소"

                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    parentFragmentManager.popBackStack(PurchasedPieceDetailFragmentName.PURCHASE_CANCEL_FRAGEMNT.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                }
            }
        }
    }

    private fun initView() {
        val priceFormat = DecimalFormat("###,###")

        viewModel.pieceBuyInfoData.observe(viewLifecycleOwner) { pair ->
            binding.textViewurchaseCancelPieceName.text = pair.second?.pieceName
            binding.textViewPurchaseCancelArtistName.text = pair.second?.authorName
            val piecePrice = priceFormat.format(pair.second?.piecePrice)
            binding.textViewPurchaseCancelPiecePrice.text = "${piecePrice}원"
            val pieceBuyTotalPrice = priceFormat.format(pair.first?.pieceBuyTotalPrice)
            binding.textViewPurchaseCancelTotalPrice.text = "${pieceBuyTotalPrice}원"
            val pieceBuyPrice = priceFormat.format(pair.first?.pieceBuyPrice)
            binding.textViewPurchaseCancelPiecePrice2.text = "${pieceBuyPrice}원"
            val pieceBuySendPrice = priceFormat.format(pair.first?.pieceBuySendPrice)
            binding.textViewPurchaseCancelDeliveryCharge.text = "${pieceBuySendPrice}원"
            binding.textViewPurchaseCancelDiscount.text = "0원"
            binding.textViewPurchaseCancelRefundMethod.text = pair.first?.pieceBuyMethod
            requireActivity().setImage(binding.imageViewPurchaseCancel, pair.second?.pieceImg)
        }
    }

    private fun settingTextFieldPurchaseCancelReason() {
        binding.apply {
            textFieldPurchaseCancelReason.setOnClickListener {
                showCancelReasonDialog()
            }
        }
    }

    private fun showCancelReasonDialog() {
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireActivity(), R.style.Theme_Category_App_MaterialAlertDialog)
        materialAlertDialogBuilder.setTitle("취소 사유")
        materialAlertDialogBuilder.setNegativeButton("취소", null)
        materialAlertDialogBuilder.setItems(cancelReasonDialogData) { dialogInterface: DialogInterface, i: Int ->
            binding.textFieldPurchaseCancelReason.setText(cancelReasonDialogData[i])

            if(binding.textInputLayoutPurchaseCancelReason.isHelperTextEnabled) {
                binding.textInputLayoutPurchaseCancelReason.isHelperTextEnabled = false
            }
        }
        materialAlertDialogBuilder.show()
    }

    private fun settingButtonPurchaseCancel() {
        binding.apply {
            buttonPurchaseCancel.setOnClickListener {
                val dialog = CustomDialog("주문 취소", "취소하면 되돌일 수 없습니다.\n취소하시겠습니까?")

                dialog.setButtonClickListener(object: CustomDialog.OnButtonClickListener{
                    override fun okButtonClick() {
                        parentFragmentManager.popBackStack(PurchasedPieceDetailFragmentName.PURCHASE_CANCEL_FRAGEMNT.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    }

                    override fun noButtonClick() {
                    }
                })

                dialog.show(requireActivity().supportFragmentManager, "CustomDialog")
            }
        }
    }
}