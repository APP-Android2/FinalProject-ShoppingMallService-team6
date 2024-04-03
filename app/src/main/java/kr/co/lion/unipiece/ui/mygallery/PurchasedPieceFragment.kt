package kr.co.lion.unipiece.ui.mygallery

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentPurchasedPieceBinding
import kr.co.lion.unipiece.databinding.RowPurchasedPieceBinding
import kr.co.lion.unipiece.ui.MainActivity

class PurchasedPieceFragment : Fragment() {

    lateinit var binding: FragmentPurchasedPieceBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPurchasedPieceBinding.inflate(inflater, container, false)
        mainActivity = activity as MainActivity

        settingRecyclerView()

        return binding.root
    }

    fun settingRecyclerView() {
        binding.apply {
            recyclerViewPurchasedPiece.apply {
                adapter = RecyclerViewAdpater()
                layoutManager = LinearLayoutManager(mainActivity)
                val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
                deco.dividerInsetStart = 50
                deco.dividerInsetEnd = 50
                deco.dividerColor = ContextCompat.getColor(mainActivity, R.color.lightgray)
                addItemDecoration(deco)
            }
        }
    }

    inner class RecyclerViewAdpater : RecyclerView.Adapter<RecyclerViewAdpater.ViewHolder>() {
        inner class ViewHolder(rowPurchasedPieceBinding: RowPurchasedPieceBinding) : RecyclerView.ViewHolder(rowPurchasedPieceBinding.root) {
            val rowPurchasedPieceBinding: RowPurchasedPieceBinding

            init {
                this.rowPurchasedPieceBinding = rowPurchasedPieceBinding

                this.rowPurchasedPieceBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val rowPurchasedPieceBinding = RowPurchasedPieceBinding.inflate(layoutInflater)
            val viewHolder = ViewHolder(rowPurchasedPieceBinding)

            return viewHolder
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.rowPurchasedPieceBinding.textViewRowPurchasedPieceName.text = "$position"
        }
    }

}