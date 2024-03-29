package kr.co.lion.unipiece.ui.mygallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentPurchasedPieceDetailBinding
import kr.co.lion.unipiece.databinding.FragmentRefundBinding

class RefundFragment : Fragment() {

    lateinit var binding: FragmentRefundBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentRefundBinding.inflate(inflater, container, false)

        settingToolbar()

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
}