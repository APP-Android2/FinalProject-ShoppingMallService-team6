package kr.co.lion.unipiece.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.ActivityFollowBinding
import kr.co.lion.unipiece.ui.author.AuthorInfoActivity
import kr.co.lion.unipiece.ui.author.viewmodel.AuthorInfoViewModel
import kr.co.lion.unipiece.ui.home.AuthorAdapter
import kr.co.lion.unipiece.ui.mypage.adapter.FollowAdapter
import kr.co.lion.unipiece.ui.payment.OrderActivity
import kr.co.lion.unipiece.ui.payment.adapter.DeliveryAdapter
import kr.co.lion.unipiece.util.CustomDialog

class FollowActivity : AppCompatActivity() {

    // 바인딩
    private lateinit var binding: ActivityFollowBinding
    // 작가정보 뷰모델
    private val viewModel:AuthorInfoViewModel by viewModels()
    // 현재 유저 인덱스
    val userIdx = UniPieceApplication.prefs.getUserIdx("userIdx",0)

    val followAdapter: FollowAdapter= FollowAdapter(
        emptyList(),

        // 항목 클릭 시
        rowClickListener = { authorIdx ->
            Log.d("테스트 rowClickListener authorIdx", authorIdx.toString())

            // 작가 번호를 작가정보액티비티로 전달한다.
            val intent = Intent(this,AuthorInfoActivity::class.java)
            intent.putExtra("authorIdx",authorIdx)
            startActivity(intent)
            finish()

        },

        // 팔로우 취소 버튼 클릭 시
        followCancelButtonOnClickListener = { authorIdx ->
            Log.d("테스트 followCancelButtonOnClickListener authorIdx", authorIdx.toString())
            lifecycleScope.launch {
                // 유저 이름 받아오기 (추후)

                // 팔로우 취소 다이얼로그 호출
                val dialog = CustomDialog("팔로우 취소", "해당 작가 팔로우를 취소하시겠습니까?")
                dialog.show(dialog.parentFragmentManager,"FollowCancelCustomDialog")
                dialog.setButtonClickListener(object : CustomDialog.OnButtonClickListener {
                    // 확인 버튼
                    override fun okButtonClick() {
                        lifecycleScope.launch {
                            // 팔로우 취소 처리
                            viewModel.cancelFollowing(userIdx,authorIdx)
                        }


                    }
                    // 취소 버튼
                    override fun noButtonClick() {

                    }

                })

            }


        }
    )

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