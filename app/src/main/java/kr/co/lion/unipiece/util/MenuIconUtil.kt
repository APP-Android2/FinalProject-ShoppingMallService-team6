package kr.co.lion.unipiece.util

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.Menu
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

fun Context.setMenuIconColor(menu: Menu, itemId: Int, color: Int){
        menu.findItem(itemId)?.icon?.mutate()?.let {
            it.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(this, color), PorterDuff.Mode.SRC_IN)
            menu.findItem(itemId).icon = it
        }
    }