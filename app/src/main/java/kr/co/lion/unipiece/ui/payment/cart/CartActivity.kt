package kr.co.lion.unipiece.ui.payment.cart

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivityCartBinding
import kr.co.lion.unipiece.databinding.RowCartBinding
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

    fun setCheckBoxAll(){
        // 전체 선택 체크박스 클릭 리스너 설정
        activityCartBinding.checkBoxCartAll.apply {
            setOnClickListener {
                // 체크박스의 체크 상태를 가져옵니다.
                val isChecked = this.isChecked

                // 어댑터를 가져와서 selectAll 함수를 호출합니다.
                // 이때, 체크박스의 현재 상태(isChecked)를 인자로 전달합니다.
                // 이는 모든 항목을 현재 체크박스의 상태와 동일하게 선택하거나 선택 해제하는 기능을 수행합니다.
                val adapter = activityCartBinding.recyclerViewCartList.adapter as CartRecyclerViewAdapter
                adapter.selectAll(isChecked)
            }
        }
    }

    /////////////////////////////// 기능 구현 ///////////////////////////////////////

    // 툴바 셋팅
    fun setToolbar(){
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
    fun clickButtonOrder(){
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
    fun setRecyclerViewCart(){
        activityCartBinding.apply {
            recyclerViewCartList.apply {
                // 어댑터 초기화 시 OnItemCheckStateChangeListener 구현을 전달
                // CartRecyclerViewAdapter의 초기화 시점에 받는 리스너
                // 이 리스너는 항목의 체크 상태가 변경될 때 호출되어,
                // 전체 선택 체크박스의 상태를 업데이트하는 데 사용됩니다.
                adapter = CartRecyclerViewAdapter(object : OnItemCheckStateChangeListener {
                    override fun onItemCheckStateChanged(isAllSelected: Boolean) {
                        // 모든 항목이 선택되었는지 여부에 따라 전체 선택 체크박스의 상태를 업데이트합니다.
                        checkBoxCartAll.isChecked = isAllSelected
                    }
                })

                layoutManager = LinearLayoutManager(this@CartActivity)
            }
        }
    }

    // 장바구니 화면의 RecyclerView의 어댑터
    inner class CartRecyclerViewAdapter(private val listener: OnItemCheckStateChangeListener) :
        RecyclerView.Adapter<CartRecyclerViewAdapter.CartViewHolder>() {

        // 항목의 선택 상태를 저장하는 리스트
        var isCheckedList = MutableList(10) { false } // 여기서 10은 항목의 수, 초기 상태는 모두 false(선택 안됨)

        // CartRecyclerViewAdapter 내부
        fun selectAll(isChecked: Boolean) {
            // 모든 항목의 선택 상태를 변경합니다.
            // isSelected 파라미터 값에 따라 모든 항목을 선택하거나 선택 해제합니다.
            isCheckedList = MutableList(itemCount) { isChecked }

            // 데이터가 변경되었음을 어댑터에게 알려 UI를 갱신합니다.
            notifyDataSetChanged()
        }

        inner class CartViewHolder(rowCartBinding: RowCartBinding) :
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

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
            val rowCartBinding = RowCartBinding.inflate(layoutInflater)
            val mainViewHolder = CartViewHolder(rowCartBinding)
            return mainViewHolder
        }

        override fun getItemCount(): Int {
            return isCheckedList.size
        }

        override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
            // 체크박스 상태를 명시적으로 설정
            // isSelectedList에서 현재 position에 해당하는 항목의 선택 상태를 가져와서
            // 해당 항목의 체크박스 상태를 설정합니다.
            holder.rowCartBinding.checkBoxCartRow.isChecked = isCheckedList[position]

            // 체크박스 리스너 설정 전에 기존 리스너를 제거
            // 이는 리사이클러 뷰가 뷰를 재활용할 때 발생할 수 있는 중복 클릭 이벤트를 방지하기 위함입니다.
            holder.rowCartBinding.checkBoxCartRow.setOnCheckedChangeListener(null)

            // 체크박스 클릭 리스너 설정
            holder.rowCartBinding.checkBoxCartRow.setOnClickListener {
                // 클릭된 항목의 현재 위치를 가져옵니다.
                // RecyclerView에서 항목이 재활용되기 때문에 클릭 이벤트가 발생할 때
                // 현재 항목의 정확한 위치를 얻기 위해 holder.adapterPosition을 사용합니다.
                val currentPosition = holder.position

                // 현재 항목의 선택 상태를 반전시킵니다.
                // 현재 상태가 선택됨(true)이면 선택되지 않음(false)으로, 선택되지 않음(false)이면 선택됨(true)으로 변경합니다.
                val isChecked = !isCheckedList[currentPosition]

                // 변경된 상태를 isSelectedList에 반영합니다.
                isCheckedList[currentPosition] = isChecked

                // 해당 항목만 업데이트하여 UI를 최신 상태로 갱신합니다.
                // 이는 성능 최적화를 위해 전체 목록을 다시 로드하는 것이 아니라 변경된 부분만 업데이트합니다.
                notifyItemChanged(currentPosition)

                // 리스너를 통해 액티비티에 체크 상태 변경을 알립니다.
                // 모든 항목의 선택 상태를 확인한 후, 모든 항목이 선택되었다면 true, 그렇지 않다면 false를 전달합니다.
                // 이를 통해 액티비티에서는 전체 선택 체크박스의 상태를 업데이트할 수 있습니다.
                listener.onItemCheckStateChanged(isAllSelected())
            }
        }

        // 모든 항목이 선택되었는지 확인하는 함수
        fun isAllSelected(): Boolean {
            // isSelectedList의 모든 항목이 true(선택됨)일 경우에만 true를 반환하고,
            // 하나라도 false(선택되지 않음)가 있다면 false를 반환합니다.
            return isCheckedList.all { it }
        }
    }
}

interface OnItemCheckStateChangeListener {
    fun onItemCheckStateChanged(isAllSelected: Boolean)
}