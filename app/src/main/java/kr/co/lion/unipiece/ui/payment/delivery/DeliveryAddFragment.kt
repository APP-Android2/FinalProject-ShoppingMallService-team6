package kr.co.lion.unipiece.ui.payment.delivery


import android.os.Bundle
import androidx.fragment.app.DialogFragment
import kr.co.lion.unipiece.databinding.DialogDeliveryAddBinding


class DeliveryAddFragment : DialogFragment() {

    lateinit var dialogDeliveryAddBinding: DialogDeliveryAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogDeliveryAddBinding = DialogDeliveryAddBinding.inflate(layoutInflater)

    }
}