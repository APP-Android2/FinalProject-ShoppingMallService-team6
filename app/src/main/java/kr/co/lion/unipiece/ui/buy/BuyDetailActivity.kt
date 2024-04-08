package kr.co.lion.unipiece.ui.buy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivityBuyDetailBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.payment.cart.CartActivity
import kr.co.lion.unipiece.ui.payment.order.OrderActivity
import kr.co.lion.unipiece.util.MainFragmentName.*
import kr.co.lion.unipiece.util.setMenuIconColor

class BuyDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityBuyDetailBinding

    // 좋아요 버튼 테스트 데이터
    var click = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBuyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()
        likeBtnClick()
        cartBtnClick()
        buyBtnClick()

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