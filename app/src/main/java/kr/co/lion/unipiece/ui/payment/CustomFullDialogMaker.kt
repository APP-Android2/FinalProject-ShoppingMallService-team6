package kr.co.lion.unipiece.ui.payment

import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.util.Log
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.DialogDeliveryAddBinding

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
    ) {
        val dialog = Dialog(context, R.style.Theme_UniPiece)
        val dialogBinding = DialogDeliveryAddBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(dialogBinding.root)



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
            // 수정할 정보 가져오기
            textFieldDeliveryAddReceiver.text = addReceiver.toEditable()
            textFieldDeliveryAddPhone.text = addPhone.toEditable()
            val length = addNickName.length
            Log.d("test1234","$length")
            textFieldDeliveryAddNickName.text = addNickName.substring(1,length-1).toEditable()
            textFieldDeliveryAddAdress.text = addAddress.toEditable()
            textFieldDeliveryAddAddressDetail.text = addAddressDetail.toEditable()
            checkBoxAddDeliveryBasicDelivery.isChecked = basicDelivery
            // 저장버튼
            with(saveButton) {

                // 저장버튼 텍스트와 연결
                text = saveButtonText

                // 저장버튼 클릭 시 동작
                setOnClickListener {


                    target.onClickSaveButton()


                    // 다이얼로그 종료
                    dialog.dismiss()
                }
                // 다이얼로그 출력
                dialog.show()
            }
            // 집 클릭 시
            buttonDeliveryAddHouse.setOnClickListener{
                textFieldDeliveryAddNickName.isEnabled = false
                textFieldDeliveryAddNickName.text = "집".toEditable()
            }
            // 회사 클릭 시
            buttonDeliveryAddCompany.setOnClickListener{
                textFieldDeliveryAddNickName.isEnabled = false
                textFieldDeliveryAddNickName.text = "회사".toEditable()
            }
            // 학교 클릭 시
            buttonDeliveryAddSchool.setOnClickListener{
                textFieldDeliveryAddNickName.isEnabled = false
                textFieldDeliveryAddNickName.text = "학교".toEditable()
            }
            // 친구 클릭 시
            buttonDeliveryAddFriend.setOnClickListener{
                textFieldDeliveryAddNickName.isEnabled = false
                textFieldDeliveryAddNickName.text = "친구".toEditable()
            }
            // 가족 클릭 시
            buttonDeliveryAddFamily.setOnClickListener{
                textFieldDeliveryAddNickName.isEnabled = false
                textFieldDeliveryAddNickName.text = "가족".toEditable()
            }
            // 집 클릭 시
            buttonDeliveryAddDirectly.setOnClickListener{
                textFieldDeliveryAddNickName.isEnabled = true
                textFieldDeliveryAddNickName.text = "".toEditable()

            }
            // 주소 검색
            with(buttonDeliveryAddAdressSearch){
                setOnClickListener {

                }
            }

            // 주소 비활성화
            textFieldDeliveryAddAdress.isEnabled = false


        }
    }
    // String 타입을 editable 타입으로 형변환 코드
    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
}