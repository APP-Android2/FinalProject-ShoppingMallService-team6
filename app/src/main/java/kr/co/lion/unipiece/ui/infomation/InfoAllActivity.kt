package kr.co.lion.unipiece.ui.infomation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivityInfoAllBinding
import kr.co.lion.unipiece.databinding.InfoAllBinding
import kr.co.lion.unipiece.util.setMenuIconColor

class InfoAllActivity : AppCompatActivity() {

    lateinit var activityInfoAllBinding: ActivityInfoAllBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityInfoAllBinding = ActivityInfoAllBinding.inflate(layoutInflater)
        setContentView(activityInfoAllBinding.root)
        settingToolBar()
        settingAdapter()
    }

    //툴바
    private fun settingToolBar(){
        activityInfoAllBinding.apply {
            toolBarInfoAll.apply {
                title = "소식 전체 보기"
                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    finish()
                }
                inflateMenu(R.menu.menu_home)
                setMenuIconColor(menu, R.id.menu_home, R.color.second)
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_home -> {
                            finish()
                        }
                    }

                    true
                }
            }
        }
    }


    private fun settingAdapter(){
        activityInfoAllBinding.apply {
            recyclerviewInfoAll.apply {
                adapter = RecyclerviewAdapter()
                layoutManager = LinearLayoutManager(this@InfoAllActivity)
            }
        }
    }


    inner class RecyclerviewAdapter:RecyclerView.Adapter<RecyclerviewAdapter.ViewHolderClass>(){

        inner class ViewHolderClass(infoAllBinding: InfoAllBinding):RecyclerView.ViewHolder(infoAllBinding.root){
            var infoAllBinding :InfoAllBinding

            init {
                this.infoAllBinding = infoAllBinding
                this.infoAllBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
            var infoAllBinding = InfoAllBinding.inflate(layoutInflater)
            var viewHolder = ViewHolderClass(infoAllBinding)
            return viewHolder
        }

        override fun getItemCount(): Int {
            return 30
        }

        override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
            holder.infoAllBinding.imageView2.setImageResource(R.drawable.icon)
            holder.infoAllBinding.textInfoAllTitle.text = "UniPiece"
            holder.infoAllBinding.textInfoAllDate.text = "2024-04-01 ~ 2024-04-26"
            holder.infoAllBinding.textInfoAllAuthorName.text = "멋쟁이 사람들"
            holder.infoAllBinding.root.setOnClickListener {
                startActivity(Intent(this@InfoAllActivity, InfoOneActivity::class.java))
            }
        }
    }
}