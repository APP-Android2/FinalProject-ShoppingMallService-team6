package kr.co.lion.unipiece.ui.payment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.co.lion.unipiece.databinding.FragmentOrderSuccessBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.mygallery.PurchasedPieceDetailActivity

class OrderSuccessFragment : Fragment() {

    lateinit var binding: FragmentOrderSuccessBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentOrderSuccessBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    fun initView() {
        with(binding) {


            // 결제 내역 보기 버튼 클릭 시 ////////////////////////////////////////////////////
            with(buttonPaymentDetailSee) {
                setOnClickListener {
                    val purchasedIntent =
                        Intent(requireActivity(), PurchasedPieceDetailActivity::class.java)
                    startActivity(purchasedIntent)
                }
            }
            // 계속 쇼핑하기 버튼 클릭 시 /////////////////////////////////////////////////
            with(buttonContinueShopping) {
                setOnClickListener {
                    val buyIntent = Intent(requireActivity(), MainActivity::class.java)
                    val mainActivity = startActivity(buyIntent)


                }
            }
        }
    }
}