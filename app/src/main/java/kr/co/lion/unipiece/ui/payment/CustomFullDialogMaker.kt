package kr.co.lion.unipiece.ui.payment

import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.util.Log
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.DialogDeliveryAddBinding
import kr.co.lion.unipiece.model.DeliveryData

object CustomFullDialogMaker {

    // 기능 구현부에서 커스텀 다이얼로그를 호출할 함수
    fun getDialog(
        context: Context,
        title: String,
        saveButtonText: String,
        target: CustomFullDialogListener,
        addReceiver: String,
        addPhone: String,
        addNickName: String,
        addAddress: String,
        addAddressDetail: String,
        basicDelivery: Boolean,
        userIdx: Int,
        deliveryIdx: Int,
    ) {
        val dialog = Dialog(context, R.style.Theme_UniPiece)
        val dialogBinding = DialogDeliveryAddBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(dialogBinding.root)

        // DB로 넘겨줄 배송지 데이터 세팅
        fun insertDeliveryData(): DeliveryData {
            with(dialogBinding) {
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


        // 바인딩
        with(dialogBinding) {

            // 툴바
            with(toolbarDeliveryAdd) {

                // 툴바 제목과 연결
                this.title = title

                // 뒤로가기 내비 아이콘 연결
                setNavigationIcon(R.drawable.back_icon)

                // // 뒤로가기 내비게이션버튼 클릭 시 동작
                setNavigationOnClickListener {
                    target.onClickCancelButton()
                    // 다이얼로그 종료
                    dialog.dismiss()
                }
                // 다이얼로그 출력
                dialog.show()
            }
            // 배송지 수정으로 넘어왔을 경우 셋팅
            textFieldDeliveryAddReceiver.text = addReceiver.toEditable()
            textFieldDeliveryAddPhone.text = addPhone.toEditable()
            val length = addNickName.length
            textFieldDeliveryAddNickName.text = addNickName.substring(1, length - 1).toEditable()
            textFieldDeliveryAddAddress.text = addAddress.toEditable()
            textFieldDeliveryAddAddressDetail.text = addAddressDetail.toEditable()
            checkBoxAddDeliveryBasicDelivery.isChecked = basicDelivery
            // 저장버튼
            with(saveButton) {

                // 저장버튼 텍스트와 연결
                text = saveButtonText

                // 저장버튼 클릭 시 동작
                setOnClickListener {

                    // 배송지 데이터를 담아서 보내준다.
                    val deliveryData = insertDeliveryData()
                    target.onClickSaveButton(deliveryData)


                    // 다이얼로그 종료
                    dialog.dismiss()
                }
                // 다이얼로그 출력
                dialog.show()
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
    }

    // String 타입을 editable 타입으로 형변환 코드
    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)


}