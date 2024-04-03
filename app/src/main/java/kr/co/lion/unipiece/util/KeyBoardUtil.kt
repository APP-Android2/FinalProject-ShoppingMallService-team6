package kr.co.lion.unipiece.util

import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.SystemClock
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlin.concurrent.thread

    fun Context.showSoftInput(view: View){
        view.requestFocus()
        thread {
            SystemClock.sleep(200)
            val inputMethodManger = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManger.showSoftInput(view, 0)
        }
    }

    fun Activity.hideSoftInput(){
        if(window.currentFocus != null) {
            val inputMethodManger = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManger.hideSoftInputFromWindow(window.currentFocus?.windowToken, 0)
            window.currentFocus?.clearFocus()
        }
    }
