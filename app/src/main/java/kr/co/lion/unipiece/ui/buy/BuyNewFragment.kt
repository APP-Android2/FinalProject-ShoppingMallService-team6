package kr.co.lion.unipiece.ui.buy

import android.content.Intent
import android.os.Bundle
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
import kr.co.lion.unipiece.databinding.FragmentBuyNewBinding
import kr.co.lion.unipiece.ui.buy.adapter.BuyNewAdapter
import kr.co.lion.unipiece.ui.buy.viewmodel.BuyViewModel

class BuyNewFragment : Fragment() {

    lateinit var binding: FragmentBuyNewBinding

    private val viewModel: BuyViewModel by viewModels( ownerProducer = { requireParentFragment()} )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBuyNewBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setLoading()
    }

    fun initView() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.newPieceInfoList.observe(viewLifecycleOwner) { value ->
                val buyNewAdapter  = BuyNewAdapter(
                        value,
                        itemClickListener = { position ->
                            val intent = Intent(requireActivity(), BuyDetailActivity::class.java)
                            intent.putExtra("pieceIdx", value[position].pieceIdx)
                            intent.putExtra("authorIdx", value[position].authorIdx)
                            startActivity(intent)
                        }
                    )

                with(binding){
                    buyNewRV.adapter = buyNewAdapter
                    buyNewRV.layoutManager = GridLayoutManager(activity, 2)
                }

                binding.progressBar.visibility = View.GONE
            }
        }
    }

    fun setLoading(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.newLoading.observe(viewLifecycleOwner) { value ->
                if (value) {
                    binding.buyNewRV.scrollToPosition(0)
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }
}