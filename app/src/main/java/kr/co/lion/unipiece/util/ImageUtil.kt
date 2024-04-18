package kr.co.lion.unipiece.util

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import kr.co.lion.unipiece.R

fun Context.setImage(imageView: ImageView, url: String?) {

    Glide.with(this).load(url)
        .placeholder(R.drawable.icon) // 로딩 중일 때
        .error(R.drawable.icon) // 오류 발생 시
        .into(imageView)

}

//이미지Url을 이미지 이름으로 변환
fun Context.gettingImageName(imgUrl: String): String {
    // URL에서 마지막 슬래시 다음의 문자열을 반환
    val correctedUrl = imgUrl.replace("%2F", "/")
    return correctedUrl.substringAfterLast("/").substringBefore("?")
}

//이미지 이름을 다시 Url로 바꿔주는 메서드
fun Context.getImageUrlFromName(imageName: String): String {
    val baseUrl = "https://firebasestorage.googleapis.com/v0/b/unipiece-463d3.appspot.com/o/PromoteInfo%2F"
    val token = "?alt=media&token=dc9df86f-2006-4cf8-b354-1c9d56fc3bc3"
    return baseUrl + imageName + token
}


class SquareImageView : AppCompatImageView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, width) // 가로 크기를 높이에도 적용하여 정사각형으로 만듭니다.
    }
}