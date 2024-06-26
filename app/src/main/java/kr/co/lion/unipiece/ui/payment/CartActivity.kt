package kr.co.lion.unipiece.ui.payment

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.ActivityCartBinding
import kr.co.lion.unipiece.model.CartInfoData
import kr.co.lion.unipiece.ui.author.AuthorInfoActivity
import kr.co.lion.unipiece.ui.buy.BuyDetailActivity
import kr.co.lion.unipiece.ui.payment.adapter.CartAdapter
import kr.co.lion.unipiece.ui.payment.viewmodel.CartViewModel
import kr.co.lion.unipiece.util.CustomDialog
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CartActivity : AppCompatActivity() {
    lateinit var binding: ActivityCartBinding
    private val viewModel: CartViewModel by viewModels()
    val userIdx = UniPieceApplication.prefs.getUserIdx("userIdx", 0)

    private val cartAdapter: CartAdapter = CartAdapter(
        emptyList<CartInfoData>().toMutableList(),

        // 항목 작품 이미지 클릭 시 (pieceIdx를 받아옴)
        pieceImgOnClickListener = {
            val intent = Intent(this, BuyDetailActivity::class.java).apply {
                putExtra("authorIdx", it["authorIdx"])
                putExtra("pieceIdx", it["pieceIdx"])
            }
            startActivity(intent)

        },
        authorNameOnClickListener = { authorIdx ->
            val intent = Intent(this, AuthorInfoActivity::class.java).apply {
                putExtra("authorIdx", authorIdx)
            }
            startActivity(intent)
        },
        closeButtonOnClickListener = { pieceIdx ->
            val dialog = CustomCloseDialog("", "이 작품을 장바구니에서 삭제하시겠습니까?")
            dialog.setButtonClickListener(object :
                CustomCloseDialog.OnButtonClickListener {
                // 확인 버튼 클릭 시
                override fun okButtonClick() {
                    lifecycleScope.launch {
                        // 삭제 처리
                        viewModel.deleteCartDataByUserIdx(userIdx, pieceIdx){
                            // viewModel.setLoading(true)
                            if(it){
                                viewModel.getCartDataByUserIdx(userIdx)
                                dialog.dismiss()
                            }
                        }
                    }
                }

                // 취소 버튼 클릭 시
                override fun noButtonClick() {

                }

            })
            dialog.show(supportFragmentManager, "deleteDialog")
        }


    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        observeData()
        setLoading()
    }

    fun setLoading(){
        viewModel.loading.observe(this@CartActivity) { loading ->
            with(binding){
                if(loading){
                    progressBar.visibility = View.VISIBLE
                    cardView.visibility = View.GONE
                } else {
                    progressBar.visibility = View.GONE
                    cardView.visibility = View.VISIBLE
                }
            }
        }
    }


    // 툴바 셋팅

    fun initView() {
        with(binding) {

            // 툴바 세팅
            with(toolbarCart) {
                // 타이틀
                setTitle("장바구니")

                // 뒤로가기 아이콘 셋팅
                setNavigationIcon(R.drawable.back_icon)

                // 뒤로가기 버튼 클릭 시
                setNavigationOnClickListener {
                    finish()
                }
            }


            // 장바구니 화면의 RecyclerView 설정
            with(recyclerViewCartList) {
                // 어댑터 초기화 시 OnItemCheckStateChangeListener 구현을 전달
                // CartRecyclerViewAdapter의 초기화 시점에 받는 리스너
                // 이 리스너는 항목의 체크 상태가 변경될 때 호출되어,
                // 전체 선택 체크박스의 상태를 업데이트하는 데 사용됩니다.
                adapter = cartAdapter

                layoutManager = LinearLayoutManager(this@CartActivity)


                // 마지막 리사이클러뷰 항목의 디바이더 삭제
                addItemDecoration(
                    MaterialDividerItemDecoration(
                        this@CartActivity,
                        (layoutManager as LinearLayoutManager).orientation
                    ).apply {
                        isLastItemDecorated = false
                        setDividerColorResource(this@CartActivity, R.color.lightgray)
                        dividerInsetEnd = 16.dp
                        dividerInsetStart = 16.dp
                    }

                )
            }

            // 전체선택 체크박스
            with(checkBoxCartAll) {
                setOnClickListener {
                    if (isChecked) {
                        cartAdapter.selectAll()
                    } else {
                        cartAdapter.clearSelection()
                    }
                }
            }

            // 선택한 작품 제거 버튼
            with(buttonCartDelete) {
                setOnClickListener {
                    val snackbar = showLikeSnackbar("장바구니에서 삭제했습니다.")
                    // viewModel.setLoading(true)
                    val list = cartAdapter.getSelectedData()
                    lifecycleScope.launch {
                        val deletionTasks = list.map { item ->
                            async {
                                suspendCoroutine<Boolean> { cont ->
                                    viewModel.deleteCartDataByUserIdx(userIdx, item.pieceInfo.pieceIdx) { success ->
                                        cont.resume(success)
                                    }
                                }
                            }
                        }
                        val results = deletionTasks.awaitAll()

                        if (results.all { it }) {
                            viewModel.getCartDataByUserIdx(userIdx)
                            // snackbar.dismiss()
                        }
                    }

                    /*list.forEach {
                        viewModel.deleteCartDataByUserIdx(userIdx, it.pieceInfo.pieceIdx) {
                            if(it){
                                viewModel.getCartDataByUserIdx(userIdx)
                            }
                        }
                    }*/
                }
            }

            // 주문하기 버튼 클릭 시
            with(buttonCartOrder) {
                setOnClickListener {
                    // CartAdapter에서 현재 체크된 데이터를 가져온다.
                    var currentData = cartAdapter.getSelectedData()
                    Log.d("currentData", "$currentData")

                    // 전체 다 선택이 안 되어있으면 장바구니 전체 데이터를 주문하기로 넘겨준다
                    if(currentData.isNullOrEmpty()){
                        currentData = cartAdapter.getCurrentData()
                    }
                    
                    // pieceIdx만 추출하여 리스트로 만든다.
                    val pieceIdxList = currentData.map { it.pieceInfo.pieceIdx }
                    Log.d("pieceIdxList", "$pieceIdxList")
                    // Intent에 pieceIdx 리스트를 넣어서 OrderActivity로 전달한다.
                    val orderIntent =
                        Intent(this@CartActivity, OrderActivity::class.java).apply {
                            putExtra(
                                "pieceIdxList",
                                ArrayList(pieceIdxList)
                            ) // ArrayList로 변환하여 넣는다.
                        }
                    startActivity(orderIntent)
                }
            }

        }
    }
    fun observeData() {
        // 데이터 변경 관찰
        lifecycleScope.launch {
            viewModel.cartInfoData.observe(this@CartActivity) { cartDataList ->
                cartAdapter.updateData(cartDataList)
                Log.d("viewmodel", "${viewModel.cartInfoData.value.toString()}")
            }
        }
    }

    private fun showLikeSnackbar(message: String): Snackbar {
        return Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setAnchorView(binding.bottomCart)
            .setBackgroundTint(ContextCompat.getColor(this@CartActivity, R.color.second))
            .setTextColor(ContextCompat.getColor(this@CartActivity, R.color.white))
            .also { it.show() }
    }
}




    // dp값으로 변환하는 확장함수
    inline val Int.dp: Int
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics
        ).toInt()

    inline val Float.dp: Float
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics
        )
