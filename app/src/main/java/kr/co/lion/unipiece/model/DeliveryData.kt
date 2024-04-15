package kr.co.lion.unipiece.model

data class DeliveryData (
    var deliveryName : String,
    var deliveryPhone : String,
    var deliveryNickName : String,
    var deliveryAddress : String,
    var deliveryMemo : String,
    var basicDelivery : Boolean,
    var userIdx : Int,
    var deliveryIdx : Int,

){
    constructor() : this("", "","","","",true,0,0)
}