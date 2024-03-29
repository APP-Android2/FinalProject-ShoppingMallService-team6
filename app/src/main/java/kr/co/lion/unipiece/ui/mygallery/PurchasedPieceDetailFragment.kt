package kr.co.lion.unipiece.ui.mygallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentPurchasedPieceDetailBinding
import kr.co.lion.unipiece.util.setMenuIconColor

class PurchasedPieceDetailFragment : Fragment() {

    lateinit var binding: FragmentPurchasedPieceDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentPurchasedPieceDetailBinding.inflate(inflater, container, false)

        settingToolbar()
        settingRefundApprovalView()

        return binding.root
    }

    fun settingToolbar() {
        binding.apply {
            toolbarPurchasedPieceDetail.apply {
                title = "구매한 작품 상세보기"

                setNavigationIcon(R.drawable.back_icon)

                inflateMenu(R.menu.menu_home)

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
}