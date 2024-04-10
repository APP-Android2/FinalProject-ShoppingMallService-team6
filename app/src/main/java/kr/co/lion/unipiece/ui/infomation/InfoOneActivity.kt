package kr.co.lion.unipiece.ui.infomation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivityInfoOneBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.util.setMenuIconColor

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

            textInfoOneName.text = "행사명 : UniPiece 이벤트~"
            textInfoOneDate.text = "일정 : 2024 03/31 ~ 2024 04/26"
            textInfoOnePlace.text = "장소 : 멋쟁이 사자처럼"
            textInfoOneInfo.text = "행사소개:\n우리는 UniPiece이고\n대학생들의 작품을 판매하는 어플입니다\n다들 어떠신가요??"
            textInfoOneCost.text = "참가비 : 무료입니다"
            textInfoOneUrl.text = "참가 신청 주소 : https:UniPiece.com"
        }
    }
}