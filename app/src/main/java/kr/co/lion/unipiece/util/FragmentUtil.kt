package kr.co.lion.unipiece.util

class FragmentUtil {

}

// MainActivity에서 보여줄 프래그먼트들의 이름
enum class MainFragmentName(var str:String){
    HOME_FRAGMENT("HomeFragment"),
    BUY_FRAGMENT("BuyFragment"),
    RANK_FRAGMENT("RankFragment"),
    MY_GALLERY_FRAGMENT("MyGalleryFragment"),
    MY_PAGE_FRAGMENT("MyPageFragment")
}

// PurchasedPieceDetailActivity 프래그먼트 이름
enum class PurchasedPieceDetailFragmentName(var str: String) {
    PURCHEASED_PIECE_DETAIL_FRAGMENT("PurchasedPieceDetailFramgent"),
    PURCHASE_CANCEL_FRAGEMNT("PurchaseCancelFragment"),
    REFUND_FRAGMENT("Refund_Fragment")
}