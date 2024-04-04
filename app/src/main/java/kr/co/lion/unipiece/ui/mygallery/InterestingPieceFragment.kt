package kr.co.lion.unipiece.ui.mygallery

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentInterestingPieceBinding
import kr.co.lion.unipiece.databinding.RowInterestingPieceBinding
import kr.co.lion.unipiece.ui.MainActivity

class InterestingPieceFragment : Fragment() {

    lateinit var binding: FragmentInterestingPieceBinding
    lateinit var mainActivity: MainActivity

    var isLikedPieceExist = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInterestingPieceBinding.inflate(inflater, container, false)
        mainActivity = activity as MainActivity

        settingRecyclerView()

        return binding.root
    }

    fun settingRecyclerView() {
        binding.apply {
            recyclerViewInterestingPiece.apply {
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
        inner class ViewHolder(rowInterestingPieceBinding: RowInterestingPieceBinding) : RecyclerView.ViewHolder(rowInterestingPieceBinding.root) {
            val rowInterrstingPieceBinding: RowInterestingPieceBinding

            init {
                this.rowInterrstingPieceBinding = rowInterestingPieceBinding

                this.rowInterrstingPieceBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val rowInterrstingPieceBinding = RowInterestingPieceBinding.inflate(layoutInflater)
            val viewHolder = ViewHolder(rowInterrstingPieceBinding)

            return viewHolder
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.rowInterrstingPieceBinding.textViewRowInterestingPieceName.text = "$position"
        }
    }
}