package kr.co.lion.unipiece.ui.author

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivityUpdateAuthorBinding

class UpdateAuthorActivity : AppCompatActivity() {

    lateinit var activityUpdateAuthorBinding: ActivityUpdateAuthorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityUpdateAuthorBinding = ActivityUpdateAuthorBinding.inflate(layoutInflater)
        setContentView(activityUpdateAuthorBinding.root)
        initView()
    }

    private fun initView(){
        activityUpdateAuthorBinding.apply {
            toolBarUpdate.apply {
                title = "작가 갱신"
                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {

                }
            }
        }
    }
}