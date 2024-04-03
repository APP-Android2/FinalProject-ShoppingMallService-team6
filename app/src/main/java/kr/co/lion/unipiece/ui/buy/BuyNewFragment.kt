package kr.co.lion.unipiece.ui.buy

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentBuyNewBinding
import kr.co.lion.unipiece.ui.buy.adapter.BuyNewAdapter
import kr.co.lion.unipiece.ui.buy.adapter.BuyPopAdapter

class BuyNewFragment : Fragment() {

    lateinit var binding: FragmentBuyNewBinding
    lateinit var adapter: BuyNewAdapter

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
    }

    fun initView() {

        val testImageList = arrayListOf(R.drawable.icon, R.drawable.icon, R.drawable.icon,
            R.drawable.icon, R.drawable.icon, R.drawable.icon,
            R.drawable.icon, R.drawable.icon, R.drawable.icon)

        adapter = BuyNewAdapter(testImageList,
            itemClickListener = { testReviewId ->
                Log.d("test", testReviewId.toString())})

        with(binding){
            buyNewRV.adapter = adapter
            buyNewRV.layoutManager = GridLayoutManager(activity, 2)
            adapter.notifyDataSetChanged()
        }
    }
}