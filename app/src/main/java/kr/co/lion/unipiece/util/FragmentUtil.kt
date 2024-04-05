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

//LoginActivity에서 보여줄 프래그먼트들의 이름
enum class LoginFragmentName(var str:String){
    LOGIN_FRAGMENT("LoginFragment"),
    JOIN_FRAGMENT("JoinFragment")
}

//작가 등록 Fragment 이름
enum class AddAuthorFragmentName(var str:String){
    GUIDE_LINE_FRAGMENT("GuideLineFragment"),
    REGISTER_AUTHOR_FRAGMENT("RegisterAuthorFragment")
}

// PurchasedPieceDetailActivity 프래그먼트 이름
enum class PurchasedPieceDetailFragmentName(var str: String) {
    PURCHEASED_PIECE_DETAIL_FRAGMENT("PurchasedPieceDetailFramgent"),
    PURCHASE_CANCEL_FRAGEMNT("PurchaseCancelFragment"),
    REFUND_FRAGMENT("Refund_Fragment")
}

// UserInfoActivity 프래그먼트 이름
enum class UserInfoFragmentName(var str:String){
    USER_INFO_FRAGMENT("UserInfoFragment"),
    MODIFY_USER_INFO_FRAGMENT("ModifyUserInfoFragment"),
}

// VisitGalleryActivity 프래그먼트 이름
enum class VisitGalleryFragmentName(var str:String){
    VISIT_GALLERY_HISTORY_FRAGMENT("VisitGalleryHistoryFragment"),
    APPLY_VISIT_GALLERY_FRAGMENT("ApplyVisitGalleryFragment"),
}

enum class RankFragmentName(var str: String) {
    RANK_PIECE_FRAGMENT("RankPieceFragment"),
}