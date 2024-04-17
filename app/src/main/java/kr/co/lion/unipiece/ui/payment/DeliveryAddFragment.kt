package kr.co.lion.unipiece.ui.payment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.DialogDeliveryAddBinding
import kr.co.lion.unipiece.model.DeliveryData
import kr.co.lion.unipiece.ui.payment.viewmodel.DeliveryViewModel


class DeliveryAddFragment : DialogFragment() {

    lateinit var binding: DialogDeliveryAddBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        binding = DialogDeliveryAddBinding.inflate(inflater, container, false)

        return binding.root
    }





}
