package com.example.sharedpreference

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

    fun saveUser(name: String, mobile: String) {
        val editor = prefs.edit()
        editor.putString("user_name", name)
        editor.putString("user_mobile", mobile)
        editor.apply()
    }

    fun getUserName(): String? {
        return prefs.getString("user_name", null)
    }

    fun getUserMobile(): String? {
        return prefs.getString("user_mobile", null)
    }
}
