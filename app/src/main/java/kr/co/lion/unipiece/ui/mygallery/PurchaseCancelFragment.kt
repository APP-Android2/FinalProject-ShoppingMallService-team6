package kr.co.lion.unipiece.ui.mygallery

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentPurchaseCancelBinding
import kr.co.lion.unipiece.databinding.FragmentPurchasedPieceDetailBinding
import kr.co.lion.unipiece.util.PurchasedPieceDetailFragmentName

class PurchaseCancelFragment : Fragment() {

    lateinit var binding: FragmentPurchaseCancelBinding
    lateinit var purchasedPieceDetailActivity: PurchasedPieceDetailActivity

    var cancelReasonDialogData = arrayOf(
        "작품이 마음에 들지 않아요", "다른 작품으로 변경하고 싶어요", "배송지를 변경하고 싶어요", "주문을 잘못했어요"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentPurchaseCancelBinding.inflate(inflater, container, false)
        purchasedPieceDetailActivity = activity as PurchasedPieceDetailActivity

        settingToolbar()
        settingTextFieldPurchaseCancelReason()

        return binding.root
    }

    fun settingToolbar() {
        binding.apply {
            toolbarPurchaseCancel.apply {
                title = "구매 취소"

                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    purchasedPieceDetailActivity.removeFragment(PurchasedPieceDetailFragmentName.PURCHASE_CANCEL_FRAGEMNT)
                }
            }
        }
    }

    fun settingTextFieldPurchaseCancelReason() {
        binding.apply {
            textFieldPurchaseCancelReason.setOnClickListener {
                showCancelReasonDialog()
            }
        }
    }

    fun showCancelReasonDialog() {
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(purchasedPieceDetailActivity)
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
}