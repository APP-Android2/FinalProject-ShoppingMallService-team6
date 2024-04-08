package kr.co.lion.unipiece.ui.buy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivityBuyDetailBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.payment.cart.CartActivity
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
        val buyIntent = intent.getBooleanExtra(BUY_FRAGMENT.str, false)
        val rankIntent = intent.getBooleanExtra(RANK_FRAGMENT.str, false)
        val mygalleryIntent = intent.getBooleanExtra(MY_GALLERY_FRAGMENT.str, false)

        val intent = Intent(this@BuyDetailActivity, MainActivity::class.java)
        intent.putExtra("RankFragment", rankIntent)
        intent.putExtra("BuyFragment", buyIntent)
        intent.putExtra("MyGalleryFragment", mygalleryIntent)

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
        with(binding.cartBtn){
            setOnClickListener {
                val intent = Intent(this@BuyDetailActivity, CartActivity::class.java)
                startActivity(intent)
            }
        }
    }

}