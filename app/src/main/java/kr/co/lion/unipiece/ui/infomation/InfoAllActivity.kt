package kr.co.lion.unipiece.ui.infomation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivityInfoAllBinding
import kr.co.lion.unipiece.databinding.InfoAllBinding
import kr.co.lion.unipiece.ui.infomation.viewModel.InfoAllViewModel
import kr.co.lion.unipiece.util.setMenuIconColor

class InfoAllActivity : AppCompatActivity() {

    lateinit var activityInfoAllBinding: ActivityInfoAllBinding

    val viewModel:InfoAllViewModel by viewModels()

    val infoAllAdapter:InfoAllAdapter by lazy {
        val adapter = InfoAllAdapter(emptyList())
        adapter.setRecyclerviewClickListener(object : InfoAllAdapter.ItemOnClickListener{
            override fun recyclerviewClickListener() {
                startActivity(Intent(this@InfoAllActivity, InfoOneActivity::class.java))
            }
        })
        adapter
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityInfoAllBinding = ActivityInfoAllBinding.inflate(layoutInflater)
        setContentView(activityInfoAllBinding.root)
        initView()
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

    private fun initView(){
        lifecycleScope.launch {
            viewModel.promoteInfoList.observe(this@InfoAllActivity) { value ->
                infoAllAdapter.updateData(value)
            }
        }
    }
}