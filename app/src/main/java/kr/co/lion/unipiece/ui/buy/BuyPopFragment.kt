package kr.co.lion.unipiece.ui.buy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.databinding.FragmentBuyPopBinding
import kr.co.lion.unipiece.ui.buy.adapter.BuyPopAdapter
import kr.co.lion.unipiece.ui.buy.viewmodel.BuyViewModel

class BuyPopFragment : Fragment() {

    lateinit var binding: FragmentBuyPopBinding

    private val viewModel: BuyViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBuyPopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    fun initView() {

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.popPieceInfoList.observe(viewLifecycleOwner, Observer { value ->
                    val buyPopAdapter = BuyPopAdapter(value,
                        itemClickListener = { pieceIdx ->
                            Log.d("테스트 pieceIdx", pieceIdx.toString())
                            val intent = Intent(requireActivity(), BuyDetailActivity::class.java)
                            startActivity(intent)
                        }
                    )
                with(binding){
                    buyPopRV.adapter = buyPopAdapter
                    buyPopRV.layoutManager = GridLayoutManager(activity, 2)
                }
                })
            }
        }
    }

}