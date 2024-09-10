package com.ivis.qcauditapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class QcPreferences(context: Context) {
    private val preferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    fun saveString(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }
    // Retrieve a string value from SharedPreferences
    fun getString(key: String, defaultValue: String = ""): String {
        return preferences.getString(key, defaultValue) ?: defaultValue
    }

    // Save an integer value to SharedPreferences
    fun saveInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    // Retrieve an integer value from SharedPreferences
    fun getInt(key: String, defaultValue: Int = 0): Int {
        return preferences.getInt(key, defaultValue)
    }

    // Save a boolean value to SharedPreferences
    fun saveBoolean(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    // Retrieve a boolean value from SharedPreferences
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    // Remove a value from SharedPreferences
    fun removeValue(key: String) {
        preferences.edit().remove(key).apply()
    }

    // Clear all values from SharedPreferences
    fun clearPreferences() {
        preferences.edit().clear().apply()
    }

    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = Date(System.currentTimeMillis())
        return dateFormat.format(currentDate)
    }
}
