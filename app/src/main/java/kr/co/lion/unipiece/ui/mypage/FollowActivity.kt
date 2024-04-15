package kr.co.lion.unipiece.ui.mypage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivityFollowBinding
import kr.co.lion.unipiece.databinding.RowFollowBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.author.AuthorInfoActivity
import kr.co.lion.unipiece.ui.mypage.adapter.FollowAdapter
import kr.co.lion.unipiece.util.CustomDialog

class FollowActivity : AppCompatActivity() {

    lateinit var activityFollowBinding: ActivityFollowBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityFollowBinding = ActivityFollowBinding.inflate(layoutInflater)
        setContentView(activityFollowBinding.root)


        setToolbar()
        setRecyclerViewFollow()

    }

    // 툴바 셋팅
    fun setToolbar(){
        activityFollowBinding.apply {
            toolbarFollowAuthor.apply {
                title = "팔로우한 작가"
                isTitleCentered = true

                // 뒤로가기 아이콘
                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    finish()
                }
            }
        }
    }

    // 메인 화면의 RecyclerView 설정
    fun setRecyclerViewFollow(){
        activityFollowBinding.apply {
            recyclerViewFollowAuthorList.apply {
                // 어뎁터
                adapter = FollowAdapter()
                // 레이아웃 매니저
                layoutManager = GridLayoutManager(this@FollowActivity,2)
            }
        }
    }


}