package kr.co.lion.unipiece.ui.mygallery

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentPurchasedPieceDetailBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.mygallery.viewmodel.PurchasedPieceDetailViewModel
import kr.co.lion.unipiece.util.PurchasedPieceDetailFragmentName
import kr.co.lion.unipiece.util.setImage
import kr.co.lion.unipiece.util.setMenuIconColor
import java.text.DecimalFormat

class PurchasedPieceDetailFragment : Fragment() {

    lateinit var binding: FragmentPurchasedPieceDetailBinding
    private val viewModel: PurchasedPieceDetailViewModel by viewModels()

    private var pieceIdx = 0
    private var pieceBuyIdx = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPurchasedPieceDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pieceIdx = arguments?.getInt("pieceIdx", -1) ?: -1
        pieceBuyIdx = arguments?.getInt("pieceBuyIdx", -1) ?: -1

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBarPurchasedPieceDetail.isVisible = true
            } else {
                binding.progressBarPurchasedPieceDetail.isVisible = false
            }
        }

        initView()
        settingPieceBuyInfo()
        settingToolbar()
        settingRefundApprovalView()
        settingButtons()
    }

    private fun initView() {
        lifecycleScope.launch {
            viewModel.getPieceBuyInfoByPieceBuyIdx(pieceIdx, pieceBuyIdx)
        }
    }

    private fun settingToolbar() {
        binding.apply {
            toolbarPurchasedPieceDetail.apply {
                title = "구매한 작품 상세보기"

                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    requireActivity().finish()
                }

                inflateMenu(R.menu.menu_home)
                setOnMenuItemClickListener {
                    when(it.itemId) {
                        R.id.menu_home -> {
                            val intent = Intent(requireActivity(), MainActivity::class.java)
                                .apply{ // MainActivity가 이미 실행 중인 경우 해당 인스턴스를 재사용합니다.
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            }

                            intent.putExtra("HomeFragment", true)
                            requireActivity().finish()
                            startActivity(intent)
                        }
                    }

                    true
                }

                requireContext().setMenuIconColor(menu, R.id.menu_home, R.color.second)
            }
        }
    }

    private fun settingPieceBuyInfo() {
        val priceFormat = DecimalFormat("###,###")

        viewModel.pieceBuyInfoData.observe(viewLifecycleOwner) { pair ->
            binding.textViewPurchasedPieceDetailState.text = pair.first?.pieceBuyState
            binding.textViewPurchasedPieceDetailPieceName.text = pair.second?.pieceName
            binding.textViewPurchasedPieceDetailArtistName.text = pair.second?.authorName
            val piecePrice = priceFormat.format(pair.second?.piecePrice)
            binding.textViewPurchasedPieceDetailPiecePrice.text = "${piecePrice}원"
            binding.textViewPurchasedPieceDetailRecipientName.text = pair.first?.pieceBuyName
            binding.textViewPurchasedPieceDetailPhoneNumber.text = pair.first?.pieceBuyPhone
            binding.textViewPurchasedPieceDetailAddress.text = pair.first?.pieceBuyAddress
            binding.textViewPurchasedPieceDetailDeliveryMemo.text = pair.first?.pieceBuyMemo
            binding.textViewPurchasedPieceDetailCourierName.text = pair.first?.pieceBuyCo
            binding.textViewPurchasedPieceDetailInvoiceNumber.text = pair.first?.pieceBuySendNum
            val pieceBuyPrice = priceFormat.format(pair.first?.pieceBuyPrice)
            binding.textViewPurchasedPieceDetailPiecePrice2.text = "${pieceBuyPrice}원"
            val pieceBuySendPrice = priceFormat.format(pair.first?.pieceBuySendPrice)
            binding.textViewPurchasedPieceDetailDeliveryCharge.text = "${pieceBuySendPrice}원"
            binding.textViewPurchasedPieceDetailDiscount.text = "0원"
            val pieceBuyTotalPrice = priceFormat.format(pair.first?.pieceBuyTotalPrice)
            binding.textViewPurchasedPieceDetailPayment.text = "${pieceBuyTotalPrice}원"
            binding.textViewPurchasedPieceDetailPaymentMethod.text = pair.first?.pieceBuyMethod
            binding.textViewPurchasedPieceDetailPaymentMethodPrice.text = "${pieceBuyTotalPrice}원"
            requireActivity().setImage(binding.imageViewPurchasedPieceDetail, pair.second?.pieceImg)
        }
    }

    private fun settingRefundApprovalView() {
        binding.apply {
            linearLayoutRefundApproval.isVisible = false
            dividerRefundApproval.isVisible = false
        }
    }

    private fun settingButtons() {
        val supportFragmentManager = parentFragmentManager.beginTransaction()
        binding.apply {
            buttonPurchasedPieceDetailOrderCancel.setOnClickListener {
            supportFragmentManager.replace(R.id.containerPurchasedPieceDetail, PurchaseCancelFragment())
                .addToBackStack(PurchasedPieceDetailFragmentName.PURCHASE_CANCEL_FRAGEMNT.str)
                .commit()
            }

            buttonPurchasedPieceDetailRefund.setOnClickListener {
                supportFragmentManager.replace(R.id.containerPurchasedPieceDetail, RefundFragment())
                    .addToBackStack(PurchasedPieceDetailFragmentName.REFUND_FRAGMENT.str)
                    .commit()
            }
        }
    }
}