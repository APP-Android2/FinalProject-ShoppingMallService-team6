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
                adapter = MainRecyclerViewAdapter()
                // 레이아웃 매니저
                layoutManager = GridLayoutManager(this@FollowActivity,2)
            }
        }
    }

    // 메인 화면의 RecyclerView의 어뎁터
    inner class MainRecyclerViewAdapter :
        RecyclerView.Adapter<MainRecyclerViewAdapter.MainViewHolder>() {
        inner class MainViewHolder(rowFollowBinding: RowFollowBinding) :
            RecyclerView.ViewHolder(rowFollowBinding.root) {
            val rowFollowBinding: RowFollowBinding

            init {
                this.rowFollowBinding = rowFollowBinding

                // 항목별 팔로잉 버튼 클릭 시
                this.rowFollowBinding.buttonFollowListFollowing.setOnClickListener {
                    // 속이 빈 버튼모양으로 변경
                }

                this.rowFollowBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val rowFollowBinding = RowFollowBinding.inflate(layoutInflater)
            val mainViewHolder = MainViewHolder(rowFollowBinding)
            return mainViewHolder
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            holder.rowFollowBinding.textViewFollowListAuthorName.text = "홍길동"
        }


    }
}