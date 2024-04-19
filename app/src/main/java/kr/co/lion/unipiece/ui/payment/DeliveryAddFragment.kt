package kr.co.lion.unipiece.ui.payment


import android.os.Bundle
import androidx.fragment.app.DialogFragment
import kr.co.lion.unipiece.databinding.FragmentDeliveryCustomDialogBinding


class DeliveryAddFragment : DialogFragment() {

    lateinit var binding: FragmentDeliveryCustomDialogBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentDeliveryCustomDialogBinding.inflate(layoutInflater)


    }
}