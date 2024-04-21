package kr.co.lion.unipiece.ui.infomation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivityInfoOneBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.home.viewModel.GalleryInfoViewModel
import kr.co.lion.unipiece.ui.home.viewModel.NewsInfoViewModel
import kr.co.lion.unipiece.ui.home.viewModel.PromoteInfoViewModel
import kr.co.lion.unipiece.util.getImageUrlFromGallery
import kr.co.lion.unipiece.util.getImageUrlFromName
import kr.co.lion.unipiece.util.getImageUrlFromNews
import kr.co.lion.unipiece.util.setImage
import kr.co.lion.unipiece.util.setMenuIconColor

class InfoOneActivity : AppCompatActivity() {

    lateinit var activityInfoOneBinding: ActivityInfoOneBinding

    val promoteViewModel:PromoteInfoViewModel by viewModels()

    val newsViewModel:NewsInfoViewModel by viewModels()

    val galleryViewModel:GalleryInfoViewModel by viewModels()

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
                val newsImg = intent?.getStringExtra("newsImg")



                lifecycleScope.launch {
                    val promoteInfo = promoteViewModel.getPromoteInfoByImage(promoteImg?:"")
                    val newsInfo = newsViewModel.getNewsInfoByImage(newsImg?:"")

                    if (promoteInfo?.homeIdx == 1){
                        title = "전시 홍보"
                    }else if (newsInfo?.homeIdx == 2){
                        title = "소식"
                    }else{
                        title = "전시실 작품"
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
            val newsImg = intent?.getStringExtra("newsImg")
            val galleryInfoImg = intent?.getStringExtra("galleryInfoImg")
            lifecycleScope.launch {
                val promoteInfo = promoteViewModel.getPromoteInfoByImage(promoteImg?:"")

                val newsInfo = newsViewModel.getNewsInfoByImage(newsImg?:"")

                val galleryInfo = galleryViewModel.getGalleryInfoByImg(galleryInfoImg?:"")

                if (promoteInfo?.homeIdx == 1){
                    textInfoOneName.text = "행사명 : ${promoteInfo?.promoteName}"
                    textInfoOneDate.text = "일정 : ${promoteInfo?.promoteDate}"
                    textInfoOnePlace.text = "장소 : ${promoteInfo?.promotePlace}"
                    textInfoOneInfo.text = "행사소개: ${promoteInfo?.promoteContent}"
                    textInfoOneCost.text = "참가비 : ${promoteInfo?.promoteMoney}"
                    textInfoOneUrl.text = "참가 신청 주소 : ${promoteInfo?.promoteLink}"

                    val imgUrl = getImageUrlFromName(promoteInfo?.promoteImg?:"")

                    setImage(activityInfoOneBinding.imageViewEventLogo, imgUrl)
                }
                else if (newsInfo?.homeIdx == 2){
                    textInfoOneName.text = "행사명 : ${newsInfo?.newsName}"
                    textInfoOneDate.text = "일정 : ${newsInfo?.newsDate}"
                    textInfoOnePlace.visibility = View.GONE
                    textInfoOneInfo.text = "이벤트 소개: ${newsInfo?.newsContent}"
                    textInfoOneCost.text = "이벤트 : ${newsInfo?.newsSale}"
                    textInfoOneUrl.visibility = View.GONE

                    val imgUrl = getImageUrlFromNews(newsInfo?.newsImg?:"")
                    Log.d("test1234", imgUrl)

                    setImage(activityInfoOneBinding.imageViewEventLogo, imgUrl)
                }
                else if (galleryInfo?.homeIdx == 3){
                    textInfoOneName.text = "작품명 : ${galleryInfo?.galleryInfoName}"
                    textInfoOneDate.text = "제작년도 : ${galleryInfo?.galleryInfoDate}"
                    textInfoOnePlace.visibility = View.GONE
                    textInfoOneInfo.text = "작가: ${galleryInfo?.galleryInfoAuthor}"
                    textInfoOneCost.text = "작품 소개 : ${galleryInfo?.galleryInfoContent}"
                    textInfoOneUrl.visibility = View.GONE

                    val imgUrl = getImageUrlFromGallery(galleryInfo.galleryInfoImg?:"")
                    Log.d("test1234", imgUrl)

                    setImage(activityInfoOneBinding.imageViewEventLogo, imgUrl)
                }
            }
        }
    }
}