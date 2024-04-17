package kr.co.lion.unipiece.ui.payment

import kr.co.lion.unipiece.model.DeliveryData

interface CustomFullDialogListener {
    fun onClickSaveButton(deliveryData: DeliveryData)

    fun onClickCancelButton()
}
