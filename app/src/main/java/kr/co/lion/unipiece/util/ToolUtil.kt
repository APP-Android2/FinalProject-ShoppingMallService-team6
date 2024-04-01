package kr.co.lion.unipiece.util

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.SystemClock
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.concurrent.thread

class ToolUtil {
    companion object {
        // 뷰에 포커스 주고 키보드 올리기
        fun showSoftInput(context: Context, view: View){
            view.requestFocus()
            thread {
                SystemClock.sleep(200)
                val inputMethodManger = context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManger.showSoftInput(view, 0)
            }
        }

        // 키보드 내리고 포커스 제거
        fun hideSoftInput(activity: Activity){
            // 포커스를 가지고 있는 뷰가 있을 경우
            if(activity.window.currentFocus != null){
                val inputMethodManger = activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManger.hideSoftInputFromWindow(activity.window.currentFocus?.windowToken, 0)
                activity.window.currentFocus?.clearFocus()
            }
        }
    }
}