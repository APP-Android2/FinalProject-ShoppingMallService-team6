package kr.co.lion.unipiece.ui.buy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivityBuyDetailBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.author.AuthorInfoActivity
import kr.co.lion.unipiece.ui.buy.viewmodel.BuyDetailViewModel
import kr.co.lion.unipiece.ui.buy.viewmodel.BuyDetailViewModelFactory
import kr.co.lion.unipiece.ui.payment.cart.CartActivity
import kr.co.lion.unipiece.ui.payment.order.OrderActivity
import kr.co.lion.unipiece.util.MainFragmentName.*
import kr.co.lion.unipiece.util.setImage
import kr.co.lion.unipiece.util.setMenuIconColor
import java.text.DecimalFormat

class BuyDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityBuyDetailBinding

    private var pieceIdx: Int = -1

    private var authorIdx: Int = -1

    private val viewModel: BuyDetailViewModel by lazy {
        ViewModelProvider(this, BuyDetailViewModelFactory(pieceIdx, authorIdx)).get(BuyDetailViewModel::class.java)
    }

    // 좋아요 버튼 테스트 데이터
    var click = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBuyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setToolbar()
        likeBtnClick()
        cartBtnClick()
        buyBtnClick()

    }

    fun initView(){

        pieceIdx = intent.getIntExtra("pieceIdx", -1)
        authorIdx = intent.getIntExtra("authorIdx", -1)

        lifecycleScope.launch {
            viewModel.getIdxPieceInfo(pieceIdx)
            viewModel.getIdxAuthorInfo(authorIdx)
            viewModel.getAuthorReviewDataByIdx(authorIdx)
        }

        setPieceInfo()
        setAuthorInfo()
        setAuthorReview()
    }

    fun setPieceInfo(){
        viewModel.pieceInfo.observe(this@BuyDetailActivity, Observer {
            with(binding){
                if (it != null) {
                    progressBar.visibility = View.GONE
                    setImage(pieceImg, it.pieceImg)
                    authorName.text = it.authorName
                    pieceName.text = it.pieceName
                    pieceYear.text = "제작년도 ${it.makeYear}"
                    pieceSize.text = "작품 크기 ${it.pieceSize}"
                    pieceMaterial.text = "작품 재료 ${it.pieceMaterial}"
                    pieceLike.text = "${it.pieceLike}명이 좋아요를 눌렀어요"
                    pieceInfo.text = "${it.pieceInfo}"

                    val priceFormat = DecimalFormat("###,###")
                    val price = priceFormat.format(it.piecePrice)
                    buyBtn.text = "${price}원 구매"
                }
            }
        })
    }

    fun setAuthorInfo(){
        viewModel.authorInfo.observe(this@BuyDetailActivity, Observer {
            with(binding){
                if (it != null) {
                    progressBar.visibility = View.GONE
                    setImage(authorImg, it.authorImg)
                    authorInfoName.text = it.authorName
                    authorInfo.text = it.authorInfo

                    val intent = Intent(this@BuyDetailActivity, AuthorInfoActivity::class.java)
                    intent.putExtra("authorIdx", it.authorIdx)

                    authorInfoMore.setOnClickListener {
                        startActivity(intent)
                    }

                }
            }
        })
    }

    fun setAuthorReview(){
        viewModel.authorReviewList.observe(this@BuyDetailActivity, Observer {
            with(binding){
                progressBar.visibility = View.GONE
                if (it != null) {
                    when(it.size){
                        0 -> {
                            review1.visibility = View.GONE
                            review2.visibility = View.GONE
                            review3.visibility = View.GONE
                        }
                        1 -> {
                            review2.visibility = View.GONE
                            review3.visibility = View.GONE

                            nickname1.text = it[0].userNickname
                            reviewText1.text = it[0].reviewContent
                        }
                        2 -> {
                            review3.visibility = View.GONE

                            nickname1.text = it[0].userNickname
                            reviewText1.text = it[0].reviewContent
                            nickname2.text = it[1].userNickname
                            reviewText2.text = it[1].reviewContent

                        }
                        3 -> {
                            nickname1.text = it[0].userNickname
                            reviewText1.text = it[0].reviewContent
                            nickname2.text = it[1].userNickname
                            reviewText2.text = it[1].reviewContent
                            nickname3.text = it[2].userNickname
                            reviewText3.text = it[2].reviewContent
                        }
                    }
                }
            }
        })
    }

    fun setToolbar(){
        with(binding) {
            toolbarBuyDetail.apply{
                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    finish()
                }

                inflateMenu(R.menu.menu_search)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_search -> {
                            setIntent("SearchFragment")
                            true
                        }
                        R.id.menu_cart -> {

                            true
                        }
                        else -> false
                    }
                }

                setMenuIconColor(menu, R.id.menu_search, R.color.second)
            }
        }
    }

    fun setIntent(name: String) {

        val intent = Intent(this@BuyDetailActivity, MainActivity::class.java).apply {
            // MainActivity가 이미 실행 중인 경우 해당 인스턴스를 재사용합니다.
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        intent.putExtra(name, true)
        startActivity(intent)
        finish()
    }

    fun likeBtnClick(){

        with(binding.likeBtn){
            setOnClickListener {
                click = !click
                if(click){
                    setImageResource(R.drawable.heart_icon)
                } else {
                    setImageResource(R.drawable.heartoff_icon)
                }
            }
        }
    }

    fun cartBtnClick() {
        with(binding.cartBtn) {
            setOnClickListener {
                val intent = Intent(this@BuyDetailActivity, CartActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun buyBtnClick() {
        with(binding.buyBtn) {
            setOnClickListener {
                val intent = Intent(this@BuyDetailActivity, OrderActivity::class.java)
                startActivity(intent)
            }
        }
    }

}