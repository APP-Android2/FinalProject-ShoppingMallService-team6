package kr.co.lion.unipiece.ui.buy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.ActivityBuyDetailBinding
import kr.co.lion.unipiece.model.CartData
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.author.AuthorInfoActivity
import kr.co.lion.unipiece.ui.buy.viewmodel.BuyDetailViewModel
import kr.co.lion.unipiece.ui.buy.viewmodel.BuyDetailViewModelFactory
import kr.co.lion.unipiece.ui.mypage.VisitGalleryActivity
import kr.co.lion.unipiece.ui.payment.CartActivity
import kr.co.lion.unipiece.ui.payment.OrderActivity
import kr.co.lion.unipiece.util.setImage
import kr.co.lion.unipiece.util.setMenuIconColor
import java.text.DecimalFormat

class BuyDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityBuyDetailBinding

    private var pieceIdx: Int = -1

    private var authorIdx: Int = -1

    private var userIdx: Int = UniPieceApplication.prefs.getUserIdx("userIdx",0)

    private val viewModel: BuyDetailViewModel by lazy {
        ViewModelProvider(this, BuyDetailViewModelFactory(pieceIdx, authorIdx, userIdx)).get(BuyDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBuyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIdx()
        initView()
    }

    fun getIdx(){
        pieceIdx = intent.getIntExtra("pieceIdx", -1)
        authorIdx = intent.getIntExtra("authorIdx", -1)
    }

    fun initView(){

        lifecycleScope.launch {
            viewModel.getIdxPieceInfo()
            viewModel.getIdxAuthorInfo()
            viewModel.getAuthorReviewDataByIdx()
        }

        setToolbar()

        setPieceInfo()
        setAuthorInfo()
        setAuthorReview()

        setProgressBar()

        getLikeBtn()
        setLikeBtn()

        setLikeCount()

        visitGalleryBtn()

        buyBtnClick()

        getCartBtn()
        setCartBtn()
    }

    fun setLikeCount(){
        viewModel.likePieceCount.observe(this@BuyDetailActivity) { likeCount ->
            binding.pieceLike.text = "${likeCount}명이 좋아요를 눌렀어요"
        }
    }

    fun setLikeBtn(){

        viewModel.likePiece.observe(this@BuyDetailActivity) { isLiked ->
            with(binding.likeBtn) {
                setImageResource(if (isLiked) R.drawable.heart_icon else R.drawable.heartoff_icon)
            }
        }

        clickLikeBtn()
    }

    fun getLikeBtn(){
        lifecycleScope.launch {
            viewModel.getLikePiece()
        }
    }

    fun clickLikeBtn(){
        binding.likeBtn.setOnClickListener {
            lifecycleScope.launch {
                if(viewModel.likePiece.value == true) {
                    showLikeSnackbar("좋아요를 취소했습니다.")
                    viewModel.cancelLikePiece()
                    viewModel.updateLike()
                } else {
                    showLikeSnackbar("좋아요를 눌렀습니다.")
                    viewModel.addLikePiece()
                    viewModel.updateLike()
                }
                viewModel.getLikePiece()
            }
        }
    }

    fun setProgressBar(){
        viewModel.allDataReceived.observe(this@BuyDetailActivity) {
            if (it) {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    fun setPieceInfo(){
        viewModel.pieceInfo.observe(this@BuyDetailActivity) {
            with(binding) {
                if (it != null) {

                    lifecycleScope.launch {
                        viewModel.setPieceInfoReceived(true)
                    }

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
        }
    }

    fun setAuthorInfo(){
        viewModel.authorInfo.observe(this@BuyDetailActivity) {
            with(binding) {
                if (it != null) {

                    lifecycleScope.launch {
                        viewModel.setAuthorInfoReceived(true)
                    }

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
        }
    }

    fun setAuthorReview(){
        viewModel.authorReviewList.observe(this@BuyDetailActivity) {
            with(binding) {

                lifecycleScope.launch {
                    viewModel.setAuthorReviewReceived(true)
                }

                if (it != null) {
                    when (it.size) {
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

                        else -> {
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
        }
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

    fun visitGalleryBtn(){
        binding.galleryBtn.setOnClickListener {
            val intent = Intent(this@BuyDetailActivity, VisitGalleryActivity::class.java)
            startActivity(intent)
        }
    }

    fun getCartBtn(){
        lifecycleScope.launch {
            viewModel.getCartPiece()
        }
    }

    fun setCartBtn(){
        viewModel.cartPiece.observe(this@BuyDetailActivity) {
            with(binding.cartBtn) {
                setImageResource(if (it) R.drawable.shopcart_icon_on else R.drawable.shopcart_icon)
            }
        }

        cartBtnClick()
    }

    fun cartBtnClick() {
        binding.cartBtn.setOnClickListener {
            lifecycleScope.launch {
                if(viewModel.cartPiece.value == true){
                    showCartSnackbar("장바구니에서 삭제했습니다.")
                    viewModel.cancelCartPiece()
                } else {
                    showCartSnackbar("장바구니에 담았습니다.")
                    viewModel.insertCartData(CartData(userIdx, pieceIdx, Timestamp.now()))
                }
                viewModel.getCartPiece()
            }
        }
    }

    fun buyBtnClick() {
        with(binding.buyBtn) {
            setOnClickListener {
                val intent = Intent(this@BuyDetailActivity, OrderActivity::class.java)

                intent.putExtra("pieceIdx", viewModel.pieceInfo.value?.pieceIdx)

                startActivity(intent)
            }
        }
    }

    private fun showLikeSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setAnchorView(binding.bottomBarBuyDetail)
            .setBackgroundTint(ContextCompat.getColor(this@BuyDetailActivity, R.color.second))
            .setTextColor(ContextCompat.getColor(this@BuyDetailActivity, R.color.white))
            .show()
    }

    private fun showCartSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setAnchorView(binding.bottomBarBuyDetail)
            .setBackgroundTint(ContextCompat.getColor(this@BuyDetailActivity, R.color.first))
            .setTextColor(ContextCompat.getColor(this@BuyDetailActivity, R.color.white))
            .setActionTextColor(ContextCompat.getColor(this@BuyDetailActivity, R.color.third))
            .setAction("장바구니로 가기"){
                val intent = Intent(this@BuyDetailActivity, CartActivity::class.java)
                startActivity(intent)
            }
            .show()
    }
}