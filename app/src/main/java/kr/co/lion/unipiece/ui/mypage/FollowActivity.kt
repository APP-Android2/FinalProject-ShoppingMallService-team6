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

                // 프로필 사진을 클릭 시
                this.rowFollowBinding.ivProfileImage.setOnClickListener{
                    val authorInfoIntent = Intent(this@FollowActivity,AuthorInfoActivity::class.java)
                    startActivity(authorInfoIntent)
                }

                // 항목별 팔로우 취소 텍스트 버튼 클릭 시
                this.rowFollowBinding.textButtonFollowCancel.setOnClickListener {
                    val followCancelDialog = CustomDialog("팔로우 취소", "홍길동 작가 팔로우를 취소하시겠습니까?")
                    followCancelDialog.show(this@FollowActivity.supportFragmentManager,"FollowCancelCustomDialog")
                    followCancelDialog.setButtonClickListener(object :CustomDialog.OnButtonClickListener{
                        override fun okButtonClick() {

                        }

                        override fun noButtonClick() {

                        }

                    })
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