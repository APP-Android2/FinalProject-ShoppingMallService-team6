package kr.co.lion.unipiece.ui.payment.cart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.unipiece.databinding.ActivityCartBinding
import kr.co.lion.unipiece.databinding.RowCartBinding

class CartActivity : AppCompatActivity() {
    lateinit var activityCartBinding: ActivityCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCartBinding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(activityCartBinding.root)


        setToolbar()
        setRecyclerViewCart()
        clickButtonOrder()
    }

    fun setToolbar(){
        activityCartBinding.apply {
            toolbarCart.apply {
                title = "장바구니"

            }
        }
    }

    // 주문하기 버튼 클릭 시
    fun clickButtonOrder(){
        activityCartBinding.apply {
            buttonCartOrder.apply {
                setOnClickListener {
//                    // OrderActivity를 실행한다.
//                    val orderIntent = Intent(this@CartActivity, OrderActivity::class.java)
//                    startActivity(orderIntent)
//                    // CartActivity를 종료한다.
//                    finish()

                }
            }
        }
    }

    // 메인 화면의 RecyclerView 설정
    fun setRecyclerViewCart(){
        activityCartBinding.apply {
            recyclerViewCartList.apply {
                // 어뎁터
                adapter = MainRecyclerViewAdapter()
                // 레이아웃 매니저
                layoutManager = LinearLayoutManager(this@CartActivity)
                // 데코레이션
                val deco = MaterialDividerItemDecoration(this@CartActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }

    // 메인 화면의 RecyclerView의 어뎁터
    inner class MainRecyclerViewAdapter :
        RecyclerView.Adapter<MainRecyclerViewAdapter.MainViewHolder>() {
        inner class MainViewHolder(rowCartBinding: RowCartBinding) :
            RecyclerView.ViewHolder(rowCartBinding.root) {
            val rowCartBinding: RowCartBinding

            init {
                this.rowCartBinding = rowCartBinding
                this.rowCartBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val rowCartBinding = RowCartBinding.inflate(layoutInflater)
            val mainViewHolder = MainViewHolder(rowCartBinding)
            return mainViewHolder
        }

        override fun getItemCount(): Int {
            return 100
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            holder.rowCartBinding.textViewRowCart.text = "이거슨 테스트"
        }
    }
}