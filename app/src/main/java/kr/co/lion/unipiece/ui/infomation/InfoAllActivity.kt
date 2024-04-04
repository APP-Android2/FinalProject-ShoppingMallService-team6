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

    val infoAllAdapter:InfoAllAdapter by lazy {
        InfoAllAdapter()
    }
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
                adapter = infoAllAdapter
                layoutManager = LinearLayoutManager(this@InfoAllActivity)
            }
        }
    }
}