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
import androidx.fragment.app.FragmentManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentPurchaseCancelBinding
import kr.co.lion.unipiece.util.CustomDialog
import kr.co.lion.unipiece.util.PurchasedPieceDetailFragmentName

class PurchaseCancelFragment : Fragment() {

    lateinit var binding: FragmentPurchaseCancelBinding

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

        settingToolbar()
        settingTextFieldPurchaseCancelReason()
        settingButtonPurchaseCancel()
    }

    fun settingToolbar() {
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

    fun settingTextFieldPurchaseCancelReason() {
        binding.apply {
            textFieldPurchaseCancelReason.setOnClickListener {
                showCancelReasonDialog()
            }
        }
    }

    fun showCancelReasonDialog() {
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

    fun settingButtonPurchaseCancel() {
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