package kr.co.lion.unipiece.ui.payment.delivery

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentDeliveryManagerBinding
import kr.co.lion.unipiece.databinding.RowDeliveryBinding
import kr.co.lion.unipiece.ui.payment.order.OrderActivity
import kr.co.lion.unipiece.util.CustomDialog

class DeliveryManagerFragment : Fragment() {
    lateinit var fragmentDeliveryManagerBinding: FragmentDeliveryManagerBinding
    lateinit var rowDeliveryBinding: RowDeliveryBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentDeliveryManagerBinding = FragmentDeliveryManagerBinding.inflate(layoutInflater)
        rowDeliveryBinding = RowDeliveryBinding.inflate(layoutInflater)

        setToolbar()
        clickButtonDeliveryNewAdd()
        setRecyclerViewDelivery()

        return fragmentDeliveryManagerBinding.root
    }



    ///////////////////////////////////////////기능 구현/////////////////////////////////////////////

    // 툴바 셋팅
    fun setToolbar(){
        fragmentDeliveryManagerBinding.apply {
            toolbarDeliveryManager.apply {

                // 타이틀
                setTitle("배송지 관리")
                isTitleCentered = true

                // 뒤로가기 버튼 아이콘
                setNavigationIcon(R.drawable.back_icon)

                // 뒤로가기 버튼 클릭 시
                setNavigationOnClickListener {
                    requireActivity().finish()
                }
            }
        }
    }

    // 신규 배송지 등록 버튼 클릭 시
    fun clickButtonDeliveryNewAdd() {
        fragmentDeliveryManagerBinding.apply {
            buttonDeliveryMainNewAdd.apply {
                setOnClickListener {

                    // 커스텀 다이얼로그 (풀스크린)
                    CustomFullDialogMaker.apply {

                        // 다이얼로그 호출
                        getDialog(
                            requireActivity(),
                            "신규 배송지 등록",
                            "저장하기",
                            object : CustomFullDialogListener {

                                // 클릭한 이후 동작

                                // 저장하기 버튼 클릭 후 동작
                                override fun onClickSaveButton() {
                                    Toast.makeText(requireActivity(), "저장했다!", Toast.LENGTH_SHORT)
                                        .show()
                                }

                                // 뒤로가기 버튼 클릭 후 동작
                                override fun onClickCancelButton() {
                                    Toast.makeText(requireActivity(), "뒤로갔다!", Toast.LENGTH_SHORT)
                                        .show()


                                }
                            }
                        )
                    }
                }
            }
        }
    }


    /////////////////////////////////////////// 리사이클러뷰 ////////////////////////////////////////
    // 배송지 화면의 RecyclerView 설정
    fun setRecyclerViewDelivery() {
        fragmentDeliveryManagerBinding.apply {
            recyclerViewDeliveryList.apply {
                // 어뎁터
                adapter = DeliveryRecyclerViewAdapter()
                // 레이아웃 매니저
                layoutManager = LinearLayoutManager(requireActivity())

            }
        }
    }

    // 배송지 화면의 RecyclerView의 어뎁터
    inner class DeliveryRecyclerViewAdapter :
        RecyclerView.Adapter<DeliveryRecyclerViewAdapter.DeliveryViewHolder>() {
        inner class DeliveryViewHolder(rowDeliveryBinding: RowDeliveryBinding) :
            RecyclerView.ViewHolder(rowDeliveryBinding.root) {
            val rowDeliveryBinding: RowDeliveryBinding

            init {
                this.rowDeliveryBinding = rowDeliveryBinding

                // 항목별 삭제 버튼 클릭 시 다이얼로그
                this.rowDeliveryBinding.buttonDeliveryDelete.setOnClickListener{
                    val dialog = CustomDialog("배송지 삭제", "이 배송지를 삭제하시겠습니까?")
                    dialog.setButtonClickListener(object :CustomDialog.OnButtonClickListener{
                        override fun okButtonClick() {

                        }

                        override fun noButtonClick() {

                        }

                    })
                    dialog.show(requireActivity().supportFragmentManager, "CustomDialog")

                }

                // 항목별 수정 버튼 클릭 시 풀스크린 다이얼로그
                this.rowDeliveryBinding.buttonDeliveryUpdate.setOnClickListener {

                    // 커스텀 다이얼로그 (풀스크린)
                    CustomFullDialogMaker.apply {
                        // 다이얼로그 호출
                        getDialog(
                            requireActivity(),
                            "배송지 수정",
                            "저장하기",
                            object : CustomFullDialogListener {

                                // 클릭한 이후 동작
                                // 저장하기 버튼 클릭 후 동작
                                override fun onClickSaveButton() {
                                    Toast.makeText(requireActivity(), "저장했다!", Toast.LENGTH_SHORT)
                                        .show()
                                }

                                // 뒤로가기 버튼 클릭 후 동작
                                override fun onClickCancelButton() {
                                    Toast.makeText(requireActivity(), "뒤로갔다!", Toast.LENGTH_SHORT)
                                        .show()


                                }
                            }
                        )
                    }
                }

                // 선택 버튼 클릭 시
                this.rowDeliveryBinding.buttonDeliverySelect.setOnClickListener {

                    requireActivity().finish()
                }

                // 항목 클릭 시 클릭되는 범위 설정
                this.rowDeliveryBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
            val rowDeliveryBinding = RowDeliveryBinding.inflate(layoutInflater)
            val deliveryViewHolder = DeliveryViewHolder(rowDeliveryBinding)
            return deliveryViewHolder
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
            holder.rowDeliveryBinding.textViewDeliveryName.text = "홍길동"
            holder.rowDeliveryBinding.textViewDeliveryAdressName.text = "(공중화장실)"
            holder.rowDeliveryBinding.textViewDeliveryAddress.text = "대구광역시 북구 손난로 100길"
            holder.rowDeliveryBinding.textViewDeliveryPhone.text = "010-1544-7979"
        }


    }
}