package kr.co.lion.unipiece.ui.payment.delivery


import android.os.Bundle

import androidx.fragment.app.DialogFragment
import kr.co.lion.unipiece.databinding.DialogDeliveryAddBinding


class DeliveryAddFragment : DialogFragment() {

    lateinit var dialogDeliveryAddBinding: DialogDeliveryAddBinding
    lateinit var deliveryActivity: DeliveryActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogDeliveryAddBinding = DialogDeliveryAddBinding.inflate(layoutInflater)
        deliveryActivity = activity as DeliveryActivity

    }
}