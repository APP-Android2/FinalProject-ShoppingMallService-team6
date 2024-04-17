package kr.co.lion.unipiece.ui.payment.cart

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivityCartBinding
import kr.co.lion.unipiece.ui.payment.adapter.CartAdapter
import kr.co.lion.unipiece.ui.payment.adapter.OnItemCheckStateChangeListener
import kr.co.lion.unipiece.ui.payment.order.OrderActivity

class CartActivity : AppCompatActivity() {
    lateinit var activityCartBinding: ActivityCartBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityCartBinding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(activityCartBinding.root)

        setToolbar()
        setRecyclerViewCart()
        clickButtonOrder()
        setCheckBoxAll()
    }

    /////////////////////////////// 기능 구현 ///////////////////////////////////////

    // 툴바 셋팅
    fun setToolbar() {
        activityCartBinding.apply {
            toolbarCart.apply {
                // 타이틀
                setTitle("장바구니")

                // 뒤로가기 아이콘 셋팅
                setNavigationIcon(R.drawable.back_icon)

                // 뒤로가기 버튼 클릭 시
                setNavigationOnClickListener {
                    finish()
                }
            }
        }
    }

    // 주문하기 버튼 클릭 시
    fun clickButtonOrder() {
        activityCartBinding.apply {
            buttonCartOrder.apply {
                setOnClickListener {
                    // OrderActivity를 실행한다.
                    val orderIntent = Intent(this@CartActivity, OrderActivity::class.java)
                    startActivity(orderIntent)

                }
            }
        }
    }

    ///////////////////////////////////// 리사이클러뷰 ////////////////////////////////////

    // 장바구니 화면의 RecyclerView 설정
    fun setRecyclerViewCart() {
        activityCartBinding.apply {
            recyclerViewCartList.apply {
                // 어댑터 초기화 시 OnItemCheckStateChangeListener 구현을 전달
                // CartRecyclerViewAdapter의 초기화 시점에 받는 리스너
                // 이 리스너는 항목의 체크 상태가 변경될 때 호출되어,
                // 전체 선택 체크박스의 상태를 업데이트하는 데 사용됩니다.
                adapter = CartAdapter(object : OnItemCheckStateChangeListener {
                    override fun onItemCheckStateChanged(isAllSelected: Boolean) {
                        // 모든 항목이 선택되었는지 여부에 따라 전체 선택 체크박스의 상태를 업데이트합니다.
                        checkBoxCartAll.isChecked = isAllSelected
                    }
                })

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
        }
    }

    fun setCheckBoxAll() {
        // 전체 선택 체크박스 클릭 리스너 설정
        activityCartBinding.checkBoxCartAll.apply {
            setOnClickListener {
                // 체크박스의 체크 상태를 가져옵니다.
                val isChecked = this.isChecked

                // 어댑터를 가져와서 selectAll 함수를 호출합니다.
                // 이때, 체크박스의 현재 상태(isChecked)를 인자로 전달합니다.
                // 이는 모든 항목을 현재 체크박스의 상태와 동일하게 선택하거나 선택 해제하는 기능을 수행합니다.
                val adapter = activityCartBinding.recyclerViewCartList.adapter as CartAdapter
                adapter.selectAll(isChecked)
            }
        }
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
