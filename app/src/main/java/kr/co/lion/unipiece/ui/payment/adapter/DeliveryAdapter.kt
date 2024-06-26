package kr.co.lion.unipiece.ui.payment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.RowDeliveryBinding
import kr.co.lion.unipiece.model.DeliveryData
import kr.co.lion.unipiece.ui.payment.DeliveryAddFragment
import kr.co.lion.unipiece.ui.payment.DeliveryUpdateFragment
import kr.co.lion.unipiece.util.CustomDialog
import kr.co.lion.unipiece.util.DeliveryFragmentName
import java.util.Locale

// 배송지 화면의 RecyclerView의 어뎁터
class DeliveryAdapter(
    private var deliveryList: List<DeliveryData>,
    private val rowClickListener: (Int) -> Unit,
    private val updateButtonClickListener: (Int) -> Unit,
    private val deleteButtonClickListener: (Int) -> Unit
) : RecyclerView.Adapter<DeliveryViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): DeliveryViewHolder {

        val binding: RowDeliveryBinding =
            RowDeliveryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return DeliveryViewHolder(
            viewGroup.context,
            binding,
            rowClickListener,
            updateButtonClickListener,
            deleteButtonClickListener
        )
    }

    override fun getItemCount(): Int {

        return deliveryList.size

    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        holder.bind(deliveryList[position], rowClickListener)

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
    private val binding: RowDeliveryBinding,
    private val rowClickListener: (Int) -> Unit,
    private val updateButtonClickListener: (Int) -> Unit,
    private val deleteButtonClickListener: (Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {


    // 배송지 항목별로 세팅.
    fun bind(data: DeliveryData, rowClickListener: (Int) -> Unit) {

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


            // 항목 하나
            with(root) {
                layoutParams = ViewGroup.LayoutParams(
                    // 항목 클릭 시 클릭되는 범위 설정
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                // 클릭 리스너 설정. 클릭하면 deliveryIdx를 전달한다.
                setOnClickListener {
                    rowClickListener.invoke(data.deliveryIdx)
                }
            }
            // 화살표 버튼 클릭 시
            buttonDeliverySelect.setOnClickListener {
                rowClickListener.invoke(data.deliveryIdx)
            }


            // 항목별 삭제 버튼 클릭 시 다이얼로그
            buttonDeliveryDelete.setOnClickListener {
                deleteButtonClickListener.invoke(data.deliveryIdx)

            }

            // 항목별 수정 버튼
            with(buttonDeliveryUpdate) {
                // 버튼 클릭 시
                setOnClickListener {
                    updateButtonClickListener.invoke(data.deliveryIdx)
                }
            }
        }
    }
}

