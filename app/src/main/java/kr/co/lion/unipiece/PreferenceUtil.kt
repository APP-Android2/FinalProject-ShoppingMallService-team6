package kr.co.lion.unipiece

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("UserIdx", Context.MODE_PRIVATE)
    private val autoLogin: SharedPreferences = context.getSharedPreferences("userId", Context.MODE_PRIVATE)
    private val deleteUserIdx : SharedPreferences = context.getSharedPreferences("userIdx", Context.MODE_PRIVATE)
    private val deleteUserId : SharedPreferences = context.getSharedPreferences("userId", Context.MODE_PRIVATE)

    fun getUserIdx(key:String, defValue:Int) :Int{
        return prefs.getString(key, defValue.toString()).toString().toInt()
    }

    fun setUserIdx(key: String, int:Int){
        prefs.edit().putString(key, int.toString()).apply()
    }

    fun getAutoLogin(key :String, defValue: String) :String{
        return autoLogin.getString(key, defValue).toString()
    }

    fun setAutoLogin(key: String, str:String){
        autoLogin.edit().putString(key, str).apply()
    }

    fun deleteUserIdx(key:String){
        deleteUserIdx.edit().remove(key).apply()
    }

    fun deleteUserId(key:String){
        deleteUserId.edit().remove(key).apply()
    }
}