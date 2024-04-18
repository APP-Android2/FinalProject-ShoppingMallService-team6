package kr.co.lion.unipiece.ui.infomation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivityInfoOneBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.home.viewModel.PromoteInfoViewModel
import kr.co.lion.unipiece.util.getImageUrlFromName
import kr.co.lion.unipiece.util.setImage
import kr.co.lion.unipiece.util.setMenuIconColor

class InfoOneActivity : AppCompatActivity() {

    lateinit var activityInfoOneBinding: ActivityInfoOneBinding

    val promoteViewModel:PromoteInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityInfoOneBinding = ActivityInfoOneBinding.inflate(layoutInflater)
        initView()
        setEvent()

        setContentView(activityInfoOneBinding.root)
    }

    //툴바 설정
    private fun setEvent(){
        activityInfoOneBinding.apply {
            toolBarInfoOne.apply {

                val promoteImg = intent?.getStringExtra("promoteImg")
                lifecycleScope.launch {
                    val promoteInfo = promoteViewModel.getPromoteInfoByImage(promoteImg?:"")

                    if (promoteInfo?.promoteImg == promoteImg){
                        title = "전시 홍보"
                    }
                }
                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    finish()
                }
                inflateMenu(R.menu.menu_home)
                setMenuIconColor(menu, R.id.menu_home, R.color.second)
                setOnMenuItemClickListener {
                    when(it.itemId) {
                        R.id.menu_home -> {
                            val intent = Intent(this@InfoOneActivity, MainActivity::class.java)
                                .apply{ // MainActivity가 이미 실행 중인 경우 해당 인스턴스를 재사용합니다.
                                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                }

                            intent.putExtra("HomeFragment", true)
                            finish()
                            startActivity(intent)
                        }
                    }

                    true

                }
            }
        }
    }

    //화면 설정
    private fun initView(){
        activityInfoOneBinding.apply {
            val promoteImg = intent?.getStringExtra("promoteImg")

            lifecycleScope.launch {
                val promoteInfo = promoteViewModel.getPromoteInfoByImage(promoteImg?:"")

                if (promoteInfo?.promoteImg == promoteImg){
                    textInfoOneName.text = "행사명 : ${promoteInfo?.promoteName}"
                    textInfoOneDate.text = "일정 : ${promoteInfo?.promoteDate}"
                    textInfoOnePlace.text = "장소 : ${promoteInfo?.promotePlace}"
                    textInfoOneInfo.text = "행사소개: ${promoteInfo?.promoteContent}"
                    textInfoOneCost.text = "참가비 : ${promoteInfo?.promoteMoney}"
                    textInfoOneUrl.text = "참가 신청 주소 : ${promoteInfo?.promoteLink}"

                    val imgUrl = getImageUrlFromName(promoteInfo?.promoteImg?:"")

                    setImage(activityInfoOneBinding.imageViewEventLogo, imgUrl)
                }
            }
        }
    }
}