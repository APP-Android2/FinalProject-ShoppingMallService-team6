package kr.co.lion.unipiece.ui.buy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentBuyPopBinding
import kr.co.lion.unipiece.ui.buy.adapter.BuyPopAdapter

class BuyPopFragment : Fragment() {

    lateinit var binding: FragmentBuyPopBinding
    lateinit var adapter: BuyPopAdapter
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

        val testImageList = arrayListOf(R.drawable.icon, R.drawable.icon, R.drawable.icon,
            R.drawable.icon, R.drawable.icon, R.drawable.icon,
            R.drawable.icon, R.drawable.icon, R.drawable.icon)

        adapter = BuyPopAdapter(testImageList,
            itemClickListener = { testReviewId ->
                val intent = Intent(requireActivity(), BuyDetailActivity::class.java)
                intent.putExtra("BuyFragment", true)
                startActivity(intent)
            }
        )

        with(binding){
            buyPopRV.adapter = adapter
            buyPopRV.layoutManager = GridLayoutManager(activity, 2)
            adapter.notifyDataSetChanged()
        }
    }

}