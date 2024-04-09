package kr.co.lion.unipiece.ui.mygallery

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentRefundBinding
import kr.co.lion.unipiece.util.CustomDialog
import kr.co.lion.unipiece.util.PurchasedPieceDetailFragmentName

class RefundFragment : Fragment() {

    lateinit var binding: FragmentRefundBinding
    lateinit var supportFragmentManager: FragmentManager

    var refundReasonDialogData = arrayOf(
        "작품에 문제가 있어요", "주문하지 않은 작품이 왔어요", "작품을 받지 못했어요"
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        supportFragmentManager = parentFragmentManager
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentRefundBinding.inflate(inflater, container, false)

        settingToolbar()
        settingTextFieldRefundReason()
        settingButtonRefund()

        return binding.root
    }

    fun settingToolbar() {
        binding.apply {
            toolbarRefund.apply {
                title = "반품 접수"

                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    supportFragmentManager.popBackStack(PurchasedPieceDetailFragmentName.REFUND_FRAGMENT.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                }
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
                val dialog = CustomDialog("반품 접수", "반품 접수 신청 시 되돌릴 수 없습니다.\n접수하시겠습니까?\n\n※ 반품 접수 승인 완료까지 1~2일 소요됩니다.")

                dialog.setButtonClickListener(object: CustomDialog.OnButtonClickListener{
                    override fun okButtonClick() {
                        supportFragmentManager.popBackStack(PurchasedPieceDetailFragmentName.REFUND_FRAGMENT.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    }

                    override fun noButtonClick() {
                    }
                })

                dialog.show(requireActivity().supportFragmentManager, "CustomDialog")
            }
        }
    }
}