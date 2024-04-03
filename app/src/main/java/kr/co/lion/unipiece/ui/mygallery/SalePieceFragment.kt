package kr.co.lion.unipiece.ui.mygallery

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentSalePieceBinding
import kr.co.lion.unipiece.databinding.RowSalePieceBinding
import kr.co.lion.unipiece.ui.MainActivity

class SalePieceFragment : Fragment() {

    lateinit var binding: FragmentSalePieceBinding
    lateinit var mainActivity: MainActivity

    var isArtist = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSalePieceBinding.inflate(inflater, container, false)
        mainActivity = activity as MainActivity

        initView()
        settingRecyclerView()

        return binding.root
    }

    fun initView() {
        binding.apply {
            if(isArtist) {
                layoutNotArtist.isVisible = false
            } else {
                layoutArtist.isVisible = false
            }
        }
    }

    fun settingRecyclerView() {
        binding.apply {
            recyclerViewSalePiece.apply {
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
        inner class ViewHolder(rowSalePieceBinding: RowSalePieceBinding) : RecyclerView.ViewHolder(rowSalePieceBinding.root) {
            val rowSalePieceBinding: RowSalePieceBinding

            init {
                this.rowSalePieceBinding = rowSalePieceBinding

                this.rowSalePieceBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val rowSalePieceBinding = RowSalePieceBinding.inflate(layoutInflater)
            val viewHolder = ViewHolder(rowSalePieceBinding)

            return viewHolder
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.rowSalePieceBinding.textViewRowSalePieceName.text = "$position"
        }
    }
}