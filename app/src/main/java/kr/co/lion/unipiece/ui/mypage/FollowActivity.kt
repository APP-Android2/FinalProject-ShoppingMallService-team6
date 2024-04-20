package kr.co.lion.unipiece.ui.mypage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivityFollowBinding
import kr.co.lion.unipiece.ui.author.AuthorInfoActivity
import kr.co.lion.unipiece.ui.home.AuthorAdapter
import kr.co.lion.unipiece.ui.mypage.adapter.FollowAdapter

class FollowActivity : AppCompatActivity() {

    lateinit var binding: ActivityFollowBinding

    val followAdapter: FollowAdapter by lazy {
        val adapter = FollowAdapter()
        adapter.setRecyclerviewClickListener(object : FollowAdapter.FollowAuthorImageOnClickListener{
            override fun followAuthorImageClickListener() {
                startActivity(Intent(this@FollowActivity,AuthorInfoActivity::class.java))
            }
        })
        adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFollowBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initView()

    }


    fun initView(){
        with(binding){

            // 툴바 셋팅
            with(toolbarFollowAuthor) {
                title = "팔로우한 작가"
                isTitleCentered = true

                // 뒤로가기 아이콘
                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    finish()
                }
            }

            // 리사이클러뷰
            with(recyclerViewFollowAuthorList) {
                // 어뎁터
                adapter = followAdapter
                // 레이아웃 매니저
                layoutManager = GridLayoutManager(this@FollowActivity, 2)
            }
        }
    }
}