package kr.co.lion.unipiece.model

import com.google.firebase.Timestamp

data class PieceBuyInfoData(
    var pieceBuyState: String = "",
    var pieceBuyName: String = "",
    var pieceBuyPhone: String = "",
    var pieceBuyAddress: String = "",
    var pieceBuyMemo: String = "",
    var pieceBuyCo: String = "",
    var pieceBuySendNum: String = "",
    var pieceBuyPrice: Int = 0,
    var pieceBuySendPrice: Int = 0,
    var pieceBuyTotalPrice: Int = 0,
    var pieceBuyMethod: String = "",
    var pieceBuyCancel: String = "",
    var pieceBuyRefund: String = "",
    var pieceBuyDetailRefund: String = "",
    var pieceBuyImg: String = "",
    var pieceBuyDate: Timestamp = Timestamp.now(),
    var pieceBuyIdx: Int = 0,
    var pieceIdx: Int = 0,
    var userIdx: Int = 0
)