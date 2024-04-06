package kr.co.lion.unipiece.ui.payment.delivery

import android.app.Dialog
import android.content.Context
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.DialogDeliveryAddBinding

object CustomFullDialogMaker {

    // 기능 구현부에서 커스텀 다이얼로그를 호출할 함수
    fun getDialog(
        context: Context,
        title: String,
        saveButtonText: String,
        target: CustomFullDialogListener
    ) {
        val dialog = Dialog(context, R.style.Theme_UniPiece)
        val dialogBinding = DialogDeliveryAddBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(dialogBinding.root)



        dialogBinding.apply {

            // 툴바
            toolbarDeliveryAdd.apply {

                // 툴바 제목과 연결
                this.title = title

                // 뒤로가기 내비 아이콘 연결
                this.setNavigationIcon(R.drawable.back_icon)

                // // 뒤로가기 내비게이션버튼 클릭 시 동작
                this.setNavigationOnClickListener {
                    target.onClickCancelButton()

                    // 다이얼로그 종료
                    dialog.dismiss()
                }
                // 다이얼로그 출력
                dialog.show()
            }

            // 저장버튼
            saveButton.apply {

                // 저장버튼 텍스트와 연결
                this.text = saveButtonText

                // 저장버튼 클릭 시 동작
                this.setOnClickListener {
                    target.onClickSaveButton()

                    // 다이얼로그 종료
                    dialog.dismiss()
                }
                // 다이얼로그 출력
                dialog.show()
            }
        }
    }
}