package kr.co.lion.unipiece.ui.buy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivityBuyDetailBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.search.SearchFragment
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
    }

    fun setToolbar(){
        with(binding) {
            toolbarBuyDetail.apply{
                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    setIntent("BuyFragment")
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
        val intent = Intent(this@BuyDetailActivity, MainActivity::class.java)
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
}