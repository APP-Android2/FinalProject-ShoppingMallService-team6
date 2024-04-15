package kr.co.lion.unipiece.ui.payment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.databinding.RowDeliveryBinding
import kr.co.lion.unipiece.model.DeliveryData
import kr.co.lion.unipiece.ui.payment.delivery.CustomFullDialogListener
import kr.co.lion.unipiece.ui.payment.delivery.CustomFullDialogMaker
import kr.co.lion.unipiece.ui.payment.delivery.DeliveryViewModel
import kr.co.lion.unipiece.util.CustomDialog

// 배송지 화면의 RecyclerView의 어뎁터
class DeliveryAdapter : RecyclerView.Adapter<DeliveryViewHolder>() {

    private var deliveryList = mutableListOf<DeliveryData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowDeliveryBinding.inflate(inflater, parent, false)
        return DeliveryViewHolder(parent.context, binding)
    }

    override fun getItemCount(): Int {
        return deliveryList.size
    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        val item = deliveryList[position] // deliveryList는 예시로, 실제 데이터 리스트 변수명에 맞춰주세요.
        holder.bind(item)
    }


}

class DeliveryViewHolder(private val context: Context,private val binding: RowDeliveryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: DeliveryData) { // 예시 타입, 실제 타입으로 대체 필요
        binding.apply {
            textViewDeliveryName.text = data.deliveryName
            textViewDeliveryNickName.text = data.deliveryNickName
            textViewDeliveryPhone.text = data.deliveryPhone
            textViewDeliveryAddress.text = data.deliveryAddress
        }
    }

    init {
        // 항목별 삭제 버튼 클릭 시 다이얼로그
        this.binding.buttonDeliveryDelete.setOnClickListener {
            val dialog = CustomDialog("배송지 삭제", "이 배송지를 삭제하시겠습니까?")
            dialog.setButtonClickListener(object : CustomDialog.OnButtonClickListener {
                override fun okButtonClick() {

                }

                override fun noButtonClick() {

                }

            })
            //dialog.show(context, "CustomDialog")

        }

        // 항목별 수정 버튼 클릭 시 풀스크린 다이얼로그
        this.binding.buttonDeliveryUpdate.setOnClickListener {

            // 커스텀 다이얼로그 (풀스크린)
            CustomFullDialogMaker.apply {
                // 다이얼로그 호출
                getDialog(
                    context,
                    "배송지 수정",
                    "저장하기",
                    object : CustomFullDialogListener {

                        // 클릭한 이후 동작
                        // 저장하기 버튼 클릭 후 동작
                        override fun onClickSaveButton() {

                        }

                        // 뒤로가기 버튼 클릭 후 동작
                        override fun onClickCancelButton() {
                            Toast.makeText(context, "뒤로갔다!", Toast.LENGTH_SHORT)
                                .show()


                        }
                    }
                )
            }
        }

        // 선택 버튼 클릭 시
        this.binding.buttonDeliverySelect.setOnClickListener {

        }

        // 항목 클릭 시 클릭되는 범위 설정
        this.binding.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
