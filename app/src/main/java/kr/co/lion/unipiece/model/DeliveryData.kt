package kr.co.lion.unipiece.model

data class DeliveryData (
    var deliveryName : String = "",
    var deliveryPhone : String = "",
    var deliveryNickName : String = "",
    var deliveryAddress : String = "",
    var deliveryAddressDetail : String = "",
    var deliveryMemo : String = "",
    var basicDelivery : Boolean = false,
    var userIdx : Int = -1,
    var deliveryIdx : Int = -1,

)