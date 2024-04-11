package kr.co.lion.unipiece.util

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import kr.co.lion.unipiece.R

fun Context.setImage(imageView: ImageView, url: String?) {
    Glide.with(this).load(url)
        .placeholder(R.drawable.icon) // 로딩할때
        .error(R.drawable.icon)
        .into(imageView)
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