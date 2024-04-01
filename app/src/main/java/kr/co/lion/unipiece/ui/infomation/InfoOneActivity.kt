package kr.co.lion.unipiece.ui.infomation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivityInfoOneBinding
import kr.co.lion.unipiece.ui.MainActivity

class InfoOneActivity : AppCompatActivity() {

    lateinit var activityInfoOneBinding: ActivityInfoOneBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityInfoOneBinding = ActivityInfoOneBinding.inflate(layoutInflater)
        initView()
        setContentView(activityInfoOneBinding.root)
    }

    //툴바 설정
    private fun initView(){
        activityInfoOneBinding.apply {
            toolBarInfoOne.apply {
                title = "전시 홍보"
                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    finish()
                }
                inflateMenu(R.menu.menu_home)
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_home -> {
                            startActivity(Intent(this@InfoOneActivity, MainActivity::class.java))
                        }
                    }

                    true
                }
            }

            textInfoOneName.text = "행사명 : UniPiece 이벤트~"
            textInfoOneDate.text = "일정 : 2024 03/31 ~ 2024 04/26"
            textInfoOnePlace.text = "장소 : 멋쟁이 사자처럼"
            textInfoOneInfo.text = "행사소개:\n우리는 UniPiece이고\n대학생들의 작품을 판매하는 어플입니다\n다들 어떠신가요??"
            textInfoOneCost.text = "참가비 : 무료입니다"
            textInfoOneUrl.text = "참가 신청 주소 : https:UniPiece.com"
        }
    }
}