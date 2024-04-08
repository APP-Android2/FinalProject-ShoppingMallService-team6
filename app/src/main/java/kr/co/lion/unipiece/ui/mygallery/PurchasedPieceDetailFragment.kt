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
import kr.co.lion.unipiece.ui.home.HomeFragment
import kr.co.lion.unipiece.ui.payment.cart.CartActivity
import kr.co.lion.unipiece.ui.search.SearchFragment
import kr.co.lion.unipiece.util.MainFragmentName
import kr.co.lion.unipiece.util.PurchasedPieceDetailFragmentName
import kr.co.lion.unipiece.util.setMenuIconColor

class PurchasedPieceDetailFragment : Fragment() {

    lateinit var binding: FragmentPurchasedPieceDetailBinding
    lateinit var purchasedPieceDetailActivity: PurchasedPieceDetailActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentPurchasedPieceDetailBinding.inflate(inflater, container, false)
        purchasedPieceDetailActivity = activity as PurchasedPieceDetailActivity

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
                    purchasedPieceDetailActivity.finish()
                }

                inflateMenu(R.menu.menu_home)
                setOnMenuItemClickListener {
                    when(it.itemId) {
                        R.id.menu_home -> {
                            val intent = Intent(requireActivity(), MainActivity::class.java)
                            purchasedPieceDetailActivity.finish()
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
        binding.apply {
            buttonPurchasedPieceDetailOrderCancel.setOnClickListener {
                purchasedPieceDetailActivity.replaceFragment(PurchasedPieceDetailFragmentName.PURCHASE_CANCEL_FRAGEMNT, true)
            }

            buttonPurchasedPieceDetailRefund.setOnClickListener {
                purchasedPieceDetailActivity.replaceFragment(PurchasedPieceDetailFragmentName.REFUND_FRAGMENT, true)
            }
        }
    }
}