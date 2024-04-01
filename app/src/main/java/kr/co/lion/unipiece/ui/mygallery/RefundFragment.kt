package kr.co.lion.unipiece.ui.mygallery

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentPurchasedPieceDetailBinding
import kr.co.lion.unipiece.databinding.FragmentRefundBinding

class RefundFragment : Fragment() {

    lateinit var binding: FragmentRefundBinding
    lateinit var purchasedPieceDetailActivity: PurchasedPieceDetailActivity

    var refundReasonDialogData = arrayOf(
        "작품에 문제가 있어요", "주문하지 않은 작품이 왔어요", "작품을 받지 못했어요"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentRefundBinding.inflate(inflater, container, false)
        purchasedPieceDetailActivity = activity as PurchasedPieceDetailActivity

        settingToolbar()
        settingTextFieldRefundReason()

        return binding.root
    }

    fun settingToolbar() {
        binding.apply {
            toolbarRefund.apply {
                title = "반품 접수"

                setNavigationIcon(R.drawable.back_icon)
            }
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
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(purchasedPieceDetailActivity)
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
}