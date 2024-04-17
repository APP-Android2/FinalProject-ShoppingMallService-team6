package kr.co.lion.unipiece.ui.payment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.databinding.RowDeliveryBinding
import kr.co.lion.unipiece.model.DeliveryData
import kr.co.lion.unipiece.ui.payment.CustomFullDialogListener
import kr.co.lion.unipiece.ui.payment.CustomFullDialogMaker
import kr.co.lion.unipiece.ui.payment.viewmodel.DeliveryViewModel
import kr.co.lion.unipiece.util.CustomDialog
import java.util.Locale

// 배송지 화면의 RecyclerView의 어뎁터
class DeliveryAdapter(
    var deliveryList: List<DeliveryData>,
    private val itemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<DeliveryViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): DeliveryViewHolder {

        val binding: RowDeliveryBinding =
            RowDeliveryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return DeliveryViewHolder(
            viewGroup.context,
            binding,
            DeliveryViewModel(),
            itemClickListener
        )
    }

    override fun getItemCount(): Int {

        return deliveryList.size

    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        holder.bind(deliveryList[position], itemClickListener)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(list: List<DeliveryData>) {
        deliveryList = list
        notifyDataSetChanged()
        Log.d("update adapter", list.toString())
    }

}

class DeliveryViewHolder(
    private val context: Context,
    val binding: RowDeliveryBinding,
    private val viewModel: DeliveryViewModel,
    private val itemClickListener: (Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    // 배송지 항목별로 세팅.
    fun bind(data: DeliveryData, itemClickListener: (Int) -> Unit) {

        with(binding) {
            // 받는 이
            textViewDeliveryName.text = data.deliveryName
            // 배송지명 (별명)
            textViewDeliveryNickName.text = "(${data.deliveryNickName})"
            // 연락처
            val phoneNumberFormat =
                PhoneNumberUtils.formatNumber(data.deliveryPhone, Locale.getDefault().country)
            textViewDeliveryPhone.text = phoneNumberFormat
            // 주소
            textViewDeliveryAddress.text = data.deliveryAddress
            // 상세주소
            textViewDeliveryAddressDetail.text = data.deliveryAddressDetail

            // 기본 배송지
            buttonBasicDelivery.isVisible = data.basicDelivery
            imageViewBasicDeliveryCheck.isVisible = data.basicDelivery


            // 클릭 리스너 설정. 클릭하면 deliveryIdx를 전달한다.
            root.setOnClickListener {
                itemClickListener.invoke(data.deliveryIdx)
            }

            // 선택 버튼 클릭 시
            buttonDeliverySelect.setOnClickListener {
                itemClickListener.invoke(data.deliveryIdx)
            }


            // 항목별 삭제 버튼 클릭 시 다이얼로그
            buttonDeliveryDelete.setOnClickListener {
                val dialog = CustomDialog("배송지 삭제", "이 배송지를 삭제하시겠습니까?")
                dialog.setButtonClickListener(object : CustomDialog.OnButtonClickListener {
                    // 확인 버튼 클릭 시
                    override fun okButtonClick() {
                        // DB에 있는 해당 배송지 삭제 구현
                    }

                    // 취소 버튼 클릭 시
                    override fun noButtonClick() {

                    }

                })
                // dialog.show(dialog.childFragmentManager,"deleteDialog")

            }

            // 항목별 수정 버튼 클릭 시 풀스크린 다이얼로그
            buttonDeliveryUpdate.setOnClickListener {

                // 커스텀 다이얼로그 (풀스크린)
                with(CustomFullDialogMaker) {
                    // 다이얼로그 호출
                    getDialog(
                        context,
                        "배송지 수정",
                        "수정하기",
                        object : CustomFullDialogListener {

                            // 클릭한 이후 동작
                            // 수정하기 버튼 클릭 후 동작
                            override fun onClickSaveButton(deliveryData: DeliveryData) {
                                viewModel.viewModelScope.launch {
                                    viewModel.insertDeliveryData(deliveryData)



                                }

                            }

                            // 뒤로가기 버튼 클릭 후 동작
                            override fun onClickCancelButton() {

                            }
                        },

                        textViewDeliveryName.text.toString(),
                        textViewDeliveryPhone.text.toString(),
                        textViewDeliveryNickName.text.toString(),
                        textViewDeliveryAddress.text.toString(),
                        textViewDeliveryAddressDetail.text.toString(),
                        buttonBasicDelivery.isVisible,
                        data.userIdx,
                        data.deliveryIdx,
                    )
                }
            }

            // 항목 클릭 시 클릭되는 범위 설정
            root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }
}
