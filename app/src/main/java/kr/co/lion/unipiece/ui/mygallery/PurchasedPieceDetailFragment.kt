package kr.co.lion.unipiece.ui.mygallery

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentPurchasedPieceDetailBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.util.PurchasedPieceDetailFragmentName
import kr.co.lion.unipiece.util.setMenuIconColor

class PurchasedPieceDetailFragment : Fragment() {

    lateinit var binding: FragmentPurchasedPieceDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentPurchasedPieceDetailBinding.inflate(inflater, container, false)

        settingToolbar()
        settingRefundApprovalView()
        settingButtons()

        return binding.root
    }

    fun settingToolbar() {
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

    fun settingRefundApprovalView() {
        binding.apply {
            linearLayoutRefundApproval.isVisible = false
            dividerRefundApproval.isVisible = false
        }
    }

    fun settingButtons() {
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