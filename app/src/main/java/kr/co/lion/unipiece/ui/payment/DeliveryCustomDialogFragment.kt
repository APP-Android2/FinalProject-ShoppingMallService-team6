package kr.co.lion.unipiece.ui.payment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.fragment.app.DialogFragment
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentDeliveryCustomDialogBinding
import kr.co.lion.unipiece.model.DeliveryData
import java.util.concurrent.locks.ReentrantLock

class DeliveryCustomDialogFragment(
    val title: String,
    val saveButtonText: String,
    val addReceiver: String,
    val addPhone: String,
    val addNickName: String,
    val addAddress: String,
    val addAddressDetail: String,
    val basicDelivery: Boolean,
    val userIdx: Int,
    val deliveryIdx: Int
) : DialogFragment() {

    private lateinit var binding: FragmentDeliveryCustomDialogBinding

    override fun onStart() {
        super.onStart()


        // full Screen code
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeliveryCustomDialogBinding.inflate(inflater, container, false)


        fun insertDeliveryData(): DeliveryData {
            with(binding) {
                val deliveryName = textFieldDeliveryAddReceiver.text.toString()
                val deliveryPhone = textFieldDeliveryAddPhone.text.toString()
                val deliveryNickName = textFieldDeliveryAddNickName.text.toString()
                val deliveryAddress = textFieldDeliveryAddAddress.text.toString()
                val deliveryAddressDetail = textFieldDeliveryAddAddressDetail.text.toString()
                val basicDelivery = checkBoxAddDeliveryBasicDelivery.isChecked
                val deliveryMemo = ""

                return DeliveryData(
                    deliveryName,
                    deliveryPhone,
                    deliveryNickName,
                    deliveryAddress,
                    deliveryAddressDetail,
                    deliveryMemo,
                    basicDelivery,
                    userIdx,
                    deliveryIdx
                )
            }
        }


        with(binding) {
            // 툴바
            with(toolbarDeliveryAdd) {
                // 툴바 타이틀
                this.title = title

                setNavigationOnClickListener {
                    buttonCilckListener.onClickCancelButton()
                    dismiss()
                }
            }

            // 배송지 수정으로 넘어왔을 경우 셋팅
            textFieldDeliveryAddReceiver.text = addReceiver.toEditable()
            textFieldDeliveryAddPhone.text = addPhone.toEditable()
            val length = addNickName.length
            textFieldDeliveryAddNickName.text = addNickName.substring(1, length - 1).toEditable()
            textFieldDeliveryAddAddress.text = addAddress.toEditable()
            textFieldDeliveryAddAddressDetail.text = addAddressDetail.toEditable()
            checkBoxAddDeliveryBasicDelivery.isChecked = basicDelivery


            // 저장하기 버튼
            with(saveButton) {

                // 저장버튼 텍스트와 연결
                text = saveButtonText

                setOnClickListener {

                    val deliveryData = insertDeliveryData()
                    buttonCilckListener.onClickSaveButton(deliveryData)
                    dismiss()
                }
            }
            // 집 클릭 시
            buttonDeliveryAddHouse.setOnClickListener {
                textFieldDeliveryAddNickName.isEnabled = false
                textFieldDeliveryAddNickName.text = "집".toEditable()
            }
            // 회사 클릭 시
            buttonDeliveryAddCompany.setOnClickListener {
                textFieldDeliveryAddNickName.isEnabled = false
                textFieldDeliveryAddNickName.text = "회사".toEditable()
            }
            // 학교 클릭 시
            buttonDeliveryAddSchool.setOnClickListener {
                textFieldDeliveryAddNickName.isEnabled = false
                textFieldDeliveryAddNickName.text = "학교".toEditable()
            }
            // 친구 클릭 시
            buttonDeliveryAddFriend.setOnClickListener {
                textFieldDeliveryAddNickName.isEnabled = false
                textFieldDeliveryAddNickName.text = "친구".toEditable()
            }
            // 가족 클릭 시
            buttonDeliveryAddFamily.setOnClickListener {
                textFieldDeliveryAddNickName.isEnabled = false
                textFieldDeliveryAddNickName.text = "가족".toEditable()
            }
            // 집 클릭 시
            buttonDeliveryAddDirectly.setOnClickListener {
                textFieldDeliveryAddNickName.isEnabled = true
                textFieldDeliveryAddNickName.text = "".toEditable()

            }
            // 주소 검색
            with(buttonDeliveryAddAdressSearch) {
                setOnClickListener {

                }
            }

            // 주소 비활성화
            textFieldDeliveryAddAddress.isEnabled = false
        }

        return binding.root
    }

    interface DeliveryCustomDialogListener {
        fun onClickSaveButton(deliveryData: DeliveryData)

        fun onClickCancelButton()

    }

    //클릭 이벤트 설정
    fun setButtonClickListener(buttonClickListener: DeliveryCustomDialogListener) {
        this.buttonCilckListener = buttonClickListener
    }

    //클릭 이벤트 설정
    private lateinit var buttonCilckListener: DeliveryCustomDialogListener

    // String 타입을 editable 타입으로 형변환 코드
    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
}